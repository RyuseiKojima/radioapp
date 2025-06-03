package com.example.radioapp.controller;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.RadioProgramService;
import com.example.radioapp.service.RadioStationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("program", new RadioProgram());
        model.addAttribute("stations", stationService.findAll());
        return "programs/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("program", programService.findById(id));
        model.addAttribute("stations", stationService.findAll());
        return "programs/form";
    }

    @PostMapping
    public String save(@Validated @ModelAttribute("program") RadioProgram program,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "programs/form";
        }
        programService.save(program);
        return "redirect:/programs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        programService.delete(id);
        return "redirect:/programs";
    }
}
