package com.example.readerai.service;

import com.example.readerai.converter.ParticipantConverter;
import com.example.readerai.dto.ParticipantDTO;
import com.example.readerai.entity.Participant;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    private final ParticipantConverter participantConverter;

    private final UserService userService;

    public ParticipantDTO save(ParticipantDTO participantDTO) {
        Participant participant = participantConverter.fromDTO(participantDTO);
        participant = participantRepository.save(participant);
        participant.setUserId(userService.getUserId());

        return participantConverter.toDTO(participant);
    }

    public List<ParticipantDTO> getAll() {
        String userId = userService.getUserId();
        List<Participant> participants = participantRepository.findByUserId(userId);
        return participants.stream().map(participantConverter::toDTO).collect(Collectors.toList());
    }

    public ParticipantDTO getById(Long id) throws NotFoundException {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participant with id " + id + " not found"));
        return participantConverter.toDTO(participant);
    }
}
