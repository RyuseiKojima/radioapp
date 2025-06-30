package com.example.radioapp.controller;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.FollowService;
import com.example.radioapp.service.RadioProgramService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * ラジオ番組
 */
@Controller
@RequestMapping("/programs")
public class RadioProgramController {

    private final RadioProgramService programService;
    private final FollowService followService;

    public RadioProgramController(RadioProgramService programService, FollowService followService) {
        this.programService = programService;
        this.followService = followService;
    }

    /**
     * 一覧画面
     *
     * @param model   遷移先に渡すデータ
     * @param message メッセージ
     * @param user    認証済みユーザー
     * @return 一覧画面
     */
    @GetMapping
    public String list(Model model, @ModelAttribute("message") String message, @AuthenticationPrincipal AppUser user) {
        List<RadioProgram> programs = programService.findAll();
        model.addAttribute("programs", programs);
        model.addAttribute("message", message);
        
        // ユーザーがログインしている場合、フォロー状態を一括取得
        if (user != null) {
            Map<Long, Boolean> followStatusMap = followService.getFollowStatusMap(programs, user);
            model.addAttribute("followStatusMap", followStatusMap);
        }
        
        return "programs/list";
    }

    /**
     * 詳細画面
     * @param id 番組id
     * @param model 遷移先に渡すデータ
     * @return 詳細画面
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        RadioProgram program = programService.findById(id);
        model.addAttribute("program", program);
        return "programs/detail";
    }
}
