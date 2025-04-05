package com.example.readerai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSessionDTO extends AuditDTO {

    private Long id;

    private ProgressDTO progress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer startPage;

    private Integer endPage;

    private Long time;
}