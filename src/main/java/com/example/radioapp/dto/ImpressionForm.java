package com.example.radioapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ImpressionForm {
    @NotNull
    private Long programId;

    @NotNull
    private LocalDate broadcastDate;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}