package com.biblioteca.controller;

import com.biblioteca.repository.LibroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LibroController {

    private final LibroRepository libroRepository;

    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @GetMapping("/libros")
    public String listar(Model model) {
        model.addAttribute("libros", libroRepository.findAll());
        return "libros";
    }
}