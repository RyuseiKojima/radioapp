package com.example.radioapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/**
 * 感想テーブル
 */
@Entity
@Data
public class Impression {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザデータ
     */
    @ManyToOne(optional = false)
    private AppUser user;

    /**
     * エピソードデータ
     */
    @ManyToOne(optional = false)
    private RadioEpisode episode;

    /**
     * タイトル
     */
    private String title;

    /**
     * 感想
     */
    @Column(length = 2000)
    private String content;
}
