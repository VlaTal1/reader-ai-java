package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEST_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_QUESTION_TEST"))
    private Test test;

    @Column(name = "QUESTION", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "QUOTE", nullable = false, columnDefinition = "TEXT")
    private String quote;
}