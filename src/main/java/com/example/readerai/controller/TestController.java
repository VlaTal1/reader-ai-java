package com.example.readerai.controller;

import com.example.readerai.dto.TestDTO;
import com.example.readerai.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping
    public ResponseEntity<TestDTO> createTest(@RequestBody TestDTO testDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTest(testDTO));
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<List<TestDTO>> getTestsByParticipantId(@PathVariable Long participantId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.getTestsByParticipantId(participantId));
    }
}
