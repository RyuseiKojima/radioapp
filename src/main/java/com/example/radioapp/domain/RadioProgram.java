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
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

/**
 * 番組テーブル
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
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

    /**
     * 双方向関連で無限ループを避けるためにidのみでequalsを判定
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RadioProgram that = (RadioProgram) o;
        return Objects.equals(id, that.id);
    }

    /**
     * 双方向関連で無限ループを避けるためにidのみでhashCodeを計算
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
