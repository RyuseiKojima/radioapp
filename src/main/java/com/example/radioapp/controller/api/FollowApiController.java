package com.example.radioapp.controller.api;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.service.FollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * フォロー機能API
 */
@RestController
@RequestMapping("/api/follow")
public class FollowApiController {

    private static final Logger logger = LoggerFactory.getLogger(FollowApiController.class);
    private final FollowService followService;

    public FollowApiController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * フォロー状態を確認
     * @param programId 番組ID
     * @param user 認証済みユーザー
     * @return フォロー状態
     */
    @GetMapping("/status/{programId}")
    public ResponseEntity<Map<String, Boolean>> getFollowStatus(
            @PathVariable Long programId, 
            @AuthenticationPrincipal AppUser user) {
        
        logger.info("フォロー状態確認API呼び出し: 番組ID={}, ユーザー={}", programId, user != null ? user.getUsername() : "未認証");
        
        if (user == null) {
            logger.info("未認証ユーザーのため、フォロー状態はfalse");
            return ResponseEntity.ok(Map.of("isFollowing", false));
        }
        
        try {
            boolean isFollowing = followService.isFollowing(programId, user);
            logger.info("フォロー状態取得結果: 番組ID={}, フォロー状態={}", programId, isFollowing);
            return ResponseEntity.ok(Map.of("isFollowing", isFollowing));
        } catch (Exception e) {
            logger.error("フォロー状態確認中にエラーが発生", e);
            return ResponseEntity.ok(Map.of("isFollowing", false));
        }
    }

    /**
     * フォロー状態を切り替え
     * @param programId 番組ID
     * @param user 認証済みユーザー
     * @return 新しいフォロー状態
     */
    @PostMapping("/toggle/{programId}")
    public ResponseEntity<Map<String, Boolean>> toggleFollow(
            @PathVariable Long programId, 
            @AuthenticationPrincipal AppUser user) {
        
        logger.info("フォロー切り替えAPI呼び出し: 番組ID={}, ユーザー={}", programId, user != null ? user.getUsername() : "未認証");
        
        if (user == null) {
            logger.warn("未認証ユーザーによるフォロー切り替え試行");
            return ResponseEntity.status(401).body(Map.of("error", true));
        }
        
        try {
            followService.toggleFollow(programId, user);
            boolean isFollowing = followService.isFollowing(programId, user);
            logger.info("フォロー切り替え完了: 番組ID={}, 新しいフォロー状態={}", programId, isFollowing);
            return ResponseEntity.ok(Map.of("isFollowing", isFollowing));
        } catch (Exception e) {
            logger.error("フォロー切り替え中にエラーが発生", e);
            return ResponseEntity.status(500).body(Map.of("error", true));
        }
    }
}
