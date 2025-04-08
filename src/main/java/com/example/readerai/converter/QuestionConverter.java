package com.example.readerai.converter;

import com.example.readerai.dto.QuestionDTO;
import com.example.readerai.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QuestionConverter {

    private final AnswerConverter answerConverter;

    public Question fromDTO(QuestionDTO entry) {
        Question question = Question.builder()
                .id(entry.getId())
                .question(entry.getQuestion())
                .quote(entry.getQuote())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getAnswers() != null) {
            question.setAnswers(entry.getAnswers().stream()
                    .map(answerConverter::fromDTO)
                    .collect(Collectors.toList()));
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

        if (entry.getAnswers() != null) {
            questionDTO.setAnswers(entry.getAnswers().stream()
                    .map(answerConverter::toDTO)
                    .collect(Collectors.toList()));
        }

        return questionDTO;
    }
}