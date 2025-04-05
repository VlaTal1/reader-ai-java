package com.example.readerai.converter;

import com.example.readerai.dto.ParticipantDTO;
import com.example.readerai.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipantConverter {

    public Participant fromDTO(ParticipantDTO entry) {
        return Participant.builder()
                .id(entry.getId())
                .supId(entry.getSupId())
                .name(entry.getName())
                .userId(entry.getUserId())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }

    public ParticipantDTO toDTO(Participant entry) {
        return ParticipantDTO.builder()
                .id(entry.getId())
                .supId(entry.getSupId())
                .name(entry.getName())
                .userId(entry.getUserId())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();
    }
}