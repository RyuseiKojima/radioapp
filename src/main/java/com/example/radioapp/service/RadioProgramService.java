package com.example.radioapp.service;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.repository.RadioProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RadioProgramService {

    private final RadioProgramRepository repository;

    public RadioProgramService(RadioProgramRepository repository) {
        this.repository = repository;
    }

    public List<RadioProgram> findAll() {
        return repository.findAll();
    }

    public RadioProgram findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定されたIDが存在しません：" + id));
    }

    public void save(RadioProgram program) {
        repository.save(program);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}