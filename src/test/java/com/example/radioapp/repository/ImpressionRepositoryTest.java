package com.example.radioapp.repository;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.domain.Impression;
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
 * ImpressionRepositoryの統合テストクラス
 */
@DataJpaTest
@ActiveProfiles("test")
class ImpressionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ImpressionRepository impressionRepository;

    private RadioStation testStation;
    private RadioProgram testProgram1;
    private RadioProgram testProgram2;
    private RadioEpisode testEpisode1;
    private RadioEpisode testEpisode2;
    private RadioEpisode testEpisode3;
    private AppUser testUser;
    private Impression testImpression1;
    private Impression testImpression2;
    private Impression testImpression3;

    @BeforeEach
    void setUp() {
        // テスト用の放送局を作成
        testStation = new RadioStation();
        testStation.setName("NHKラジオ第1");

        // テスト用の番組を作成
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

        // テスト用のエピソードを作成
        testEpisode1 = new RadioEpisode();
        testEpisode1.setProgram(testProgram1);
        testEpisode1.setBroadcastDate(LocalDate.of(2025, 7, 1));

        testEpisode2 = new RadioEpisode();
        testEpisode2.setProgram(testProgram1);
        testEpisode2.setBroadcastDate(LocalDate.of(2025, 7, 2));

        testEpisode3 = new RadioEpisode();
        testEpisode3.setProgram(testProgram2);
        testEpisode3.setBroadcastDate(LocalDate.of(2025, 7, 1));

        // テスト用のユーザーを作成
        testUser = new AppUser();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole("USER");

        // テスト用の感想を作成
        testImpression1 = new Impression();
        testImpression1.setUser(testUser);
        testImpression1.setEpisode(testEpisode1);
        testImpression1.setTitle("素晴らしいニュース番組");
        testImpression1.setContent("とても分かりやすいニュース解説でした。");

        testImpression2 = new Impression();
        testImpression2.setUser(testUser);
        testImpression2.setEpisode(testEpisode2);
        testImpression2.setTitle("今日のニュースも良かった");
        testImpression2.setContent("昨日に続いて、今日も興味深い内容でした。");

        testImpression3 = new Impression();
        testImpression3.setUser(testUser);
        testImpression3.setEpisode(testEpisode3);
        testImpression3.setTitle("音楽番組の感想");
        testImpression3.setContent("新しい音楽をたくさん知ることができました。");
    }

    @Test
    @DisplayName("感想を保存できる")
    void testSaveImpression() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode);

        Impression savedImpression = impressionRepository.save(testImpression1);

        assertNotNull(savedImpression);
        assertNotNull(savedImpression.getId());
        assertEquals("素晴らしいニュース番組", savedImpression.getTitle());
        assertEquals("とても分かりやすいニュース解説でした。", savedImpression.getContent());
        assertEquals(savedUser.getId(), savedImpression.getUser().getId());
        assertEquals(savedEpisode.getId(), savedImpression.getEpisode().getId());
    }

    @Test
    @DisplayName("IDで感想を検索できる")
    void testFindById() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode);
        Impression savedImpression = entityManager.persistAndFlush(testImpression1);

        Optional<Impression> foundImpression = impressionRepository.findById(savedImpression.getId());

        assertTrue(foundImpression.isPresent());
        assertEquals(savedImpression.getId(), foundImpression.get().getId());
        assertEquals("素晴らしいニュース番組", foundImpression.get().getTitle());
    }

    @Test
    @DisplayName("存在しないIDで検索した場合は空のOptionalを返す")
    void testFindByIdNotFound() {
        Optional<Impression> foundImpression = impressionRepository.findById(999L);

        assertFalse(foundImpression.isPresent());
    }

    @Test
    @DisplayName("番組IDで感想一覧を取得できる")
    void testFindByProgramId() {
        // テストデータを永続化
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        testProgram2.setStation(savedStation);
        RadioProgram savedProgram1 = entityManager.persistAndFlush(testProgram1);
        RadioProgram savedProgram2 = entityManager.persistAndFlush(testProgram2);
        
        testEpisode1.setProgram(savedProgram1);
        testEpisode2.setProgram(savedProgram1);
        testEpisode3.setProgram(savedProgram2);
        RadioEpisode savedEpisode1 = entityManager.persistAndFlush(testEpisode1);
        RadioEpisode savedEpisode2 = entityManager.persistAndFlush(testEpisode2);
        RadioEpisode savedEpisode3 = entityManager.persistAndFlush(testEpisode3);
        
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode1);
        testImpression2.setUser(savedUser);
        testImpression2.setEpisode(savedEpisode2);
        testImpression3.setUser(savedUser);
        testImpression3.setEpisode(savedEpisode3);
        
        entityManager.persistAndFlush(testImpression1);
        entityManager.persistAndFlush(testImpression2);
        entityManager.persistAndFlush(testImpression3);

        List<Impression> impressions = impressionRepository.findByProgramId(savedProgram1.getId());

        assertEquals(2, impressions.size());
        // ID降順でソートされているかチェック
        assertTrue(impressions.get(0).getId() > impressions.get(1).getId());
        
        // すべてがprogram1に関連する感想であることを確認
        assertTrue(impressions.stream().allMatch(i -> 
            i.getEpisode().getProgram().getId().equals(savedProgram1.getId())));
    }

    @Test
    @DisplayName("存在しない番組IDで検索した場合は空のリストを返す")
    void testFindByProgramIdNotFound() {
        List<Impression> impressions = impressionRepository.findByProgramId(999L);

        assertTrue(impressions.isEmpty());
    }

    @Test
    @DisplayName("番組IDで感想一覧を取得する際のソート順をテスト")
    void testFindByProgramIdSortOrder() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        testEpisode2.setProgram(savedProgram);
        RadioEpisode savedEpisode1 = entityManager.persistAndFlush(testEpisode1);
        RadioEpisode savedEpisode2 = entityManager.persistAndFlush(testEpisode2);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        // 順番を意図的に変えて保存
        testImpression2.setUser(savedUser);
        testImpression2.setEpisode(savedEpisode2);
        Impression firstSaved = entityManager.persistAndFlush(testImpression2);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode1);
        Impression secondSaved = entityManager.persistAndFlush(testImpression1);

        List<Impression> impressions = impressionRepository.findByProgramId(savedProgram.getId());

        assertEquals(2, impressions.size());
        // ID降順なので、後から保存されたものが最初に来る
        assertEquals(secondSaved.getId(), impressions.get(0).getId());
        assertEquals(firstSaved.getId(), impressions.get(1).getId());
    }

    @Test
    @DisplayName("全ての感想を取得できる")
    void testFindAll() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        testEpisode2.setProgram(savedProgram);
        RadioEpisode savedEpisode1 = entityManager.persistAndFlush(testEpisode1);
        RadioEpisode savedEpisode2 = entityManager.persistAndFlush(testEpisode2);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode1);
        testImpression2.setUser(savedUser);
        testImpression2.setEpisode(savedEpisode2);
        entityManager.persistAndFlush(testImpression1);
        entityManager.persistAndFlush(testImpression2);

        List<Impression> impressions = impressionRepository.findAll();

        assertEquals(2, impressions.size());
    }

    @Test
    @DisplayName("感想を更新できる")
    void testUpdateImpression() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode);
        Impression savedImpression = entityManager.persistAndFlush(testImpression1);

        savedImpression.setTitle("更新されたタイトル");
        savedImpression.setContent("更新された感想内容");
        Impression updatedImpression = impressionRepository.save(savedImpression);

        assertEquals(savedImpression.getId(), updatedImpression.getId());
        assertEquals("更新されたタイトル", updatedImpression.getTitle());
        assertEquals("更新された感想内容", updatedImpression.getContent());
    }

    @Test
    @DisplayName("感想を削除できる")
    void testDeleteImpression() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        RadioEpisode savedEpisode = entityManager.persistAndFlush(testEpisode1);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode);
        Impression savedImpression = entityManager.persistAndFlush(testImpression1);
        Long impressionId = savedImpression.getId();

        impressionRepository.delete(savedImpression);

        Optional<Impression> foundImpression = impressionRepository.findById(impressionId);
        assertFalse(foundImpression.isPresent());
    }

    @Test
    @DisplayName("感想数をカウントできる")
    void testCount() {
        RadioStation savedStation = entityManager.persistAndFlush(testStation);
        testProgram1.setStation(savedStation);
        RadioProgram savedProgram = entityManager.persistAndFlush(testProgram1);
        testEpisode1.setProgram(savedProgram);
        testEpisode2.setProgram(savedProgram);
        RadioEpisode savedEpisode1 = entityManager.persistAndFlush(testEpisode1);
        RadioEpisode savedEpisode2 = entityManager.persistAndFlush(testEpisode2);
        AppUser savedUser = entityManager.persistAndFlush(testUser);
        
        testImpression1.setUser(savedUser);
        testImpression1.setEpisode(savedEpisode1);
        testImpression2.setUser(savedUser);
        testImpression2.setEpisode(savedEpisode2);
        entityManager.persistAndFlush(testImpression1);
        entityManager.persistAndFlush(testImpression2);

        long count = impressionRepository.count();

        assertEquals(2, count);
    }
}
