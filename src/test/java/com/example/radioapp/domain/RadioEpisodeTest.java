package com.example.radioapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RadioEpisodeエンティティのテストクラス
 */
class RadioEpisodeTest {

    private RadioEpisode radioEpisode;
    private RadioProgram radioProgram;
    private RadioStation radioStation;

    @BeforeEach
    void setUp() {
        radioEpisode = new RadioEpisode();
        
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
    }

    @Test
    @DisplayName("デフォルトコンストラクタでオブジェクトが正しく初期化される")
    void testDefaultConstructor() {
        RadioEpisode episode = new RadioEpisode();
        
        assertNull(episode.getId());
        assertNull(episode.getProgram());
        assertNull(episode.getBroadcastDate());
    }

    @Test
    @DisplayName("getterとsetterが正しく動作する")
    void testGettersAndSetters() {
        Long id = 1L;
        LocalDate broadcastDate = LocalDate.of(2025, 7, 1);

        radioEpisode.setId(id);
        radioEpisode.setProgram(radioProgram);
        radioEpisode.setBroadcastDate(broadcastDate);

        assertEquals(id, radioEpisode.getId());
        assertEquals(radioProgram, radioEpisode.getProgram());
        assertEquals(broadcastDate, radioEpisode.getBroadcastDate());
    }

    @Test
    @DisplayName("RadioProgramとの関連が正しく設定される")
    void testProgramAssociation() {
        radioEpisode.setProgram(radioProgram);
        
        assertEquals(radioProgram, radioEpisode.getProgram());
        assertEquals("テスト番組", radioEpisode.getProgram().getTitle());
        assertEquals("テストホスト", radioEpisode.getProgram().getHost());
    }

    @Test
    @DisplayName("放送日が正しく設定される")
    void testBroadcastDate() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);
        
        radioEpisode.setBroadcastDate(today);
        assertEquals(today, radioEpisode.getBroadcastDate());
        
        radioEpisode.setBroadcastDate(yesterday);
        assertEquals(yesterday, radioEpisode.getBroadcastDate());
        
        radioEpisode.setBroadcastDate(tomorrow);
        assertEquals(tomorrow, radioEpisode.getBroadcastDate());
    }

    @Test
    @DisplayName("過去の放送日が設定できる")
    void testPastBroadcastDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        
        radioEpisode.setBroadcastDate(pastDate);
        
        assertEquals(pastDate, radioEpisode.getBroadcastDate());
        assertTrue(radioEpisode.getBroadcastDate().isBefore(LocalDate.now()));
    }

    @Test
    @DisplayName("未来の放送日が設定できる")
    void testFutureBroadcastDate() {
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        
        radioEpisode.setBroadcastDate(futureDate);
        
        assertEquals(futureDate, radioEpisode.getBroadcastDate());
        assertTrue(radioEpisode.getBroadcastDate().isAfter(LocalDate.now()));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じオブジェクト")
    void testEqualsWithSameObject() {
        assertTrue(radioEpisode.equals(radioEpisode));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - nullとの比較")
    void testEqualsWithNull() {
        assertFalse(radioEpisode.equals(null));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるクラスとの比較")
    void testEqualsWithDifferentClass() {
        assertFalse(radioEpisode.equals("string"));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じ値の場合")
    void testEqualsWithSameValues() {
        LocalDate broadcastDate = LocalDate.of(2025, 7, 1);
        
        RadioEpisode episode1 = new RadioEpisode();
        episode1.setId(1L);
        episode1.setProgram(radioProgram);
        episode1.setBroadcastDate(broadcastDate);
        
        RadioEpisode episode2 = new RadioEpisode();
        episode2.setId(1L);
        episode2.setProgram(radioProgram);
        episode2.setBroadcastDate(broadcastDate);
        
        assertTrue(episode1.equals(episode2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるIDの場合")
    void testEqualsWithDifferentId() {
        LocalDate broadcastDate = LocalDate.of(2025, 7, 1);
        
        RadioEpisode episode1 = new RadioEpisode();
        episode1.setId(1L);
        episode1.setProgram(radioProgram);
        episode1.setBroadcastDate(broadcastDate);
        
        RadioEpisode episode2 = new RadioEpisode();
        episode2.setId(2L);
        episode2.setProgram(radioProgram);
        episode2.setBroadcastDate(broadcastDate);
        
        assertFalse(episode1.equals(episode2));
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - 同じ値なら同じハッシュコード")
    void testHashCodeWithSameValues() {
        LocalDate broadcastDate = LocalDate.of(2025, 7, 1);
        
        RadioEpisode episode1 = new RadioEpisode();
        episode1.setId(1L);
        episode1.setProgram(radioProgram);
        episode1.setBroadcastDate(broadcastDate);
        
        RadioEpisode episode2 = new RadioEpisode();
        episode2.setId(1L);
        episode2.setProgram(radioProgram);
        episode2.setBroadcastDate(broadcastDate);
        
        assertEquals(episode1.hashCode(), episode2.hashCode());
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - nullの値でも例外が発生しない")
    void testHashCodeWithNullValues() {
        RadioEpisode episode = new RadioEpisode();
        
        assertDoesNotThrow(() -> episode.hashCode());
    }

    @Test
    @DisplayName("toString()メソッドが正しく動作する")
    void testToString() {
        LocalDate broadcastDate = LocalDate.of(2025, 7, 1);
        
        radioEpisode.setId(1L);
        radioEpisode.setProgram(radioProgram);
        radioEpisode.setBroadcastDate(broadcastDate);
        
        String toString = radioEpisode.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("RadioEpisode"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("broadcastDate=2025-07-01"));
    }

    @Test
    @DisplayName("equals()とhashCode()の一貫性")
    void testEqualsAndHashCodeConsistency() {
        LocalDate broadcastDate = LocalDate.of(2025, 7, 1);
        
        RadioEpisode episode1 = new RadioEpisode();
        episode1.setId(1L);
        episode1.setProgram(radioProgram);
        episode1.setBroadcastDate(broadcastDate);
        
        RadioEpisode episode2 = new RadioEpisode();
        episode2.setId(1L);
        episode2.setProgram(radioProgram);
        episode2.setBroadcastDate(broadcastDate);
        
        // equals()がtrueならhashCode()も同じ値を返すべき
        if (episode1.equals(episode2)) {
            assertEquals(episode1.hashCode(), episode2.hashCode());
        }
    }

    @Test
    @DisplayName("番組とエピソードの関連が正しく機能する")
    void testProgramEpisodeRelationship() {
        radioEpisode.setProgram(radioProgram);
        
        assertNotNull(radioEpisode.getProgram());
        assertEquals(radioProgram.getTitle(), radioEpisode.getProgram().getTitle());
        assertEquals(radioProgram.getHost(), radioEpisode.getProgram().getHost());
        assertEquals(radioProgram.getStation(), radioEpisode.getProgram().getStation());
    }
}
