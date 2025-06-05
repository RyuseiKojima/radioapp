package com.example.radioapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WellKnownController {

    @RequestMapping("/.well-known/appspecific/com.chrome.devtools.json")
    @ResponseBody
    public ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
