package com.example.radioapp.controller;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.RadioProgramService;
import com.example.radioapp.service.RadioStationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/programs")
public class RadioProgramController {

    private final RadioProgramService programService;
    private final RadioStationService stationService;

    public RadioProgramController(RadioProgramService programService, RadioStationService stationService) {
        this.programService = programService;
        this.stationService = stationService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("programs", programService.findAll());
        return "programs/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        RadioProgram program = programService.findById(id);
        model.addAttribute("program", program);
        return "programs/detail";
    }
}
