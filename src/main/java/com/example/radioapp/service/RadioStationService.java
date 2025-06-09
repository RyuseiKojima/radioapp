package com.example.radioapp.service;

import com.example.radioapp.domain.RadioStation;
import com.example.radioapp.repository.RadioStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ラジオ放送局データ業務処理
 */
@Service
public class RadioStationService {

    private final RadioStationRepository repository;

    public RadioStationService(RadioStationRepository repository) {
        this.repository = repository;
    }

    /**
     * 全権取得
     *
     * @return ラジオ放送局一覧
     */
    public List<RadioStation> findAll() {
        return repository.findAll();
    }
}