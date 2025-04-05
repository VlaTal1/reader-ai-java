package com.example.readerai.converter;

import com.example.readerai.dto.AccessDTO;
import com.example.readerai.entity.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessConverter {

    private final ParticipantConverter participantConverter;

    private final BookConverter bookConverter;

    public Access fromDTO(AccessDTO entry) {
        Access access = Access.builder()
                .id(entry.getId())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getParticipant() != null) {
            access.setParticipant(participantConverter.fromDTO(entry.getParticipant()));
        }

        if (entry.getBook() != null) {
            access.setBook(bookConverter.fromDTO(entry.getBook()));
        }

        return access;
    }

    public AccessDTO toDTO(Access entry) {
        AccessDTO accessDTO = AccessDTO.builder()
                .id(entry.getId())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getParticipant() != null) {
            accessDTO.setParticipant(participantConverter.toDTO(entry.getParticipant()));
        }

        if (entry.getBook() != null) {
            accessDTO.setBook(bookConverter.toDTO(entry.getBook()));
        }

        return accessDTO;
    }
}