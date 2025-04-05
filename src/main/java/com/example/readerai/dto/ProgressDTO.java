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
public class ProgressDTO extends AuditDTO {

    private Long id;

    private ParticipantDTO participant;

    private BookDTO book;

    private Integer readPages;

    private Integer currentPage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;
}