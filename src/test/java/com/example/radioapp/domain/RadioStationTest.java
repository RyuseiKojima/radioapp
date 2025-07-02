package com.example.radioapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RadioStationエンティティのテストクラス
 */
class RadioStationTest {

    private RadioStation radioStation;

    @BeforeEach
    void setUp() {
        radioStation = new RadioStation();
    }

    @Test
    @DisplayName("デフォルトコンストラクタでオブジェクトが正しく初期化される")
    void testDefaultConstructor() {
        RadioStation station = new RadioStation();
        
        assertNull(station.getId());
        assertNull(station.getName());
    }

    @Test
    @DisplayName("getterとsetterが正しく動作する")
    void testGettersAndSetters() {
        Long id = 1L;
        String name = "NHKラジオ第1";

        radioStation.setId(id);
        radioStation.setName(name);

        assertEquals(id, radioStation.getId());
        assertEquals(name, radioStation.getName());
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じオブジェクト")
    void testEqualsWithSameObject() {
        assertTrue(radioStation.equals(radioStation));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - nullとの比較")
    void testEqualsWithNull() {
        assertFalse(radioStation.equals(null));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるクラスとの比較")
    void testEqualsWithDifferentClass() {
        assertFalse(radioStation.equals("string"));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 同じ値の場合")
    void testEqualsWithSameValues() {
        RadioStation station1 = new RadioStation();
        station1.setId(1L);
        station1.setName("NHKラジオ第1");
        
        RadioStation station2 = new RadioStation();
        station2.setId(1L);
        station2.setName("NHKラジオ第1");
        
        assertTrue(station1.equals(station2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なるIDの場合")
    void testEqualsWithDifferentId() {
        RadioStation station1 = new RadioStation();
        station1.setId(1L);
        station1.setName("NHKラジオ第1");
        
        RadioStation station2 = new RadioStation();
        station2.setId(2L);
        station2.setName("NHKラジオ第1");
        
        assertFalse(station1.equals(station2));
    }

    @Test
    @DisplayName("equals()メソッドが正しく動作する - 異なる名前の場合")
    void testEqualsWithDifferentName() {
        RadioStation station1 = new RadioStation();
        station1.setId(1L);
        station1.setName("NHKラジオ第1");
        
        RadioStation station2 = new RadioStation();
        station2.setId(1L);
        station2.setName("NHKラジオ第2");
        
        assertFalse(station1.equals(station2));
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - 同じ値なら同じハッシュコード")
    void testHashCodeWithSameValues() {
        RadioStation station1 = new RadioStation();
        station1.setId(1L);
        station1.setName("NHKラジオ第1");
        
        RadioStation station2 = new RadioStation();
        station2.setId(1L);
        station2.setName("NHKラジオ第1");
        
        assertEquals(station1.hashCode(), station2.hashCode());
    }

    @Test
    @DisplayName("hashCode()メソッドが正しく動作する - nullの値でも例外が発生しない")
    void testHashCodeWithNullValues() {
        RadioStation station = new RadioStation();
        
        assertDoesNotThrow(() -> station.hashCode());
    }

    @Test
    @DisplayName("toString()メソッドが正しく動作する")
    void testToString() {
        radioStation.setId(1L);
        radioStation.setName("NHKラジオ第1");
        
        String toString = radioStation.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("RadioStation"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=NHKラジオ第1"));
    }

    @Test
    @DisplayName("equals()とhashCode()の一貫性")
    void testEqualsAndHashCodeConsistency() {
        RadioStation station1 = new RadioStation();
        station1.setId(1L);
        station1.setName("NHKラジオ第1");
        
        RadioStation station2 = new RadioStation();
        station2.setId(1L);
        station2.setName("NHKラジオ第1");
        
        // equals()がtrueならhashCode()も同じ値を返すべき
        if (station1.equals(station2)) {
            assertEquals(station1.hashCode(), station2.hashCode());
        }
    }
}
