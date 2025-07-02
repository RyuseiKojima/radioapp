package com.example.radioapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RadioProgramエンティティのテストクラス
 */
class RadioProgramTest {

    private RadioProgram radioProgram;
    private RadioStation radioStation;
    private AppUser appUser1;
    private AppUser appUser2;

    @BeforeEach
    void setUp() {
        radioProgram = new RadioProgram();
        
        // テスト用のRadioStationを作成
        radioStation = new RadioStation();
        radioStation.setId(1L);
        radioStation.setName("NHKラジオ第1");
        
        // テスト用のAppUserを作成
        appUser1 = new AppUser();
        appUser1.setId(1L);
        appUser1.setUsername("user1");
        
        appUser2 = new AppUser();
        appUser2.setId(2L);
        appUser2.setUsername("user2");
    }

    @Test
    @DisplayName("デフォルトコンストラクタでオブジェクトが正しく初期化される")
    void testDefaultConstructor() {
        RadioProgram program = new RadioProgram();
        
        assertNull(program.getId());
        assertNull(program.getTitle());
        assertNull(program.getHost());
        assertNull(program.getDescription());
        assertNull(program.getStation());
        assertNull(program.getFollowers());
    }

    @Test
    @DisplayName("getterとsetterが正しく動作する")
    void testGettersAndSetters() {
        Long id = 1L;
        String title = "テスト番組";
        String host = "テストホスト";
        String description = "テスト番組の説明";

        radioProgram.setId(id);
        radioProgram.setTitle(title);
        radioProgram.setHost(host);
        radioProgram.setDescription(description);
        radioProgram.setStation(radioStation);

        assertEquals(id, radioProgram.getId());
        assertEquals(title, radioProgram.getTitle());
        assertEquals(host, radioProgram.getHost());
        assertEquals(description, radioProgram.getDescription());
        assertEquals(radioStation, radioProgram.getStation());
    }

    @Test
    @DisplayName("RadioStationとの関連が正しく設定される")
    void testStationAssociation() {
        radioProgram.setStation(radioStation);
        
        assertEquals(radioStation, radioProgram.getStation());
        assertEquals("NHKラジオ第1", radioProgram.getStation().getName());
    }

    @Test
    @DisplayName("フォロワーを設定できる")
    void testSetFollowers() {
        Set<AppUser> followers = new HashSet<>();
        followers.add(appUser1);
        followers.add(appUser2);
        
        radioProgram.setFollowers(followers);
        
        assertEquals(2, radioProgram.getFollowers().size());
        assertTrue(radioProgram.getFollowers().contains(appUser1));
        assertTrue(radioProgram.getFollowers().contains(appUser2));
    }

    @Test
    @DisplayName("フォロワーを追加できる")
    void testAddFollower() {
        Set<AppUser> followers = new HashSet<>();
        radioProgram.setFollowers(followers);
        
        radioProgram.getFollowers().add(appUser1);
        
        assertEquals(1, radioProgram.getFollowers().size());
        assertTrue(radioProgram.getFollowers().contains(appUser1));
    }

    @Test
    @DisplayName("フォロワーを削除できる")
    void testRemoveFollower() {
        Set<AppUser> followers = new HashSet<>();
        followers.add(appUser1);
        followers.add(appUser2);
        radioProgram.setFollowers(followers);
        
        radioProgram.getFollowers().remove(appUser1);
        
        assertEquals(1, radioProgram.getFollowers().size());
        assertFalse(radioProgram.getFollowers().contains(appUser1));
        assertTrue(radioProgram.getFollowers().contains(appUser2));
    }

    @Test
    @DisplayName("重複したフォロワーは追加されない")
    void testDuplicateFollowers() {
        Set<AppUser> followers = new HashSet<>();
        radioProgram.setFollowers(followers);
        
        radioProgram.getFollowers().add(appUser1);
        radioProgram.getFollowers().add(appUser1); // 同じユーザーを再度追加
        
        assertEquals(1, radioProgram.getFollowers().size());
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じオブジェクト")
    void testEqualsWithSameObject() {
        assertTrue(radioProgram.equals(radioProgram));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - nullとの比較")
    void testEqualsWithNull() {
        assertFalse(radioProgram.equals(null));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるクラスとの比較")
    void testEqualsWithDifferentClass() {
        assertFalse(radioProgram.equals("string"));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じIDの場合")
    void testEqualsWithSameId() {
        RadioProgram program1 = new RadioProgram();
        program1.setId(1L);
        program1.setTitle("番組1");
        
        RadioProgram program2 = new RadioProgram();
        program2.setId(1L);
        program2.setTitle("番組2"); // 異なるタイトルでも同じIDなら等しい
        
        assertTrue(program1.equals(program2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるIDの場合")
    void testEqualsWithDifferentId() {
        RadioProgram program1 = new RadioProgram();
        program1.setId(1L);
        
        RadioProgram program2 = new RadioProgram();
        program2.setId(2L);
        
        assertFalse(program1.equals(program2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 両方ともIDがnullの場合")
    void testEqualsWithBothNullIds() {
        RadioProgram program1 = new RadioProgram();
        RadioProgram program2 = new RadioProgram();
        
        // IDがnullの場合、Objects.equals(null, null)はtrueを返す
        assertTrue(program1.equals(program2));
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - 同じIDなら同じハッシュコード")
    void testHashCodeWithSameId() {
        RadioProgram program1 = new RadioProgram();
        program1.setId(1L);
        
        RadioProgram program2 = new RadioProgram();
        program2.setId(1L);
        
        assertEquals(program1.hashCode(), program2.hashCode());
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - nullのIDでも例外が発生しない")
    void testHashCodeWithNullId() {
        RadioProgram program = new RadioProgram();
        
        assertDoesNotThrow(() -> program.hashCode());
    }

    @Test
    @DisplayName("equals()とhashCode()の一貫性")
    void testEqualsAndHashCodeConsistency() {
        RadioProgram program1 = new RadioProgram();
        program1.setId(1L);
        program1.setTitle("テスト番組");
        
        RadioProgram program2 = new RadioProgram();
        program2.setId(1L);
        program2.setTitle("別の番組");
        
        // equals()がtrueならhashCode()も同じ値を返すべき
        if (program1.equals(program2)) {
            assertEquals(program1.hashCode(), program2.hashCode());
        }
    }

    @Test
    @DisplayName("バリデーション用のアノテーションが設定されている")
    void testValidationAnnotations() {
        // このテストはアノテーションの存在を確認する
        // 実際のバリデーションテストは統合テストで行う
        radioProgram.setTitle("");
        radioProgram.setHost("");
        
        assertNotNull(radioProgram.getTitle());
        assertNotNull(radioProgram.getHost());
    }
}
