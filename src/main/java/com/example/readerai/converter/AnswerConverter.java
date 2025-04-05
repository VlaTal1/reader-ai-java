package com.example.readerai.converter;

import com.example.readerai.dto.AnswerDTO;
import com.example.readerai.entity.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerConverter {

    private final QuestionConverter questionConverter;

    public Answer fromDTO(AnswerDTO entry) {
        Answer answer = Answer.builder()
                .id(entry.getId())
                .answer(entry.getAnswer())
                .correct(entry.getCorrect())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getQuestion() != null) {
            answer.setQuestion(questionConverter.fromDTO(entry.getQuestion()));
        }

        return answer;
    }

    public AnswerDTO toDTO(Answer entry) {
        AnswerDTO answerDTO = AnswerDTO.builder()
                .id(entry.getId())
                .answer(entry.getAnswer())
                .correct(entry.getCorrect())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getQuestion() != null) {
            answerDTO.setQuestion(questionConverter.toDTO(entry.getQuestion()));
        }

        return answerDTO;
    }
}