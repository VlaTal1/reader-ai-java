package com.example.readerai.repository;

import com.example.readerai.entity.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Long> {
    List<ReadingSession> findAllByProgress_Participant_UserId(String userId);
    List<ReadingSession> findAllByProgress_Participant_Id(Long participantId);
}
