package com.example.readerai.converter;

import com.example.readerai.dto.TestDTO;
import com.example.readerai.entity.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestConverter {

    private final ProgressConverter progressConverter;

    public Test fromDTO(TestDTO entry) {
        Test test = Test.builder()
                .id(entry.getId())
                .pagesPerQuestion(entry.getPagesPerQuestion())
                .startPage(entry.getStartPage())
                .endPage(entry.getEndPage())
                .completed(entry.getCompleted())
                .dueTo(entry.getDueTo())
                .passedDate(entry.getPassedDate())
                .grade(entry.getGrade())
                .correctAnswers(entry.getCorrectAnswers())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getProgress() != null) {
            test.setProgress(progressConverter.fromDTO(entry.getProgress()));
        }

        return test;
    }

    public TestDTO toDTO(Test entry) {
        TestDTO testDTO = TestDTO.builder()
                .id(entry.getId())
                .pagesPerQuestion(entry.getPagesPerQuestion())
                .startPage(entry.getStartPage())
                .endPage(entry.getEndPage())
                .completed(entry.getCompleted())
                .dueTo(entry.getDueTo())
                .passedDate(entry.getPassedDate())
                .grade(entry.getGrade())
                .correctAnswers(entry.getCorrectAnswers())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getProgress() != null) {
            testDTO.setProgress(progressConverter.toDTO(entry.getProgress()));
        }

        return testDTO;
    }
}