package com.example.readerai.service;

import com.example.readerai.converter.ReadingSessionConverter;
import com.example.readerai.dto.ReadingSessionDTO;
import com.example.readerai.entity.Progress;
import com.example.readerai.entity.ReadingSession;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.exception.PermissionDeniedException;
import com.example.readerai.repository.ProgressRepository;
import com.example.readerai.repository.ReadingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReadingSessionService {

    private final ReadingSessionRepository readingSessionRepository;

    private final ProgressRepository progressRepository;

    private final UserService userService;

    private final ReadingSessionConverter readingSessionConverter;

    public ReadingSessionDTO save(ReadingSessionDTO readingSessionDTO) {
        if (readingSessionDTO.getProgress() == null) {
            throw new IllegalArgumentException("Progress cannot be null");
        }
        if (readingSessionDTO.getStartTime() == null || readingSessionDTO.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time cannot be null");
        }

        Progress progress = progressRepository.findById(readingSessionDTO.getProgress().getId()).orElseThrow(
                () -> new NotFoundException("Progress not found")
        );
        if (!Objects.equals(progress.getParticipant().getUserId(), userService.getUserId())) {
            throw new PermissionDeniedException("User id mismatch");
        }

        long milliseconds = java.time.Duration.between(readingSessionDTO.getStartTime(), readingSessionDTO.getEndTime()).toMillis();

        readingSessionDTO.setTime(milliseconds);
        ReadingSession readingSession = readingSessionConverter.fromDTO(readingSessionDTO);
        ReadingSession savedSession = readingSessionRepository.save(readingSession);
        return readingSessionConverter.toDTO(savedSession);
    }
}
