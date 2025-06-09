package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * エピソードデータ処理
 */
public interface RadioEpisodeRepository extends JpaRepository<RadioEpisode, Long> {
    /**
     * 番組idと放送日からデータ取得
     * @param programId 番組id
     * @param broadcastDate 放送日
     * @return エピソードデータ
     */
    Optional<RadioEpisode> findByProgramIdAndBroadcastDate(Long programId, LocalDate broadcastDate);
}