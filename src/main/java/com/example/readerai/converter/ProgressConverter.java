package com.example.readerai.converter;

import com.example.readerai.dto.ProgressDTO;
import com.example.readerai.entity.Progress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressConverter {

    private final ParticipantConverter participantConverter;

    private final BookConverter bookConverter;

    public Progress fromDTO(ProgressDTO entry) {
        Progress progress = Progress.builder()
                .id(entry.getId())
                .readPages(entry.getReadPages())
                .currentPage(entry.getCurrentPage())
                .startDate(entry.getStartDate())
                .endDate(entry.getEndDate())
                .status(entry.getStatus())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getParticipant() != null) {
            progress.setParticipant(participantConverter.fromDTO(entry.getParticipant()));
        }

        if (entry.getBook() != null) {
            progress.setBook(bookConverter.fromDTO(entry.getBook()));
        }

        return progress;
    }

    public ProgressDTO toDTO(Progress entry) {
        ProgressDTO progressDTO = ProgressDTO.builder()
                .id(entry.getId())
                .readPages(entry.getReadPages())
                .currentPage(entry.getCurrentPage())
                .startDate(entry.getStartDate())
                .endDate(entry.getEndDate())
                .status(entry.getStatus())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getParticipant() != null) {
            progressDTO.setParticipant(participantConverter.toDTO(entry.getParticipant()));
        }

        if (entry.getBook() != null) {
            progressDTO.setBook(bookConverter.toDTO(entry.getBook()));
        }

        return progressDTO;
    }
}