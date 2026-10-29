package com.biblioteca.controller;

import com.biblioteca.repository.AutorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutorController {

    private final AutorRepository autorRepository;

    public AutorController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @GetMapping("/autores")
    public String listar(Model model) {
        model.addAttribute("autores", autorRepository.findAll());
        return "autores";
    }
}