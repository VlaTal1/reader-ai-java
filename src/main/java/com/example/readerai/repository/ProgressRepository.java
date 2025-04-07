package com.example.readerai.repository;

import com.example.readerai.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Progress findByBookIdAndParticipantId(Long bookId, Long participantId);
}
