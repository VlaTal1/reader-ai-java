package com.example.readerai.service;

import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.dto.CompleteStatus;
import com.example.readerai.dto.statistics.GradeByParticipant;
import com.example.readerai.entity.Participant;
import com.example.readerai.entity.Test;
import com.example.readerai.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TestRepository testRepository;

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
}
