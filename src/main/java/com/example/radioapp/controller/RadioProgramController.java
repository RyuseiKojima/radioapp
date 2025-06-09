package com.example.radioapp.controller;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.RadioProgramService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ラジオ番組
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping("/programs")
public class RadioProgramController {

    private final RadioProgramService programService;

    public RadioProgramController(RadioProgramService programService) {
        this.programService = programService;
    }

    /**
     * 一覧画面
     * @param model 遷移先に渡すデータ
     * @return 一覧画面
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("programs", programService.findAll());
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
