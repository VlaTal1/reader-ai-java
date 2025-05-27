package com.example.readerai.service;

import com.example.readerai.converter.BookConverter;
import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.converter.ProgressConverter;
import com.example.readerai.dto.BookDTO;
import com.example.readerai.dto.ParticipantDTO;
import com.example.readerai.dto.ProgressDTO;
import com.example.readerai.dto.ReadingStatus;
import com.example.readerai.entity.Progress;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;

    private final ProgressConverter progressConverter;

    private final ParticipantService participantService;

    private final BookService bookService;

    private final ParticipantConverter participantConverter;

    private final BookConverter bookConverter;

    public ProgressDTO getProgressByBookIdAndParticipantId(Long bookId, Long participantId) throws NotFoundException {
        Progress progress = progressRepository.findByBookIdAndParticipantId(bookId, participantId);
        if (progress == null) {
            throw new NotFoundException("Progress with book id: " + bookId + " and participant id: " + participantId + " not found");
        }
        return progressConverter.toDTO(progress);
    }

    public void createIfNotExists(Long bookId, Long participantId) throws NotFoundException {
        try {
            getProgressByBookIdAndParticipantId(bookId, participantId);
        } catch (NotFoundException e) {
            ParticipantDTO participant = participantService.getById(participantId);
            BookDTO book = bookService.getBookById(bookId);

            Progress progress = Progress.builder()
                    .book(bookConverter.fromDTO(book))
                    .participant(participantConverter.fromDTO(participant))
                    .readPages(0)
                    .currentPage(0)
                    .startDate(LocalDateTime.now())
                    .endDate(null)
                    .status(ReadingStatus.NOT_STARTED.toString())
                    .build();

            progress = progressRepository.save(progress);
            progressConverter.toDTO(progress);
        } catch (Exception e) {
            throw new RuntimeException("Error while getting or creating progress", e);
        }
    }

    public ProgressDTO updateProgress(ProgressDTO progressDTO) {
        Progress progress = progressRepository.findById(progressDTO.getId()).orElseThrow(
                () -> new NotFoundException("Progress with id " + progressDTO.getId() + " not found")
        );

        if (Objects.equals(progress.getStatus(), ReadingStatus.NOT_STARTED.toString())) {
            progress.setStatus(ReadingStatus.IN_PROGRESS.toString());
        }

        progress.setReadPages(progressDTO.getReadPages());
        progress.setCurrentPage(progressDTO.getCurrentPage());

        Progress updatedProgress = progressRepository.save(progress);
        return progressConverter.toDTO(updatedProgress);
    }
}
