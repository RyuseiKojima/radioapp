package com.example.radioapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 感想フォーム
 */
@Data
public class ImpressionForm {
    /**
     * 番組id(hidden)
     */
    @NotNull
    private Long programId;

    /**
     * 放送日
     */
    @NotNull(message = "日付を選択してください")
    private LocalDate broadcastDate;

    /**
     * タイトル
     */
    @NotBlank(message = "タイトル入力は必須です")
    private String title;

    /**
     * 感想
     */
    @NotBlank(message = "感想入力は必須です")
    private String content;
}