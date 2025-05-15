package com.example.readerai.service;

import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.dto.CompleteStatus;
import com.example.readerai.dto.statistics.GradeByParticipant;
import com.example.readerai.entity.Participant;
import com.example.readerai.entity.Test;
import com.example.readerai.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final TestRepository testRepository;

    private final ParticipantConverter participantConverter;

    private final UserService userService;

    public List<GradeByParticipant> getParticipantsGrade() {
        // Fetch all tests from repository
        List<Test> allTests = testRepository.findAllByProgress_Participant_UserId(userService.getUserId());

        // Create map to store results by participant ID
        Map<Long, GradeByParticipant> resultMap = new HashMap<>();

        // Process each test
        for (Test test : allTests) {
            // Get participant
            Participant participant = test.getProgress().getParticipant();
            Long participantId = participant.getId();

            // Get or create GradeByParticipant object
            GradeByParticipant gradeData = resultMap.computeIfAbsent(
                    participantId,
                    id -> GradeByParticipant.builder()
                            .participant(participantConverter.toDTO(participant))
                            .avgGrade(0f)
                            .totalTests(0)
                            .completedTests(0)
                            .build()
            );

            // Increment total tests count
            gradeData.setTotalTests(gradeData.getTotalTests() + 1);

            // If test is completed, update completed count and recalculate average
            if (CompleteStatus.COMPLETED.toString().equals(test.getCompleted())) {
                int newCompletedCount = gradeData.getCompletedTests() + 1;
                gradeData.setCompletedTests(newCompletedCount);

                // Update running average
                float currentTotal = gradeData.getAvgGrade() * (newCompletedCount - 1);
                float newAverage = (currentTotal + test.getGrade()) / newCompletedCount;
                gradeData.setAvgGrade(newAverage);
            }
        }

        // Convert map values to list
        return new ArrayList<>(resultMap.values());
    }
}
