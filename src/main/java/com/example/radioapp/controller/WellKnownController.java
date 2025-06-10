package com.example.radioapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 警告回避用
 */
@RestController
@SuppressWarnings("unused")
public class WellKnownController {

    @RequestMapping("/.well-known/appspecific/com.chrome.devtools.json")
    public ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
