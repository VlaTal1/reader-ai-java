package com.example.readerai.controller;

import com.example.readerai.dto.ProgressDTO;
import com.example.readerai.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/participant/{participantId}/book/{bookId}")
    public ResponseEntity<ProgressDTO> getProgress(@PathVariable Long participantId, @PathVariable Long bookId) {
        return ResponseEntity.ok(progressService.getProgressByBookIdAndParticipantId(bookId, participantId));
    }

    @PutMapping("/")
    public ResponseEntity<ProgressDTO> updateProgress(@RequestBody ProgressDTO progressDTO) {
        return ResponseEntity.ok(progressService.updateProgress(progressDTO));
    }
}
