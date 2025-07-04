package com.example.radioapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * ユーザテーブル
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser implements UserDetails {
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * ユーザ名
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * パスワード
     */
    @Column(nullable = false)
    private String password;

    /**
     * ロール
     * デフォルトはUSER
     */
    private String role = "USER";

    /**
     * フォロー番組
     */
    @ManyToMany
    @JoinTable(
            name = "user_follow_program",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "program_id")
    )
    private Set<RadioProgram> followedPrograms = new HashSet<>();

    /**
     * ユーザーのロール情報を GrantedAuthority として返す
     * @return ロール情報
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role);
    }

    /**
     * 双方向関連で無限ループを避けるためにidのみでhashCodeを計算
     * IDがnullの場合は、異なるインスタンスは等しくないと見なす
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        // IDがnullの場合は等しくない（まだ永続化されていないエンティティ）
        return id != null && Objects.equals(id, appUser.id);
    }

    /**
     * 双方向関連で無限ループを避けるためにidのみでhashCodeを計算
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

