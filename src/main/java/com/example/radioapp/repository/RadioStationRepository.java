package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioStation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ラジオ放送局データ処理
 */
public interface RadioStationRepository extends JpaRepository<RadioStation, Long> {
}
