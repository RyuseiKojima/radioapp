package com.example.radioapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン機能
 */
@Controller
@SuppressWarnings("unused")
public class LoginController {

    /**
     * ログイン画面
     * @return ログイン画面へ遷移
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // templates/login.html に対応
    }
}