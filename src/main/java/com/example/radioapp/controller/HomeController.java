package com.example.radioapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ホーム画面
 */
@Controller
@SuppressWarnings("unused")
public class HomeController {
    /**
     * ホーム画面
     * @return ホーム画面に遷移
     */
    @GetMapping("/")
    public String index() {
        return "index"; // resources/templates/index.html を表示
    }
}
