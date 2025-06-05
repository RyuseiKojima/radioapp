package com.example.radioapp.service;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.repository.AppUserRepository;
import com.example.radioapp.repository.RadioProgramRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FollowService {

    private final RadioProgramRepository programRepository;
    private final AppUserRepository appUserRepository;

    public FollowService(RadioProgramRepository programRepository, AppUserRepository appUserRepository) {
        this.programRepository = programRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * 番組をフォロー
     * @param programId
     * @param principalUser
     */
    public void followProgram(Long programId, AppUser principalUser) {
        if (principalUser == null) {
            throw new IllegalStateException("ユーザーがログインしていません。ログインが必要です。");
        }
        // DBからAppUserを再取得してセッション内で扱う（遅延ロード回避）
        AppUser user = appUserRepository.findByUsername(principalUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        RadioProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new IllegalArgumentException("番組が見つかりません"));
        // 一時的にコピー
        Set<RadioProgram> followed = new HashSet<>(user.getFollowedPrograms());

        if (!followed.contains(program)) {
            user.getFollowedPrograms().add(program);
            appUserRepository.save(user);
        }
    }
}