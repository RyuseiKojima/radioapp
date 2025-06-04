package com.example.radioapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン機能
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {
        return "login"; // templates/login.html に対応
    }
}