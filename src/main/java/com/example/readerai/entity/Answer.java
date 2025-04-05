package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ANSWER")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANSWER_SEQ_ID")
    @SequenceGenerator(name = "ANSWER_SEQ_ID", sequenceName = "ANSWER_SEQ_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ANSWER_QUESTION"))
    private Question question;

    @Column(name = "ANSWER", nullable = false)
    private String answer;

    @Column(name = "CORRECT", nullable = false)
    private Boolean correct;
}