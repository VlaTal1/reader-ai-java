package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "ACCESS")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Access extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCESS_SEQ_ID")
    @SequenceGenerator(name = "ACCESS_SEQ_ID", sequenceName = "ACCESS_SEQ_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTICIPANT_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ACCESS_PARTICIPANT"))
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ACCESS_BOOK"))
    private Book book;
}