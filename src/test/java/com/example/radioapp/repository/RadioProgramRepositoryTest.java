package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioProgram;
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
 * RadioProgramRepositoryの統合テストクラス
 */
@DataJpaTest
@ActiveProfiles("test")
class RadioProgramRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RadioProgramRepository radioProgramRepository;

    private RadioStation testStation;
    private RadioProgram testProgram1;
    private RadioProgram testProgram2;

    @BeforeEach
    void setUp() {
        // テスト用の放送局を作成
        testStation = new RadioStation();
        testStation.setName("NHKラジオ第1");

        testProgram1 = new RadioProgram();
        testProgram1.setTitle("ニュースラジオ");
        testProgram1.setHost("田中アナウンサー");
        testProgram1.setDescription("最新のニュースをお届けします");
        testProgram1.setStation(testStation);

        testProgram2 = new RadioProgram();
        testProgram2.setTitle("音楽番組");
        testProgram2.setHost("佐藤DJ");
        testProgram2.setDescription("最新の音楽をお楽しみください");
        testProgram2.setStation(testStation);
    }

    @Test
    @DisplayName("番組を保存できる")
    void testSaveProgram() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);

        RadioProgram savedProgram = radioProgramRepository.save(testProgram1);

        assertNotNull(savedProgram);
        assertNotNull(savedProgram.getId());
        assertEquals("ニュースラジオ", savedProgram.getTitle());
        assertEquals("田中アナウンサー", savedProgram.getHost());
        assertEquals("最新のニュースをお届けします", savedProgram.getDescription());
        assertEquals(savedStation.getId(), savedProgram.getStation().getId());
    }

    @Test
    @DisplayName("IDで番組を検索できる")
    void testFindById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);

        Optional<RadioProgram> foundProgram = radioProgramRepository.findById(savedProgram.getId());

        assertTrue(foundProgram.isPresent());
        assertEquals(savedProgram.getId(), foundProgram.get().getId());
        assertEquals("ニュースラジオ", foundProgram.get().getTitle());
        assertEquals("田中アナウンサー", foundProgram.get().getHost());
    }

    @Test
    @DisplayName("存在しないIDで検索した場合は空のOptionalを返す")
    void testFindByIdNotFound() {
        Optional<RadioProgram> foundProgram = radioProgramRepository.findById(999L);

        assertFalse(foundProgram.isPresent());
    }

    @Test
    @DisplayName("全ての番組を取得できる")
    void testFindAll() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        testProgram2.setStation(savedStation);
        entityManager.persistAndFlush(testProgram1);
        entityManager.persistAndFlush(testProgram2);

        List<RadioProgram> programs = radioProgramRepository.findAll();

        assertEquals(2, programs.size());
        assertTrue(programs.stream().anyMatch(p -> "ニュースラジオ".equals(p.getTitle())));
        assertTrue(programs.stream().anyMatch(p -> "音楽番組".equals(p.getTitle())));
    }

    @Test
    @DisplayName("番組を更新できる")
    void testUpdateProgram() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);

        savedProgram.setTitle("更新されたニュース番組");
        savedProgram.setHost("山田アナウンサー");
        savedProgram.setDescription("更新された番組説明");
        RadioProgram updatedProgram = radioProgramRepository.save(savedProgram);

        assertEquals(savedProgram.getId(), updatedProgram.getId());
        assertEquals("更新されたニュース番組", updatedProgram.getTitle());
        assertEquals("山田アナウンサー", updatedProgram.getHost());
        assertEquals("更新された番組説明", updatedProgram.getDescription());
    }

    @Test
    @DisplayName("番組を削除できる")
    void testDeleteProgram() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        Long programId = savedProgram.getId();

        radioProgramRepository.delete(savedProgram);

        Optional<RadioProgram> foundProgram = radioProgramRepository.findById(programId);
        assertFalse(foundProgram.isPresent());
    }

    @Test
    @DisplayName("番組が存在するかチェックできる")
    void testExistsById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);

        boolean exists = radioProgramRepository.existsById(savedProgram.getId());
        boolean notExists = radioProgramRepository.existsById(999L);

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("番組数をカウントできる")
    void testCount() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        testProgram2.setStation(savedStation);
        entityManager.persistAndFlush(testProgram1);
        entityManager.persistAndFlush(testProgram2);

        long count = radioProgramRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("放送局との関連が正しく保存される")
    void testStationAssociation() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);

        RadioProgram foundProgram = radioProgramRepository.findById(savedProgram.getId()).orElse(null);
        assertNotNull(foundProgram);
        assertNotNull(foundProgram.getStation());
        assertEquals(savedStation.getId(), foundProgram.getStation().getId());
        assertEquals("NHKラジオ第1", foundProgram.getStation().getName());
    }

    @Test
    @DisplayName("IDによる削除ができる")
    void testDeleteById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        Long programId = savedProgram.getId();

        radioProgramRepository.deleteById(programId);

        Optional<RadioProgram> foundProgram = radioProgramRepository.findById(programId);
        assertFalse(foundProgram.isPresent());
    }

    @Test
    @DisplayName("複数の番組を一括保存できる")
    void testSaveAll() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        testProgram2.setStation(savedStation);
        List<RadioProgram> programs = List.of(testProgram1, testProgram2);

        List<RadioProgram> savedPrograms = radioProgramRepository.saveAll(programs);

        assertEquals(2, savedPrograms.size());
        assertTrue(savedPrograms.stream().allMatch(p -> p.getId() != null));
        assertTrue(savedPrograms.stream().allMatch(p -> p.getStation() != null));
    }

    @Test
    @DisplayName("複数のIDで番組を検索できる")
    void testFindAllById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        testProgram2.setStation(savedStation);
        RadioProgram savedProgram1 = entityManager.persistAndFlush(testProgram1);
        RadioProgram savedProgram2 = entityManager.persistAndFlush(testProgram2);

        List<RadioProgram> foundPrograms = radioProgramRepository.findAllById(
                List.of(savedProgram1.getId(), savedProgram2.getId())
        );

        assertEquals(2, foundPrograms.size());
    }
}
