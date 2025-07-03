package com.example.radioapp.repository;

import com.example.radioapp.domain.RadioEpisode;
import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.domain.RadioStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RadioEpisodeRepositoryの統合テストクラス
 */
@DataJpaTest
@ActiveProfiles("test")
class RadioEpisodeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RadioEpisodeRepository radioEpisodeRepository;

    private RadioStation testStation;
    private RadioProgram testProgram;
    private RadioEpisode testEpisode1;
    private RadioEpisode testEpisode2;

    @BeforeEach
    void setUp() {
        // テスト用の放送局を作成
        testStation = new RadioStation();
        testStation.setName("NHKラジオ第1");

        // テスト用の番組を作成
        testProgram = new RadioProgram();
        testProgram.setTitle("ニュースラジオ");
        testProgram.setHost("田中アナウンサー");
        testProgram.setDescription("最新のニュースをお届けします");
        testProgram.setStation(testStation);

        testEpisode1 = new RadioEpisode();
        testEpisode1.setProgram(testProgram);
        testEpisode1.setBroadcastDate(LocalDate.of(2025, 7, 1));

        testEpisode2 = new RadioEpisode();
        testEpisode2.setProgram(testProgram);
        testEpisode2.setBroadcastDate(LocalDate.of(2025, 7, 2));
    }

    @Test
    @DisplayName("エピソードを保存できる")
    void testSaveEpisode() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);

        RadioEpisode savedEpisode = radioEpisodeRepository.save(testEpisode1);

        assertNotNull(savedEpisode);
        assertNotNull(savedEpisode.getId());
        assertEquals(savedProgram.getId(), savedEpisode.getProgram().getId());
        assertEquals(LocalDate.of(2025, 7, 1), savedEpisode.getBroadcastDate());
    }

    @Test
    @DisplayName("IDでエピソードを検索できる")
    void testFindById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);

        Optional<RadioEpisode> foundEpisode = radioEpisodeRepository.findById(savedEpisode.getId());

        assertTrue(foundEpisode.isPresent());
        assertEquals(savedEpisode.getId(), foundEpisode.get().getId());
        assertEquals(LocalDate.of(2025, 7, 1), foundEpisode.get().getBroadcastDate());
    }

    @Test
    @DisplayName("存在しないIDで検索した場合は空のOptionalを返す")
    void testFindByIdNotFound() {
        Optional<RadioEpisode> foundEpisode = radioEpisodeRepository.findById(999L);

        assertFalse(foundEpisode.isPresent());
    }

    @Test
    @DisplayName("番組IDと放送日でエピソードを検索できる")
    void testFindByProgramIdAndBroadcastDate() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        entityManager.persistAndFlush(testEpisode1);

        Optional<RadioEpisode> foundEpisode = radioEpisodeRepository.findByProgramIdAndBroadcastDate(
                savedProgram.getId(),
                LocalDate.of(2025, 7, 1)
        );

        assertTrue(foundEpisode.isPresent());
        assertEquals(savedProgram.getId(), foundEpisode.get().getProgram().getId());
        assertEquals(LocalDate.of(2025, 7, 1), foundEpisode.get().getBroadcastDate());
    }

    @Test
    @DisplayName("存在しない番組IDと放送日で検索した場合は空のOptionalを返す")
    void testFindByProgramIdAndBroadcastDateNotFound() {
        Optional<RadioEpisode> foundEpisode = radioEpisodeRepository.findByProgramIdAndBroadcastDate(
                999L,
                LocalDate.of(2025, 7, 1)
        );

        assertFalse(foundEpisode.isPresent());
    }

    @Test
    @DisplayName("同じ番組の異なる放送日のエピソードを区別できる")
    void testFindByProgramIdAndBroadcastDateWithMultipleEpisodes() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        testEpisode2.setProgram(savedProgram);
        entityManager.persistAndFlush(testEpisode1);
        entityManager.persistAndFlush(testEpisode2);

        Optional<RadioEpisode> foundEpisode1 = radioEpisodeRepository.findByProgramIdAndBroadcastDate(
                savedProgram.getId(),
                LocalDate.of(2025, 7, 1)
        );
        Optional<RadioEpisode> foundEpisode2 = radioEpisodeRepository.findByProgramIdAndBroadcastDate(
                savedProgram.getId(),
                LocalDate.of(2025, 7, 2)
        );

        assertTrue(foundEpisode1.isPresent());
        assertTrue(foundEpisode2.isPresent());
        assertEquals(LocalDate.of(2025, 7, 1), foundEpisode1.get().getBroadcastDate());
        assertEquals(LocalDate.of(2025, 7, 2), foundEpisode2.get().getBroadcastDate());
    }

    @Test
    @DisplayName("全てのエピソードを取得できる")
    void testFindAll() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        testEpisode2.setProgram(savedProgram);
        entityManager.persistAndFlush(testEpisode1);
        entityManager.persistAndFlush(testEpisode2);

        List<RadioEpisode> episodes = radioEpisodeRepository.findAll();

        assertEquals(2, episodes.size());
    }

    @Test
    @DisplayName("エピソードを更新できる")
    void testUpdateEpisode() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);

        savedEpisode.setBroadcastDate(LocalDate.of(2025, 7, 15));
        RadioEpisode updatedEpisode = radioEpisodeRepository.save(savedEpisode);

        assertEquals(savedEpisode.getId(), updatedEpisode.getId());
        assertEquals(LocalDate.of(2025, 7, 15), updatedEpisode.getBroadcastDate());
    }

    @Test
    @DisplayName("エピソードを削除できる")
    void testDeleteEpisode() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);
        Long episodeId = savedEpisode.getId();

        radioEpisodeRepository.delete(savedEpisode);

        Optional<RadioEpisode> foundEpisode = radioEpisodeRepository.findById(episodeId);
        assertFalse(foundEpisode.isPresent());
    }

    @Test
    @DisplayName("エピソードが存在するかチェックできる")
    void testExistsById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);

        boolean exists = radioEpisodeRepository.existsById(savedEpisode.getId());
        boolean notExists = radioEpisodeRepository.existsById(999L);

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("エピソード数をカウントできる")
    void testCount() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        testEpisode2.setProgram(savedProgram);
        entityManager.persistAndFlush(testEpisode1);
        entityManager.persistAndFlush(testEpisode2);

        long count = radioEpisodeRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("番組との関連が正しく保存される")
    void testProgramAssociation() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);

        RadioEpisode foundEpisode = radioEpisodeRepository.findById(savedEpisode.getId()).orElse(null);
        assertNotNull(foundEpisode);
        assertNotNull(foundEpisode.getProgram());
        assertEquals(savedProgram.getId(), foundEpisode.getProgram().getId());
        assertEquals("ニュースラジオ", foundEpisode.getProgram().getTitle());
    }

    @Test
    @DisplayName("IDによる削除ができる")
    void testDeleteById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);
        Long episodeId = savedEpisode.getId();

        radioEpisodeRepository.deleteById(episodeId);

        Optional<RadioEpisode> foundEpisode = radioEpisodeRepository.findById(episodeId);
        assertFalse(foundEpisode.isPresent());
    }
}
