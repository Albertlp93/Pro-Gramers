package com.biblioteca.controller;

import java.security.Principal;

import com.biblioteca.repository.PrestamoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final PrestamoRepository prestamoRepository;

    public UserController(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @GetMapping("/prestamos")
    public String misPrestamos(Principal principal, Model model) {
        model.addAttribute("prestamos",
            prestamoRepository.findByUsuarioEmailOrderByFechaPrestamoDesc(
                principal.getName()));

        return "mis-prestamos";
    }
}