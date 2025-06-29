package com.example.radioapp.controller;


import com.example.radioapp.domain.AppUser;
import com.example.radioapp.service.FollowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * 番組フォロー
 */
@Controller
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    /**
     * 番組フォロー
     * @param id 番組id
     * @param user ユーザデータ
     * @return 番組一覧にリダイレクト
     */
    @PostMapping("/programs/{id}/follow")
    public String followProgram(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        followService.followProgram(id, user);
        return "redirect:/programs";
    }
}
