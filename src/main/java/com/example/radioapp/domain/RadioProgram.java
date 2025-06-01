package com.example.radioapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // getter, setter, toString, equals, hashCodeを自動生成
@NoArgsConstructor // 引数なしコンストラクタを自動生成
public class RadioProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "タイトルは必須です")
    private String title;
    @NotBlank(message = "パーソナリティは必須です")
    private String host;
    private String description;
}
