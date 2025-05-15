package com.example.readerai.dto.statistics;

import com.example.readerai.dto.ParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GradeByParticipant {

    private ParticipantDTO participant;

    private float avgGrade;

    private int totalTests;

    private int completedTests;
}
