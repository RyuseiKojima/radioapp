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

    // 編集フォーム表示
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        RadioProgram program = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("指定されたIDが存在しません：" + id ));
        model.addAttribute("program", program);
        return "programs/form";
    }

    // 編集結果の保存（登録と同じPOST先）
    @PostMapping
    public String save(@Validated @ModelAttribute("program") RadioProgram program, BindingResult result) {
        // @Valid を使い、BindingResult でエラーを拾う
        if (result.hasErrors()) {
            // バリデーションエラーがあればform画面に遷移
            return "programs/form";
        }
        repository.save(program); // IDがあれば更新、なければ新規登録
        return "redirect:/programs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/programs";
    }
}
