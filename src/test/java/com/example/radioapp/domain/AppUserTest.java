package com.example.radioapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AppUserエンティティのテストクラス
 */
class AppUserTest {

    private AppUser appUser;
    private RadioProgram radioProgram1;
    private RadioProgram radioProgram2;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        
        // テスト用のRadioProgramを作成
        radioProgram1 = new RadioProgram();
        radioProgram1.setId(1L);
        radioProgram1.setTitle("テスト番組1");
        radioProgram1.setHost("テストホスト1");
        
        radioProgram2 = new RadioProgram();
        radioProgram2.setId(2L);
        radioProgram2.setTitle("テスト番組2");
        radioProgram2.setHost("テストホスト2");
    }

    @Test
    @DisplayName("デフォルトコンストラクタでオブジェクトが正しく初期化される")
    void testDefaultConstructor() {
        AppUser user = new AppUser();
        
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertEquals("USER", user.getRole());
        assertNotNull(user.getFollowedPrograms());
        assertTrue(user.getFollowedPrograms().isEmpty());
    }

    @Test
    @DisplayName("getterとsetterが正しく動作する")
    void testGettersAndSetters() {
        Long id = 1L;
        String username = "testuser";
        String password = "testpassword";
        String role = "ADMIN";

        appUser.setId(id);
        appUser.setUsername(username);
        appUser.setPassword(password);
        appUser.setRole(role);

        assertEquals(id, appUser.getId());
        assertEquals(username, appUser.getUsername());
        assertEquals(password, appUser.getPassword());
        assertEquals(role, appUser.getRole());
    }

    @Test
    @DisplayName("デフォルトロールがUSERである")
    void testDefaultRole() {
        AppUser user = new AppUser();
        assertEquals("USER", user.getRole());
    }

    @Test
    @DisplayName("getAuthorities()でUSERロールが正しく返される")
    void testGetAuthoritiesWithUserRole() {
        appUser.setRole("USER");
        
        Collection<? extends GrantedAuthority> authorities = appUser.getAuthorities();
        
        assertEquals(1, authorities.size());
        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    @DisplayName("getAuthorities()でADMINロールが正しく返される")
    void testGetAuthoritiesWithAdminRole() {
        appUser.setRole("ADMIN");
        
        Collection<? extends GrantedAuthority> authorities = appUser.getAuthorities();
        
        assertEquals(1, authorities.size());
        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }

    @Test
    @DisplayName("UserDetailsインターフェースのデフォルトメソッドが正しく動作する")
    void testUserDetailsDefaultMethods() {
        appUser.setUsername("testuser");
        appUser.setPassword("testpassword");

        assertEquals("testuser", appUser.getUsername());
        assertEquals("testpassword", appUser.getPassword());
        
        // UserDetailsのデフォルト実装をテスト
        assertTrue(appUser.isAccountNonExpired());
        assertTrue(appUser.isAccountNonLocked());
        assertTrue(appUser.isCredentialsNonExpired());
        assertTrue(appUser.isEnabled());
    }

    @Test
    @DisplayName("フォロー番組を追加できる")
    void testAddFollowedProgram() {
        appUser.getFollowedPrograms().add(radioProgram1);
        
        assertEquals(1, appUser.getFollowedPrograms().size());
        assertTrue(appUser.getFollowedPrograms().contains(radioProgram1));
    }

    @Test
    @DisplayName("複数のフォロー番組を管理できる")
    void testMultipleFollowedPrograms() {
        appUser.getFollowedPrograms().add(radioProgram1);
        appUser.getFollowedPrograms().add(radioProgram2);
        
        assertEquals(2, appUser.getFollowedPrograms().size());
        assertTrue(appUser.getFollowedPrograms().contains(radioProgram1));
        assertTrue(appUser.getFollowedPrograms().contains(radioProgram2));
    }

    @Test
    @DisplayName("フォロー番組を削除できる")
    void testRemoveFollowedProgram() {
        appUser.getFollowedPrograms().add(radioProgram1);
        appUser.getFollowedPrograms().add(radioProgram2);
        
        appUser.getFollowedPrograms().remove(radioProgram1);
        
        assertEquals(1, appUser.getFollowedPrograms().size());
        assertFalse(appUser.getFollowedPrograms().contains(radioProgram1));
        assertTrue(appUser.getFollowedPrograms().contains(radioProgram2));
    }

    @Test
    @DisplayName("重複したフォロー番組は追加されない")
    void testDuplicateFollowedPrograms() {
        appUser.getFollowedPrograms().add(radioProgram1);
        appUser.getFollowedPrograms().add(radioProgram1); // 同じ番組を再度追加
        
        assertEquals(1, appUser.getFollowedPrograms().size());
    }

    @Test
    @DisplayName("フォロー番組のSetを直接設定できる")
    void testSetFollowedPrograms() {
        HashSet<RadioProgram> programs = new HashSet<>();
        programs.add(radioProgram1);
        programs.add(radioProgram2);
        
        appUser.setFollowedPrograms(programs);
        
        assertEquals(2, appUser.getFollowedPrograms().size());
        assertTrue(appUser.getFollowedPrograms().contains(radioProgram1));
        assertTrue(appUser.getFollowedPrograms().contains(radioProgram2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じオブジェクト")
    void testEqualsWithSameObject() {
        assertTrue(appUser.equals(appUser));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - nullとの比較")
    void testEqualsWithNull() {
        assertFalse(appUser.equals(null));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるクラスとの比較")
    void testEqualsWithDifferentClass() {
        assertFalse(appUser.equals("string"));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じIDの場合")
    void testEqualsWithSameId() {
        AppUser user1 = new AppUser();
        user1.setId(1L);
        user1.setUsername("user1");
        
        AppUser user2 = new AppUser();
        user2.setId(1L);
        user2.setUsername("user2"); // 異なるusernameでも同じIDなら等しい
        
        assertTrue(user1.equals(user2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるIDの場合")
    void testEqualsWithDifferentId() {
        AppUser user1 = new AppUser();
        user1.setId(1L);
        
        AppUser user2 = new AppUser();
        user2.setId(2L);
        
        assertFalse(user1.equals(user2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 両方ともIDがnullの場合")
    void testEqualsWithBothNullIds() {
        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        
        assertFalse(user1.equals(user2));
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - 同じIDなら同じハッシュコード")
    void testHashCodeWithSameId() {
        AppUser user1 = new AppUser();
        user1.setId(1L);
        
        AppUser user2 = new AppUser();
        user2.setId(1L);
        
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - nullのIDでも例外が発生しない")
    void testHashCodeWithNullId() {
        AppUser user = new AppUser();
        
        assertDoesNotThrow(() -> user.hashCode());
    }

    @Test
    @DisplayName("equals()とhashCode()の一貫性")
    void testEqualsAndHashCodeConsistency() {
        AppUser user1 = new AppUser();
        user1.setId(1L);
        user1.setUsername("testuser");
        
        AppUser user2 = new AppUser();
        user2.setId(1L);
        user2.setUsername("anotheruser");
        
        // equals()がtrueならhashCode()も同じ値を返すべき
        if (user1.equals(user2)) {
            assertEquals(user1.hashCode(), user2.hashCode());
        }
    }
}
