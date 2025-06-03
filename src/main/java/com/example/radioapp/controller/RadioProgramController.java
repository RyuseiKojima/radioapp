package com.example.radioapp.controller;

import com.example.radioapp.domain.RadioProgram;
import com.example.radioapp.service.RadioProgramService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/programs")
public class RadioProgramController {

    private final RadioProgramService service;

    public RadioProgramController(RadioProgramService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("programs", service.findAll());
        return "programs/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        RadioProgram program = service.findById(id);
        model.addAttribute("program", program);
        return "programs/detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("program", new RadioProgram());
        return "programs/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("program", service.findById(id));
        return "programs/form";
    }

    @PostMapping
    public String save(@Validated @ModelAttribute("program") RadioProgram program,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "programs/form";
        }
        service.save(program);
        return "redirect:/programs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/programs";
    }
}
