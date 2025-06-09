package com.example.radioapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

/**
 * エピソードテーブル
 */
@Entity
@Data
public class RadioEpisode {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 番組データ
     */
    @ManyToOne(optional = false)
    private RadioProgram program;

    /**
     * 放送日
     */
    @Column(nullable = false)
    private LocalDate broadcastDate;
}