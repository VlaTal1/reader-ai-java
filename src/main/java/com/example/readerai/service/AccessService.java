package com.example.readerai.service;

import com.example.readerai.converter.AccessConverter;
import com.example.readerai.converter.BookConverter;
import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.dto.AccessDTO;
import com.example.readerai.dto.BookDTO;
import com.example.readerai.dto.ParticipantDTO;
import com.example.readerai.entity.Access;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.repository.AccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessRepository accessRepository;

    private final ParticipantService participantService;

    private final BookService bookService;

    private final ParticipantConverter participantConverter;

    private final BookConverter bookConverter;

    private final AccessConverter accessConverter;

    private final ProgressService progressService;

    public AccessDTO grantAccess(Long bookId, Long participantId) throws NotFoundException {
        ParticipantDTO participant = participantService.getById(participantId);
        BookDTO book = bookService.getBookById(bookId);

        Access access = Access.builder()
                .participant(participantConverter.fromDTO(participant))
                .book(bookConverter.fromDTO(book))
                .build();
        access = accessRepository.save(access);

        progressService.createIfNotExists(bookId, participantId);

        return accessConverter.toDTO(access);
    }

    public List<AccessDTO> getAccessesByBookId(Long bookId) {
        List<Access> accesses = accessRepository.findByBookId(bookId);
        return accesses.stream()
                .map(accessConverter::toDTO)
                .collect(Collectors.toList());
    }
}
