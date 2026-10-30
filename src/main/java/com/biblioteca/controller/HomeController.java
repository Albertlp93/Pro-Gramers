package com.biblioteca.controller;

import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public HomeController(
            LibroRepository libroRepository,
            AutorRepository autorRepository,
            UsuarioRepository usuarioRepository,
            PrestamoRepository prestamoRepository) {

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Bienvenido a la Biblioteca");

        model.addAttribute("totalLibros", libroRepository.count());
        model.addAttribute("totalAutores", autorRepository.count());
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalPrestamos", prestamoRepository.count());

        return "home";
    }
}