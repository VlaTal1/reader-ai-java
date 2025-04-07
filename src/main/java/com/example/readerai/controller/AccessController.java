package com.example.readerai.controller;

import com.example.readerai.dto.AccessDTO;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AccessController {

    private final AccessService accessService;

    @PostMapping("/grant/book/{bookId}/participant/{participantId}")
    public ResponseEntity<AccessDTO> grantAccess(@PathVariable Long bookId, @PathVariable Long participantId) throws NotFoundException {
        AccessDTO accessDTO = accessService.grantAccess(bookId, participantId);
        return ResponseEntity.ok(accessDTO);
    }
}
