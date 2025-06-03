package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadioStationRepository extends JpaRepository<RadioStation, Long> {
}
