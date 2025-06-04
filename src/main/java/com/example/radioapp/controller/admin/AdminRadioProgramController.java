package com.example.radioapp.controller.admin;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.RadioProgramService;
import com.example.radioapp.service.RadioStationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/programs")
public class AdminRadioProgramController {

    private final RadioProgramService programService;
    private final RadioStationService stationService;

    public AdminRadioProgramController(RadioProgramService programService,
                                       RadioStationService stationService) {
        this.programService = programService;
        this.stationService = stationService;
    }

    // 一覧（管理者用）
    @GetMapping
    public String adminList(Model model) {
        model.addAttribute("programs", programService.findAll());
        return "admin/programs/list";
    }

    // 新規作成フォーム
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("program", new RadioProgram());
        model.addAttribute("stations", stationService.findAll());
        return "admin/programs/form";
    }

    // 編集フォーム
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        RadioProgram program = programService.findById(id);
        model.addAttribute("program", program);
        model.addAttribute("stations", stationService.findAll());
        return "admin/programs/form";
    }

    // 保存（新規 or 更新）
    @PostMapping
    public String save(@Valid @ModelAttribute("program") RadioProgram program,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("stations", stationService.findAll());
            return "admin/programs/form";
        }
        programService.save(program);
        return "redirect:/admin/programs";
    }

    // 削除
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        programService.delete(id);
        return "redirect:/admin/programs";
    }
}
