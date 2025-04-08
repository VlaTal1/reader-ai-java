package com.example.readerai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO extends AuditDTO {

    private Long id;

    private String question;

    private String quote;

    private List<AnswerDTO> answers;
}