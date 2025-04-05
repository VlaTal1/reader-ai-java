package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "READING_SESSION")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSession extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "READING_SESSION_SEQ_ID")
    @SequenceGenerator(name = "READING_SESSION_SEQ_ID", sequenceName = "READING_SESSION_SEQ_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROGRESS_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_READING_SESSION_PROGRESS"))
    private Progress progress;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "START_PAGE", nullable = false)
    private Integer startPage;

    @Column(name = "END_PAGE")
    private Integer endPage;

    @Column(name = "TIME")
    private Long time;
}