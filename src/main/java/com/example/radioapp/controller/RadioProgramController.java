package com.example.radioapp.controller;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.repository.RadioProgramRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/programs")
public class RadioProgramController {

    private final RadioProgramRepository repository;

    public RadioProgramController(RadioProgramRepository repository) {
        this.repository = repository;
    }

    // 一覧表示
    @GetMapping
    public String list(Model model) {
        model.addAttribute("programs", repository.findAll());
        return "programs/list";
    }

    // 登録フォーム表示
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("program", new RadioProgram());
        return "programs/form";
    }

    // 登録処理
    @PostMapping
    public String create(@Validated @ModelAttribute("program") RadioProgram program, BindingResult result) {
        // @Valid を使い、BindingResult でエラーを拾う
        if (result.hasErrors()) {
            // バリデーションエラーがあればform画面に遷移
            return "programs/form";
        }
        repository.save(program);
        return "redirect:/programs";
    }
}
