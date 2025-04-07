package com.example.readerai.repository;

import com.example.readerai.entity.Access;
import com.example.readerai.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long> {
    public List<Access> findByBookId(Long bookId);

    Long book(Book book);
}
