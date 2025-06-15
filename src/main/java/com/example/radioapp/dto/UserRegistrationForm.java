package com.example.radioapp.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 感想フォーム
 */
@Data
public class UserRegistrationForm {
    @NotBlank(message = "ユーザ名は入力必須です")
    private String username;

    @NotBlank(message = "パスワードは入力必須です")
    private String password;
}
