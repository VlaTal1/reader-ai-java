package com.example.readerai.repository;

import com.example.readerai.entity.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Long> {
    List<ReadingSession> findAllByProgress_Participant_Id(Long participantId);
    List<ReadingSession> findAllByProgress_Participant_IdAndStartTimeBetween(Long participantId, LocalDateTime start, LocalDateTime end);
}
