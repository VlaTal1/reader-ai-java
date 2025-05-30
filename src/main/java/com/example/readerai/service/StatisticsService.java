package com.example.readerai.service;

import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.dto.CompleteStatus;
import com.example.readerai.dto.statistics.GradeByParticipant;
import com.example.readerai.dto.statistics.ReadingStatsByDay;
import com.example.readerai.dto.statistics.WeeklyStatsByParticipant;
import com.example.readerai.entity.Participant;
import com.example.readerai.entity.ReadingSession;
import com.example.readerai.entity.Test;
import com.example.readerai.repository.ParticipantRepository;
import com.example.readerai.repository.ReadingSessionRepository;
import com.example.readerai.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TestRepository testRepository;
    private final ReadingSessionRepository readingSessionRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantConverter participantConverter;
    private final UserService userService;

    public List<GradeByParticipant> getParticipantsGrade() {
        List<Test> allTests = testRepository.findAllByProgress_Participant_UserId(userService.getUserId());

        Map<Long, GradeByParticipant> resultMap = new HashMap<>();

        for (Test test : allTests) {
            Participant participant = test.getProgress().getParticipant();
            Long participantId = participant.getId();

            GradeByParticipant gradeData = resultMap.computeIfAbsent(
                    participantId,
                    id -> GradeByParticipant.builder()
                            .participant(participantConverter.toDTO(participant))
                            .avgGrade(0f)
                            .totalTests(0)
                            .completedTests(0)
                            .build()
            );

            gradeData.setTotalTests(gradeData.getTotalTests() + 1);

            if (CompleteStatus.COMPLETED.toString().equals(test.getCompleted())) {
                int newCompletedCount = gradeData.getCompletedTests() + 1;
                gradeData.setCompletedTests(newCompletedCount);

                float currentTotal = gradeData.getAvgGrade() * (newCompletedCount - 1);
                float newAverage = (currentTotal + test.getGrade()) / newCompletedCount;
                gradeData.setAvgGrade(newAverage);
            }
        }

        List<GradeByParticipant> result = new ArrayList<>(resultMap.values());
        result.sort(Comparator.comparing(GradeByParticipant::getAvgGrade).reversed());

        return result;
    }

    public List<ReadingStatsByDay> getReadingStatsByDay() {
        String userId = userService.getUserId();

        List<Participant> participants = participantRepository.findByUserId(userId);

        List<ReadingStatsByDay> result = new ArrayList<>();

        for (Participant participant : participants) {
            ReadingStatsByDay participantStats = ReadingStatsByDay.builder()
                    .participant(participantConverter.toDTO(participant))
                    .dailyStats(new HashMap<>())
                    .build();

            List<ReadingSession> readingSessions = readingSessionRepository.findAllByProgress_Participant_Id(participant.getId());

            for (ReadingSession session : readingSessions) {
                if (session.getStartTime() != null && session.getEndTime() != null) {
                    LocalDate sessionDay = session.getStartTime().toLocalDate();

                    final int pagesRead = calculatePagesRead(session);
                    final float readingTimeMinutes = session.getTime() / (1000f * 60f);

                    participantStats.getDailyStats().compute(sessionDay, (date, stats) -> {
                        if (stats == null) {
                            return ReadingStatsByDay.DailyReadingStats.builder()
                                    .totalReadingTimeMinutes(readingTimeMinutes)
                                    .totalPagesRead(pagesRead)
                                    .build();
                        } else {
                            stats.setTotalReadingTimeMinutes(stats.getTotalReadingTimeMinutes() + readingTimeMinutes);
                            stats.setTotalPagesRead(stats.getTotalPagesRead() + pagesRead);
                            return stats;
                        }
                    });
                }
            }

            result.add(participantStats);
        }

        return result;
    }

    public List<WeeklyStatsByParticipant> getWeeklyStats() {
        String userId = userService.getUserId();
        List<Participant> participants = participantRepository.findByUserId(userId);

        int currentYear = LocalDate.now().getYear();
        LocalDate yearStart = LocalDate.of(currentYear, 1, 1);
        LocalDate yearEnd = LocalDate.of(currentYear, 12, 31);

        List<WeeklyStatsByParticipant> result = new ArrayList<>();

        for (Participant participant : participants) {
            WeeklyStatsByParticipant participantStats = WeeklyStatsByParticipant.builder()
                    .participant(participantConverter.toDTO(participant))
                    .weeklyStats(new HashMap<>())
                    .build();

            List<ReadingSession> readingSessions = readingSessionRepository
                    .findAllByProgress_Participant_Id(participant.getId())
                    .stream()
                    .filter(session -> session.getStartTime() != null &&
                            !session.getStartTime().toLocalDate().isBefore(yearStart) &&
                            !session.getStartTime().toLocalDate().isAfter(yearEnd))
                    .toList();

            List<Test> completedTests = testRepository
                    .findAllByProgress_Participant_IdAndCompletedAndYear(participant.getId(), CompleteStatus.COMPLETED.toString(), LocalDateTime.now().getYear())
                    .stream()
                    .filter(test -> test.getProgress().getEndDate() != null &&
                            !test.getProgress().getEndDate().toLocalDate().isBefore(yearStart) &&
                            !test.getProgress().getEndDate().toLocalDate().isAfter(yearEnd))
                    .toList();

            Map<LocalDate, List<Test>> testsByDay = new HashMap<>();
            for (Test test : completedTests) {
                LocalDate testDay = test.getProgress().getEndDate().toLocalDate();
                testsByDay.computeIfAbsent(testDay, k -> new ArrayList<>()).add(test);
            }

            for (ReadingSession session : readingSessions) {
                LocalDate sessionDay = session.getStartTime().toLocalDate();
                String weekKey = getWeekKey(sessionDay);

                final int pagesRead = calculatePagesRead(session);
                final float readingTimeMinutes = session.getTime() / (1000f * 60f);

                participantStats.getWeeklyStats().compute(weekKey, (week, stats) -> {
                    if (stats == null) {
                        stats = WeeklyStatsByParticipant.WeeklyReadingStats.builder()
                                .totalReadingTimeMinutes(0f)
                                .totalPagesRead(0)
                                .totalRatingSum(0f)
                                .ratingCount(0)
                                .build();
                    }

                    stats.setTotalReadingTimeMinutes(stats.getTotalReadingTimeMinutes() + readingTimeMinutes);
                    stats.setTotalPagesRead(stats.getTotalPagesRead() + pagesRead);

                    return stats;
                });
            }

            for (Map.Entry<LocalDate, List<Test>> entry : testsByDay.entrySet()) {
                LocalDate testDay = entry.getKey();
                String weekKey = getWeekKey(testDay);
                List<Test> dayTests = entry.getValue();

                if (!dayTests.isEmpty()) {
                    float dayAverageRating = (float) dayTests.stream()
                            .mapToDouble(Test::getGrade)
                            .average()
                            .orElse(0.0);

                    participantStats.getWeeklyStats().compute(weekKey, (week, stats) -> {
                        if (stats == null) {
                            stats = WeeklyStatsByParticipant.WeeklyReadingStats.builder()
                                    .totalReadingTimeMinutes(0f)
                                    .totalPagesRead(0)
                                    .totalRatingSum(0f)
                                    .ratingCount(0)
                                    .build();
                        }

                        stats.setTotalRatingSum(stats.getTotalRatingSum() + dayAverageRating);
                        stats.setRatingCount(stats.getRatingCount() + 1);

                        return stats;
                    });
                }
            }

            result.add(participantStats);
        }

        return result;
    }

    private String getWeekKey(LocalDate date) {
        java.time.temporal.WeekFields weekFields = java.time.temporal.WeekFields.of(java.util.Locale.getDefault());
        int weekOfYear = date.get(weekFields.weekOfWeekBasedYear());
        int year = date.get(weekFields.weekBasedYear());
        return String.format("%d-W%02d", year, weekOfYear);
    }

    private int calculatePagesRead(ReadingSession session) {
        if (session.getEndPage() == null || session.getStartPage() == null) {
            return 0;
        }

        int pages = session.getEndPage() - session.getStartPage();
        return Math.max(0, pages);
    }
}