package com.example.readerai.repository;

import com.example.readerai.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByProgress_Participant_Id(Long participantId);
    List<Test> findAllByProgress_Participant_UserId(String userId);
}
