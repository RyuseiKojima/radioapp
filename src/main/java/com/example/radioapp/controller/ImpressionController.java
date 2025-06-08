package com.example.radioapp.controller;

import com.example.radioapp.domain.AppUser;
import com.example.radioapp.dto.ImpressionForm;
import com.example.radioapp.service.ImpressionService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/impressions")
public class ImpressionController {
    private final ImpressionService impressionService;

    public ImpressionController(ImpressionService impressionService) {
        this.impressionService = impressionService;
    }

    /**
     * 新規投稿フォーム
     * @param programId
     * @param model
     * @return
     */
    @GetMapping("/new/{programId}")
    public String newForm(@RequestParam Long programId, Model model) {
        ImpressionForm impressionForm = new ImpressionForm();
        impressionForm.setProgramId(programId);
        model.addAttribute("impressionForm", impressionForm);
        return "impressions/form";
    }

    /**
     * 保存処理
     * @param user
     * @param impressionForm
     * @param result
     * @param redirectAttributes
     * @param model
     * @return
     */
    @PostMapping("/post")
    public String post(@AuthenticationPrincipal AppUser user,
                       @ModelAttribute("impressionForm") @Valid ImpressionForm impressionForm,
                       BindingResult result,
                       RedirectAttributes redirectAttributes,
                       Model model) {

        if (user == null) { // 未ログインならログインページへ
            return "redirect:/login";
        }

        if (result.hasErrors()) { // 入力エラーがあれば入力フォームに遷移
            model.addAttribute("impressionForm", impressionForm);
            return "impressions/form";
        }
        impressionService.save(user, impressionForm);

        redirectAttributes.addFlashAttribute("message", "感想を投稿しました！");
        return "redirect:/programs/list";
    }
}