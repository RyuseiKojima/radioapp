package com.example.radioapp.repository;

import com.example.radioapp.domain.Impression;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 感想データ処理
 */
public interface ImpressionRepository extends JpaRepository<Impression, Long> {
}
