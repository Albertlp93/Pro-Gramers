package com.biblioteca.controller;

import java.security.Principal;

import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.service.FavoritoService;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private final PrestamoRepository prestamoRepository;
    private final FavoritoService favoritoService;
    private final MessageSource messageSource;

    public UserController(
            PrestamoRepository prestamoRepository,
            FavoritoService favoritoService,
            MessageSource messageSource) {

        this.prestamoRepository = prestamoRepository;
        this.favoritoService = favoritoService;
        this.messageSource = messageSource;
    }

    @GetMapping("/prestamos")
    public String misPrestamos(Principal principal, Model model) {
        model.addAttribute("prestamos",
            prestamoRepository.findByUsuarioEmailOrderByFechaPrestamoDesc(
                principal.getName()));

        return "mis-prestamos";
    }

    @GetMapping("/favoritos")
    public String misFavoritos(Principal principal, Model model) {
        model.addAttribute("libros",
            favoritoService.listarFavoritos(principal.getName()));

        return "mis-favoritos";
    }

    // Marca o desmarca un libro como favorito.
    // "origen" indica a qué pantalla volver: el listado de libros o mis favoritos.
    @PostMapping("/favoritos/{id}")
    public String alternarFavorito(
            @PathVariable Long id,
            @RequestParam(defaultValue = "libros") String origen,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        boolean esFavorito =
            favoritoService.alternarFavorito(principal.getName(), id);

        String clave = esFavorito ? "favorito.agregado" : "favorito.quitado";
        redirectAttributes.addFlashAttribute("mensaje",
            messageSource.getMessage(clave, null, LocaleContextHolder.getLocale()));

        return "favoritos".equals(origen)
            ? "redirect:/user/favoritos"
            : "redirect:/libros";
    }
}
