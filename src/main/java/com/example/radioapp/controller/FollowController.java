package com.example.radioapp.controller;


import com.example.radioapp.domain.AppUser;
import com.example.radioapp.service.FollowService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/programs/{id}/follow")
    public String followProgram(@PathVariable Long id, @AuthenticationPrincipal AppUser user) {
        followService.followProgram(id, user);
        return "redirect:/programs";
    }
}
