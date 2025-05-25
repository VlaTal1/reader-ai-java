package com.example.readerai.controller;

import com.example.readerai.dto.ReadingSessionDTO;
import com.example.readerai.service.ReadingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readingSession")
@RequiredArgsConstructor
public class ReadingSessionController {

    private final ReadingSessionService readingSessionService;

    @PostMapping
    public ResponseEntity<ReadingSessionDTO> saveSession(@RequestBody ReadingSessionDTO readingSessionDTO) {
        return new ResponseEntity<>(readingSessionService.save(readingSessionDTO), HttpStatus.OK);
    }
}
