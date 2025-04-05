package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROGRESS")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Progress extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROGRESS_SEQ_ID")
    @SequenceGenerator(name = "PROGRESS_SEQ_ID", sequenceName = "PROGRESS_SEQ_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTICIPANT_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PROGRESS_PARTICIPANT"))
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PROGRESS_BOOK"))
    private Book book;

    @Column(name = "READ_PAGES", nullable = false)
    private Integer readPages;

    @Column(name = "CURRENT_PAGE", nullable = false)
    private Integer currentPage;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "STATUS", nullable = false)
    private String status;
}