package com.example.readerai.converter;

import com.example.readerai.dto.AnswerDTO;
import com.example.readerai.entity.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerConverter {

    public Answer fromDTO(AnswerDTO entry) {
        return Answer.builder()
                .id(entry.getId())
                .answer(entry.getAnswer())
                .correct(entry.getCorrect())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }

    public AnswerDTO toDTO(Answer entry) {
        return AnswerDTO.builder()
                .id(entry.getId())
                .answer(entry.getAnswer())
                .correct(entry.getCorrect())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }
}