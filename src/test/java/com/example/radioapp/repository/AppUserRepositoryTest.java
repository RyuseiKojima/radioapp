package com.example.radioapp.repository;

import com.example.radioapp.domain.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AppUserRepositoryの統合テストクラス
 */
@DataJpaTest
@ActiveProfiles("test")
class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser testUser1;
    private AppUser testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = new AppUser();
        testUser1.setUsername("testuser1");
        testUser1.setPassword("password1");
        testUser1.setRole("USER");

        testUser2 = new AppUser();
        testUser2.setUsername("testuser2");
        testUser2.setPassword("password2");
        testUser2.setRole("ADMIN");
    }

    @Test
    @DisplayName("ユーザーを保存できる")
    void testSaveUser() {
        AppUser savedUser = appUserRepository.save(testUser1);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("testuser1", savedUser.getUsername());
        assertEquals("password1", savedUser.getPassword());
        assertEquals("USER", savedUser.getRole());
    }

    @Test
    @DisplayName("IDでユーザーを検索できる")
    void testFindById() {
        AppUser savedUser = entityManager.persistAndFlush(testUser1);

        Optional<AppUser> foundUser = appUserRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals("testuser1", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("存在しないIDで検索した場合は空のOptionalを返す")
    void testFindByIdNotFound() {
        Optional<AppUser> foundUser = appUserRepository.findById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("ユーザー名でユーザーを検索できる")
    void testFindByUsername() {
        entityManager.persistAndFlush(testUser1);

        Optional<AppUser> foundUser = appUserRepository.findByUsername("testuser1");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser1", foundUser.get().getUsername());
        assertEquals("password1", foundUser.get().getPassword());
        assertEquals("USER", foundUser.get().getRole());
    }

    @Test
    @DisplayName("存在しないユーザー名で検索した場合は空のOptionalを返す")
    void testFindByUsernameNotFound() {
        Optional<AppUser> foundUser = appUserRepository.findByUsername("nonexistent");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("複数のユーザーが存在する場合でも正しいユーザーを検索できる")
    void testFindByUsernameWithMultipleUsers() {
        entityManager.persistAndFlush(testUser1);
        entityManager.persistAndFlush(testUser2);

        Optional<AppUser> foundUser1 = appUserRepository.findByUsername("testuser1");
        Optional<AppUser> foundUser2 = appUserRepository.findByUsername("testuser2");

        assertTrue(foundUser1.isPresent());
        assertTrue(foundUser2.isPresent());
        assertEquals("testuser1", foundUser1.get().getUsername());
        assertEquals("testuser2", foundUser2.get().getUsername());
        assertEquals("USER", foundUser1.get().getRole());
        assertEquals("ADMIN", foundUser2.get().getRole());
    }

    @Test
    @DisplayName("全てのユーザーを取得できる")
    void testFindAll() {
        entityManager.persistAndFlush(testUser1);
        entityManager.persistAndFlush(testUser2);

        List<AppUser> users = appUserRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("ユーザーを更新できる")
    void testUpdateUser() {
        AppUser savedUser = entityManager.persistAndFlush(testUser1);

        savedUser.setPassword("newpassword");
        savedUser.setRole("ADMIN");
        AppUser updatedUser = appUserRepository.save(savedUser);

        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals("testuser1", updatedUser.getUsername());
        assertEquals("newpassword", updatedUser.getPassword());
        assertEquals("ADMIN", updatedUser.getRole());
    }

    @Test
    @DisplayName("ユーザーを削除できる")
    void testDeleteUser() {
        AppUser savedUser = entityManager.persistAndFlush(testUser1);
        Long userId = savedUser.getId();

        appUserRepository.delete(savedUser);

        Optional<AppUser> foundUser = appUserRepository.findById(userId);
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("ユーザーが存在するかチェックできる")
    void testExistsById() {
        AppUser savedUser = entityManager.persistAndFlush(testUser1);

        boolean exists = appUserRepository.existsById(savedUser.getId());
        boolean notExists = appUserRepository.existsById(999L);

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("ユーザー数をカウントできる")
    void testCount() {
        entityManager.persistAndFlush(testUser1);
        entityManager.persistAndFlush(testUser2);

        long count = appUserRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("ユーザー名のユニーク制約をテストする")
    void testUsernameUniqueConstraint() {
        entityManager.persistAndFlush(testUser1);

        AppUser duplicateUser = new AppUser();
        duplicateUser.setUsername("testuser1"); // 同じユーザー名
        duplicateUser.setPassword("differentpassword");

        // ユニーク制約違反によりexceptionが発生することを確認
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(duplicateUser);
        });
    }
}
