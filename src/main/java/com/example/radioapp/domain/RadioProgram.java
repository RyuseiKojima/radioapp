package com.example.radioapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @ManyToOne
    @NotNull(message = "放送局を選択してください")
    @JoinColumn(name = "station_id")  // 外部キー列名
    private RadioStation station;

    @ManyToMany(mappedBy = "followedPrograms")
    private Set<AppUser> followers;
}
