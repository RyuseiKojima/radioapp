package com.example.radioapp.service;

import com.example.radioapp.domain.RadioStation;
import com.example.radioapp.repository.RadioStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RadioStationService {

    private final RadioStationRepository repository;

    public RadioStationService(RadioStationRepository repository) {
        this.repository = repository;
    }

    public List<RadioStation> findAll() {
        return repository.findAll();
    }
}