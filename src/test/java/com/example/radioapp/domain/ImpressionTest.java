package com.example.radioapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Impressionエンティティのテストクラス
 */
class ImpressionTest {

    private Impression impression;
    private AppUser appUser;
    private RadioEpisode radioEpisode;
    private RadioProgram radioProgram;
    private RadioStation radioStation;

    @BeforeEach
    void setUp() {
        impression = new Impression();
        
        // テスト用のRadioStationを作成
        radioStation = new RadioStation();
        radioStation.setId(1L);
        radioStation.setName("NHKラジオ第1");
        
        // テスト用のRadioProgramを作成
        radioProgram = new RadioProgram();
        radioProgram.setId(1L);
        radioProgram.setTitle("テスト番組");
        radioProgram.setHost("テストホスト");
        radioProgram.setStation(radioStation);
        
        // テスト用のRadioEpisodeを作成
        radioEpisode = new RadioEpisode();
        radioEpisode.setId(1L);
        radioEpisode.setProgram(radioProgram);
        radioEpisode.setBroadcastDate(LocalDate.of(2025, 7, 1));
        
        // テスト用のAppUserを作成
        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("testuser");
        appUser.setPassword("testpassword");
    }

    @Test
    @DisplayName("デフォルトコンストラクタでオブジェクトが正しく初期化される")
    void testDefaultConstructor() {
        Impression imp = new Impression();
        
        assertNull(imp.getId());
        assertNull(imp.getUser());
        assertNull(imp.getEpisode());
        assertNull(imp.getTitle());
        assertNull(imp.getContent());
    }

    @Test
    @DisplayName("getterとsetterが正しく動作する")
    void testGettersAndSetters() {
        Long id = 1L;
        String title = "感想タイトル";
        String content = "とても面白い番組でした。";

        impression.setId(id);
        impression.setUser(appUser);
        impression.setEpisode(radioEpisode);
        impression.setTitle(title);
        impression.setContent(content);

        assertEquals(id, impression.getId());
        assertEquals(appUser, impression.getUser());
        assertEquals(radioEpisode, impression.getEpisode());
        assertEquals(title, impression.getTitle());
        assertEquals(content, impression.getContent());
    }

    @Test
    @DisplayName("AppUserとの関連が正しく設定される")
    void testUserAssociation() {
        impression.setUser(appUser);
        
        assertEquals(appUser, impression.getUser());
        assertEquals("testuser", impression.getUser().getUsername());
    }

    @Test
    @DisplayName("RadioEpisodeとの関連が正しく設定される")
    void testEpisodeAssociation() {
        impression.setEpisode(radioEpisode);
        
        assertEquals(radioEpisode, impression.getEpisode());
        assertEquals("テスト番組", impression.getEpisode().getProgram().getTitle());
        assertEquals(LocalDate.of(2025, 7, 1), impression.getEpisode().getBroadcastDate());
    }

    @Test
    @DisplayName("タイトルが正しく設定される")
    void testTitle() {
        String title = "素晴らしい番組でした";
        
        impression.setTitle(title);
        
        assertEquals(title, impression.getTitle());
    }

    @Test
    @DisplayName("感想内容が正しく設定される")
    void testContent() {
        String content = "今回の番組は特に興味深い内容でした。ホストの話し方も聞きやすく、非常に勉強になりました。";
        
        impression.setContent(content);
        
        assertEquals(content, impression.getContent());
    }

    @Test
    @DisplayName("長い感想内容が設定できる")
    void testLongContent() {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longContent.append("この番組はとても面白いです。");
        }
        String content = longContent.toString();
        
        impression.setContent(content);
        
        assertEquals(content, impression.getContent());
        assertTrue(impression.getContent().length() > 1000);
    }

    @Test
    @DisplayName("空のタイトルと内容が設定できる")
    void testEmptyTitleAndContent() {
        impression.setTitle("");
        impression.setContent("");
        
        assertEquals("", impression.getTitle());
        assertEquals("", impression.getContent());
    }

    @Test
    @DisplayName("nullのタイトルと内容が設定できる")
    void testNullTitleAndContent() {
        impression.setTitle(null);
        impression.setContent(null);
        
        assertNull(impression.getTitle());
        assertNull(impression.getContent());
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じオブジェクト")
    void testEqualsWithSameObject() {
        assertTrue(impression.equals(impression));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - nullとの比較")
    void testEqualsWithNull() {
        assertFalse(impression.equals(null));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるクラスとの比較")
    void testEqualsWithDifferentClass() {
        assertFalse(impression.equals("string"));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じ値の場合")
    void testEqualsWithSameValues() {
        Impression imp1 = new Impression();
        imp1.setId(1L);
        imp1.setUser(appUser);
        imp1.setEpisode(radioEpisode);
        imp1.setTitle("感想タイトル");
        imp1.setContent("感想内容");
        
        Impression imp2 = new Impression();
        imp2.setId(1L);
        imp2.setUser(appUser);
        imp2.setEpisode(radioEpisode);
        imp2.setTitle("感想タイトル");
        imp2.setContent("感想内容");
        
        assertTrue(imp1.equals(imp2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるIDの場合")
    void testEqualsWithDifferentId() {
        Impression imp1 = new Impression();
        imp1.setId(1L);
        imp1.setTitle("感想タイトル");
        
        Impression imp2 = new Impression();
        imp2.setId(2L);
        imp2.setTitle("感想タイトル");
        
        assertFalse(imp1.equals(imp2));
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - 同じ値なら同じハッシュコード")
    void testHashCodeWithSameValues() {
        Impression imp1 = new Impression();
        imp1.setId(1L);
        imp1.setTitle("感想タイトル");
        
        Impression imp2 = new Impression();
        imp2.setId(1L);
        imp2.setTitle("感想タイトル");
        
        assertEquals(imp1.hashCode(), imp2.hashCode());
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - nullの値でも例外が発生しない")
    void testHashCodeWithNullValues() {
        Impression imp = new Impression();
        
        assertDoesNotThrow(() -> imp.hashCode());
    }

    @Test
    @DisplayName("toString()メソッドが正しく動作する")
    void testToString() {
        impression.setId(1L);
        impression.setUser(appUser);
        impression.setEpisode(radioEpisode);
        impression.setTitle("感想タイトル");
        impression.setContent("感想内容");
        
        String toString = impression.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("Impression"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("title=感想タイトル"));
        assertTrue(toString.contains("content=感想内容"));
    }

    @Test
    @DisplayName("equals()とhashCode()の一貫性")
    void testEqualsAndHashCodeConsistency() {
        Impression imp1 = new Impression();
        imp1.setId(1L);
        imp1.setTitle("感想タイトル");
        
        Impression imp2 = new Impression();
        imp2.setId(1L);
        imp2.setTitle("感想タイトル");
        
        // equals()がtrueならhashCode()も同じ値を返すべき
        if (imp1.equals(imp2)) {
            assertEquals(imp1.hashCode(), imp2.hashCode());
        }
    }

    @Test
    @DisplayName("ユーザー、エピソード、感想の関連が正しく機能する")
    void testUserEpisodeImpressionRelationship() {
        impression.setUser(appUser);
        impression.setEpisode(radioEpisode);
        impression.setTitle("番組感想");
        impression.setContent("素晴らしい内容でした");
        
        assertNotNull(impression.getUser());
        assertNotNull(impression.getEpisode());
        assertEquals("testuser", impression.getUser().getUsername());
        assertEquals("テスト番組", impression.getEpisode().getProgram().getTitle());
        assertEquals("番組感想", impression.getTitle());
        assertEquals("素晴らしい内容でした", impression.getContent());
    }
}
