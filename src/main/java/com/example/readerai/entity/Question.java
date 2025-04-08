package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "QUESTION")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_ID")
    @SequenceGenerator(name = "QUESTION_SEQ_ID", sequenceName = "QUESTION_SEQ_ID", allocationSize = 1)
    private Long id;

    @Column(name = "QUESTION", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "QUOTE", nullable = false, columnDefinition = "TEXT")
    private String quote;

    @OneToMany
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID")
    private List<Answer> answers;
}