package com.example.readerai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "PARTICIPANT")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Participant extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTICIPANT_SEQ_ID")
    @SequenceGenerator(name = "PARTICIPANT_SEQ_ID", sequenceName = "PARTICIPANT_SEQ_ID", allocationSize = 1)
    private Long id;

    @Column(name = "SUP_ID")
    private String supId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "USER_ID", nullable = false)
    private String userId;
}