package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioProgram;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RadioProgramRepository extends JpaRepository<RadioProgram, Long> {
}
