package com.example.radioapp.service;

import com.example.radioapp.domain.RadioEpisode;
import com.example.radioapp.repository.RadioEpisodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * ラジオエピソードデータ関連の操作
 */
@Service
public class RadioEpisodeService {
    private final RadioEpisodeRepository radioEpisodeRepository;

    public RadioEpisodeService (RadioEpisodeRepository radioEpisodeRepository) {
        this.radioEpisodeRepository = radioEpisodeRepository;
    }

    /**
     * 番組IDと放送日からデータ取得
     * @param programId 番組ID
     * @param broadcastDate 放送日
     * @return エピソードデータ
     */
    public RadioEpisode findByProgramIdAndBroadcastDate(Long programId, LocalDate broadcastDate) {
        return radioEpisodeRepository.findByProgramIdAndBroadcastDate(programId, broadcastDate)
                .orElseThrow(() -> new IllegalArgumentException("エピソードが見つかりません"));
    }

    /**
     * エピソードを保存
     * @param radioEpisode エピソードデータ
     */
    public void save(RadioEpisode radioEpisode) {
        radioEpisodeRepository.save(radioEpisode);
    }
}