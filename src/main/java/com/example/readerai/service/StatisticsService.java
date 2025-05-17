package com.example.readerai.service;

import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.dto.CompleteStatus;
import com.example.readerai.dto.statistics.GradeByParticipant;
import com.example.readerai.dto.statistics.ReadingStatsByDay;
import com.example.readerai.entity.Participant;
import com.example.readerai.entity.ReadingSession;
import com.example.readerai.entity.Test;
import com.example.readerai.repository.ParticipantRepository;
import com.example.readerai.repository.ReadingSessionRepository;
import com.example.readerai.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // Получаем ID текущего пользователя
        String userId = userService.getUserId();

        // Получаем всех участников для данного пользователя
        List<Participant> participants = participantRepository.findByUserId(userId);

        // Создаем результирующий список
        List<ReadingStatsByDay> result = new ArrayList<>();

        for (Participant participant : participants) {
            // Для каждого участника создаем объект статистики
            ReadingStatsByDay participantStats = ReadingStatsByDay.builder()
                    .participant(participantConverter.toDTO(participant))
                    .dailyStats(new HashMap<>())
                    .build();

            // Получаем все сессии чтения для участника
            List<ReadingSession> readingSessions = readingSessionRepository.findAllByProgress_Participant_Id(participant.getId());

            for (ReadingSession session : readingSessions) {
                if (session.getStartTime() != null && session.getEndTime() != null) {
                    // Преобразуем время в дату
                    LocalDate sessionDay = session.getStartTime().toLocalDate();

                    // Вычисляем количество прочитанных страниц
                    final int pagesRead = calculatePagesRead(session);

                    // Обновляем дневную статистику
                    participantStats.getDailyStats().compute(sessionDay, (date, stats) -> {
                        if (stats == null) {
                            return ReadingStatsByDay.DailyReadingStats.builder()
                                    .totalReadingTimeMinutes(session.getTime())
                                    .totalPagesRead(pagesRead)
                                    .build();
                        } else {
                            stats.setTotalReadingTimeMinutes(stats.getTotalReadingTimeMinutes() + session.getTime());
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

    private int calculatePagesRead(ReadingSession session) {
        if (session.getEndPage() == null || session.getStartPage() == null) {
            return 0;
        }

        int pages = session.getEndPage() - session.getStartPage();
        return Math.max(0, pages);
    }
}