package com.example.radioapp.controller.admin;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.RadioProgramService;
import com.example.radioapp.service.RadioStationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 番組管理（管理者）
 */
@Controller
@SuppressWarnings("unused")
@RequestMapping("/admin/programs")
public class AdminRadioProgramController {

    private final RadioProgramService programService;
    private final RadioStationService stationService;

    public AdminRadioProgramController(RadioProgramService programService,
                                       RadioStationService stationService) {
        this.programService = programService;
        this.stationService = stationService;
    }

    /**
     * 一覧（管理者用）
     * @param model viewへ渡すデータ
     * @return 一覧画面に遷移
     */
    @GetMapping
    public String adminList(Model model) {
        model.addAttribute("programs", programService.findAll());
        return "admin/programs/list";
    }

    /**
     * 新規作成フォーム
     * @param model viewへ渡すデータ
     * @return フォーム画面に遷移
     */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("program", new RadioProgram());
        model.addAttribute("stations", stationService.findAll());
        return "admin/programs/form";
    }

    /**
     * 編集フォーム
     * @param id id
     * @param model viewへ渡すデータ
     * @return フォーム画面に遷移
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        RadioProgram program = programService.findById(id);
        model.addAttribute("program", program);
        model.addAttribute("stations", stationService.findAll());
        return "admin/programs/form";
    }

    /**
     * 保存（新規 or 更新）
     * @param program 番組データ
     * @param result バリデ結果
     * @param model viewへ渡すデータ
     * @return 成功：一覧に遷移、失敗：フォームに戻る
     */
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

    /**
     * 削除
     * @param id id
     * @return 一覧に遷移
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        programService.delete(id);
        return "redirect:/admin/programs";
    }
}
