package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RadioEpisodeRepository extends JpaRepository<RadioEpisode, Long> {
    Optional<RadioEpisode> findByProgramIdAndBroadcastDate(Long programId, LocalDate broadcastDate);
}