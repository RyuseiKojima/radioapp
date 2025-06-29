package com.example.radioapp.controller;

import com.example.radioapp.dto.UserRegistrationForm;
import com.example.radioapp.service.AppUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ラジオ番組
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final AppUserDetailsService appUserDetailsService;

    public RegistrationController(AppUserDetailsService appUserDetailsService) {
        this.appUserDetailsService = appUserDetailsService;
    }

    /**
     * フォームページ
     * @param model Viewに渡すデータ
     * @return ページ
     */
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("form", new UserRegistrationForm());
        return "register";
    }

    /**
     * ユーザ登録処理
     * @param form フォーム丹生録
     * @param result バリデ結果
     * @return ページ
     */
    @PostMapping
    public String register(@ModelAttribute("form") @Valid UserRegistrationForm form,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        appUserDetailsService.save(form);

        return "redirect:/login?registered";
    }
}
