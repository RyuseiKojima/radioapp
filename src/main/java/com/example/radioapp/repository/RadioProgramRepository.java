package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioProgram;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ラジオ番組データ処理
 */
public interface RadioProgramRepository extends JpaRepository<RadioProgram, Long> {
}
