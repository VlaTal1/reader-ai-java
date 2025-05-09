package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TEST")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Test extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_SEQ_ID")
    @SequenceGenerator(name = "TEST_SEQ_ID", sequenceName = "TEST_SEQ_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRESS_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_TEST_PROGRESS"))
    private Progress progress;

    @Column(name = "QUESTIONS_AMOUNT", nullable = false)
    private Integer questionsAmount;

    @Column(name = "START_PAGE", nullable = false)
    private Integer startPage;

    @Column(name = "END_PAGE", nullable = false)
    private Integer endPage;

    @Column(name = "COMPLETED", nullable = false)
    private String completed;

    @Column(name = "DUE_TO")
    private LocalDateTime dueTo;

    @Column(name = "PASSED_DATE")
    private LocalDateTime passedDate;

    @Column(name = "GRADE")
    private Integer grade;

    @Column(name = "CORRECT_ANSWERS")
    private Integer correctAnswers;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "test")
    private List<Question> questions;
}