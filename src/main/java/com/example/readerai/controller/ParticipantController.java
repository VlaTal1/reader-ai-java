package com.example.readerai.controller;

import com.example.readerai.dto.ParticipantDTO;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantDTO> saveParticipant(@RequestBody ParticipantDTO participantDTO) {
        ParticipantDTO savedParticipant = participantService.save(participantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipant);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants() {
        List<ParticipantDTO> participants = participantService.getAll();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) throws NotFoundException {
        ParticipantDTO participant = participantService.getById(id);
        return ResponseEntity.ok(participant);
    }
}
