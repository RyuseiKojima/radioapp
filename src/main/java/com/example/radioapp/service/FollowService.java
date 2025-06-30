package com.example.radioapp.service;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.repository.AppUserRepository;
import com.example.radioapp.repository.RadioProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * フォローデータ業務処理
 */
@Service
public class FollowService {

    private final RadioProgramRepository programRepository;
    private final AppUserRepository appUserRepository;

    public FollowService(RadioProgramRepository programRepository, AppUserRepository appUserRepository) {
        this.programRepository = programRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * フォロー状態を確認
     * @param programId 番組id
     * @param principalUser ユーザデータ
     * @return true:フォロー中、false:未フォロー
     */
    @Transactional(readOnly = true)
    public boolean isFollowing(Long programId, AppUser principalUser) {
        if (principalUser == null) {
            return false;
        }
        AppUser user = appUserRepository.findByUsername(principalUser.getUsername())
                .orElse(null);
        if (user == null) {
            return false;
        }
        RadioProgram program = programRepository.findById(programId)
                .orElse(null);
        if (program == null) {
            return false;
        }
        
        // 直接コレクションのcontainsメソッドを使用
        return user.getFollowedPrograms().contains(program);
    }

    /**
     * フォロー状態を切り替え（フォロー/アンフォロー）
     * @param programId 番組id
     * @param principalUser ユーザデータ
     */
    @Transactional
    public void toggleFollow(Long programId, AppUser principalUser) {
        if (principalUser == null) {
            throw new IllegalStateException("ユーザーがログインしていません。ログインが必要です。");
        }
        
        // DBからAppUserを再取得してセッション内で扱う（遅延ロード回避）
        AppUser user = appUserRepository.findByUsername(principalUser.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        RadioProgram program = programRepository.findById(programId)
                .orElseThrow(() -> new IllegalArgumentException("番組が見つかりません"));

        // フォロー状態を確認
        boolean currentlyFollowing = user.getFollowedPrograms().contains(program);
        
        if (currentlyFollowing) {
            // フォロー中の場合はアンフォロー
            user.getFollowedPrograms().remove(program);
            program.getFollowers().remove(user);
        } else {
            // 未フォローの場合はフォロー
            user.getFollowedPrograms().add(program);
            program.getFollowers().add(user);
        }
        
        // 両方のエンティティを保存
        appUserRepository.save(user);
        programRepository.save(program);
    }

    /**
     * 番組一覧のフォロー状態を一括取得
     * @param programs 番組一覧
     * @param principalUser ユーザデータ
     * @return 番組IDとフォロー状態のマップ
     */
    @Transactional(readOnly = true)
    public Map<Long, Boolean> getFollowStatusMap(List<RadioProgram> programs, AppUser principalUser) {
        Map<Long, Boolean> followStatusMap = new HashMap<>();
        
        if (principalUser == null) {
            // ログインしていない場合はすべてfalse
            for (RadioProgram program : programs) {
                followStatusMap.put(program.getId(), false);
            }
            return followStatusMap;
        }
        
        AppUser user = appUserRepository.findByUsername(principalUser.getUsername())
                .orElse(null);
        if (user == null) {
            // ユーザーが見つからない場合はすべてfalse
            for (RadioProgram program : programs) {
                followStatusMap.put(program.getId(), false);
            }
            return followStatusMap;
        }
        
        // ユーザーがフォローしている番組のIDセットを取得
        Set<RadioProgram> followedPrograms = user.getFollowedPrograms();
        
        // 各番組のフォロー状態を判定
        for (RadioProgram program : programs) {
            boolean isFollowing = followedPrograms.contains(program);
            followStatusMap.put(program.getId(), isFollowing);
        }
        
        return followStatusMap;
    }
}