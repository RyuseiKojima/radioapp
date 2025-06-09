package com.example.radioapp.repository;

import com.example.radioapp.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ユーザデータ処理
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    /**
     * ユーザ名からデータ取得
     * @param username ユーザ名
     * @return ユーザデータ
     */
    Optional<AppUser> findByUsername(String username);
}