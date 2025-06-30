package com.example.radioapp.service;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.domain.Impression;
import com.example.radioapp.domain.RadioEpisode;
import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.dto.ImpressionForm;
import com.example.radioapp.repository.ImpressionRepository;
import com.example.radioapp.repository.RadioProgramRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 感想データ業務処理
 */
@Service
public class ImpressionService {
    private final ImpressionRepository impressionRepository;
    private final RadioProgramRepository radioProgramRepository;
    private final RadioEpisodeService radioEpisodeService;

    public ImpressionService(ImpressionRepository impressionRepository,
                             RadioProgramRepository radioProgramRepository,
                             RadioEpisodeService radioEpisodeService) {
        this.impressionRepository = impressionRepository;
        this.radioProgramRepository = radioProgramRepository;
        this.radioEpisodeService = radioEpisodeService;
    }

    /**
     * 感想を保存
     * @param user ユーザデータ
     * @param impressionForm 感想フォームデータ
     */
    public void save(AppUser user, ImpressionForm impressionForm) {
        Long programId = impressionForm.getProgramId();
        LocalDate localDate = impressionForm.getBroadcastDate();
        String title = impressionForm.getTitle();
        String content = impressionForm.getContent();
        RadioEpisode radioEpisode;
        try {
            radioEpisode = radioEpisodeService.findByProgramIdAndBroadcastDate(programId, localDate);
        } catch (IllegalArgumentException e) {
            radioEpisode = new RadioEpisode();
            RadioProgram radioProgram = radioProgramRepository.findById(programId).orElseThrow();
            radioEpisode.setProgram(radioProgram);
            radioEpisode.setBroadcastDate(localDate);
            radioEpisodeService.save(radioEpisode);
        }

        Impression impression = new Impression();
        impression.setUser(user);
        impression.setEpisode(radioEpisode);
        impression.setTitle(title);
        impression.setContent(content);

        impressionRepository.save(impression);
    }

    /**
     * 番組IDから感想一覧を取得
     * @param programId 番組ID
     * @return 感想一覧
     */
    public List<Impression> findByProgramId(Long programId) {
        return impressionRepository.findByProgramId(programId);
    }
}