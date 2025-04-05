package com.example.readerai.converter;

import com.example.readerai.dto.ReadingSessionDTO;
import com.example.readerai.entity.ReadingSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadingSessionConverter {

    private final ProgressConverter progressConverter;

    public ReadingSession fromDTO(ReadingSessionDTO entry) {
        ReadingSession readingSession = ReadingSession.builder()
                .id(entry.getId())
                .startTime(entry.getStartTime())
                .endTime(entry.getEndTime())
                .startPage(entry.getStartPage())
                .endPage(entry.getEndPage())
                .time(entry.getTime())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getProgress() != null) {
            readingSession.setProgress(progressConverter.fromDTO(entry.getProgress()));
        }

        return readingSession;
    }

    public ReadingSessionDTO toDTO(ReadingSession entry) {
        ReadingSessionDTO readingSessionDTO = ReadingSessionDTO.builder()
                .id(entry.getId())
                .startTime(entry.getStartTime())
                .endTime(entry.getEndTime())
                .startPage(entry.getStartPage())
                .endPage(entry.getEndPage())
                .time(entry.getTime())
                .createdAt(entry.getCreatedAt())
                .updatedAt(entry.getUpdatedAt())
                .build();

        if (entry.getProgress() != null) {
            readingSessionDTO.setProgress(progressConverter.toDTO(entry.getProgress()));
        }

        return readingSessionDTO;
    }
}