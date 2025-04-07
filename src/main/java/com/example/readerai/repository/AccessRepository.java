package com.example.readerai.repository;

import com.example.readerai.entity.Access;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long> {
    public List<Access> findByBookId(Long bookId);

    Access findByBook_IdAndParticipant_Id(Long bookId, Long participantId);
}
