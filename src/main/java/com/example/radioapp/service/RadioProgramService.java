package com.example.radioapp.service;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.repository.RadioProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ラジオ番組データ業務処理
 */
@Service
public class RadioProgramService {

    private final RadioProgramRepository radioProgramRepository;

    public RadioProgramService(RadioProgramRepository radioProgramRepository) {
        this.radioProgramRepository = radioProgramRepository;
    }

    /**
     * 全権取得
     * @return ラジオ番組一覧
     */
    public List<RadioProgram> findAll() {
        return radioProgramRepository.findAll();
    }

    /**
     * 1件取得
     * @param id ラジオ番組id
     * @return ラジオ番組
     */
    public RadioProgram findById(Long id) {
        return radioProgramRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("番組が見つかりません：" + id));
    }

    /**
     * 保存
     * @param program ラジオ番組データ
     */
    public void save(RadioProgram program) {
        radioProgramRepository.save(program);
    }

    /**
     * 削除
     * @param id ラジオ番組id
     */
    public void delete(Long id) {
        radioProgramRepository.deleteById(id);
    }
}