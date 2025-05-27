package com.example.readerai.repository;

import com.example.readerai.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByProgress_Participant_Id(Long participantId);

    List<Test> findAllByProgress_Participant_IdAndProgress_Book_Id(Long participantId, Long bookId);
    List<Test> findAllByProgress_Participant_UserId(String userId);

    @Query(value = """
            SELECT t.* FROM test t
            JOIN progress p ON t.progress_id = p.id
            WHERE t.completed = 'NOT_STARTED'
            AND p.participant_id = :participantId
            AND p.book_id = :bookId
            ORDER BY t.end_page
            LIMIT 1
            """,
            nativeQuery = true)
    Test getFirstTestByParticipantIdAndBookId(@Param("participantId") Long participantId, @Param("bookId") Long bookId);
}
