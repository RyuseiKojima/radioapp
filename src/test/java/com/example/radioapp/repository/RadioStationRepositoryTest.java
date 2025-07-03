package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioStation;
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
 * RadioStationRepositoryの統合テストクラス
 */
@DataJpaTest
@ActiveProfiles("test")
class RadioStationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RadioStationRepository radioStationRepository;

    private RadioStation testStation1;
    private RadioStation testStation2;

    @BeforeEach
    void setUp() {
        testStation1 = new RadioStation();
        testStation1.setName("NHKラジオ第1");

        testStation2 = new RadioStation();
        testStation2.setName("NHKラジオ第2");
    }

    @Test
    @DisplayName("放送局を保存できる")
    void testSaveStation() {
        RadioStation savedStation = radioStationRepository.save(testStation1);

        assertNotNull(savedStation);
        assertNotNull(savedStation.getId());
        assertEquals("NHKラジオ第1", savedStation.getName());
    }

    @Test
    @DisplayName("IDで放送局を検索できる")
    void testFindById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation1);

        Optional<RadioStation> foundStation = radioStationRepository.findById(savedStation.getId());

        assertTrue(foundStation.isPresent());
        assertEquals(savedStation.getId(), foundStation.get().getId());
        assertEquals("NHKラジオ第1", foundStation.get().getName());
    }

    @Test
    @DisplayName("存在しないIDで検索した場合は空のOptionalを返す")
    void testFindByIdNotFound() {
        Optional<RadioStation> foundStation = radioStationRepository.findById(999L);

        assertFalse(foundStation.isPresent());
    }

    @Test
    @DisplayName("全ての放送局を取得できる")
    void testFindAll() {
        entityManager.persistAndFlush(testStation1);
        entityManager.persistAndFlush(testStation2);

        List<RadioStation> stations = radioStationRepository.findAll();

        assertEquals(2, stations.size());
        assertTrue(stations.stream().anyMatch(s -> "NHKラジオ第1".equals(s.getName())));
        assertTrue(stations.stream().anyMatch(s -> "NHKラジオ第2".equals(s.getName())));
    }

    @Test
    @DisplayName("放送局を更新できる")
    void testUpdateStation() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation1);

        savedStation.setName("更新されたラジオ局");
        RadioStation updatedStation = radioStationRepository.save(savedStation);

        assertEquals(savedStation.getId(), updatedStation.getId());
        assertEquals("更新されたラジオ局", updatedStation.getName());
    }

    @Test
    @DisplayName("放送局を削除できる")
    void testDeleteStation() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation1);
        Long stationId = savedStation.getId();

        radioStationRepository.delete(savedStation);

        Optional<RadioStation> foundStation = radioStationRepository.findById(stationId);
        assertFalse(foundStation.isPresent());
    }

    @Test
    @DisplayName("放送局が存在するかチェックできる")
    void testExistsById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation1);

        boolean exists = radioStationRepository.existsById(savedStation.getId());
        boolean notExists = radioStationRepository.existsById(999L);

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("放送局数をカウントできる")
    void testCount() {
        entityManager.persistAndFlush(testStation1);
        entityManager.persistAndFlush(testStation2);

        long count = radioStationRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("放送局名のユニーク制約をテストする")
    void testStationNameUniqueConstraint() {
        entityManager.persistAndFlush(testStation1);

        RadioStation duplicateStation = new RadioStation();
        duplicateStation.setName("NHKラジオ第1"); // 同じ放送局名

        // ユニーク制約違反によりexceptionが発生することを確認
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(duplicateStation);
        });
    }

    @Test
    @DisplayName("IDによる削除ができる")
    void testDeleteById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation1);
        Long stationId = savedStation.getId();

        radioStationRepository.deleteById(stationId);

        Optional<RadioStation> foundStation = radioStationRepository.findById(stationId);
        assertFalse(foundStation.isPresent());
    }

    @Test
    @DisplayName("複数の放送局を一括保存できる")
    void testSaveAll() {
        List<RadioStation> stations = List.of(testStation1, testStation2);

        List<RadioStation> savedStations = radioStationRepository.saveAll(stations);

        assertEquals(2, savedStations.size());
        assertTrue(savedStations.stream().allMatch(s -> s.getId() != null));
    }

    @Test
    @DisplayName("複数のIDで放送局を検索できる")
    void testFindAllById() {
        RadioStation savedStation1 = entityManager.persistAndFlush(testStation1);
        RadioStation savedStation2 = entityManager.persistAndFlush(testStation2);

        List<RadioStation> foundStations = radioStationRepository.findAllById(
                List.of(savedStation1.getId(), savedStation2.getId())
        );

        assertEquals(2, foundStations.size());
    }
}
