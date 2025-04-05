package com.example.readerai.converter;

import com.example.readerai.dto.QuestionDTO;
import com.example.readerai.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionConverter {

    private final TestConverter testConverter;

    public Question fromDTO(QuestionDTO entry) {
        Question question = Question.builder()
                .id(entry.getId())
                .question(entry.getQuestion())
                .quote(entry.getQuote())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getTest() != null) {
            question.setTest(testConverter.fromDTO(entry.getTest()));
        }

        return question;
    }

    public QuestionDTO toDTO(Question entry) {
        QuestionDTO questionDTO = QuestionDTO.builder()
                .id(entry.getId())
                .question(entry.getQuestion())
                .quote(entry.getQuote())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getTest() != null) {
            questionDTO.setTest(testConverter.toDTO(entry.getTest()));
        }

        return questionDTO;
    }
}