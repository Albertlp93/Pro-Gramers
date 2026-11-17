package com.biblioteca.controller;

import com.biblioteca.dto.RegistroForm;
import com.biblioteca.service.RegistroService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registro", new RegistroForm("", "", "", "", "", ""));
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid @ModelAttribute("registro") RegistroForm formulario,
            BindingResult resultado) {

        if (resultado.hasErrors()) {
            return "registro";
        }

        try {
            registroService.registrar(formulario);
        } catch (RegistroService.RegistroInvalido ex) {
            resultado.rejectValue(ex.getCampo(), "registro.invalido", ex.getMessage());
            return "registro";
        } catch (DataIntegrityViolationException ex) {
            resultado.reject("registro.noGuardado",
                    "No se pudo crear la cuenta. Comprueba si el correo o el DNI ya están registrados.");
            return "registro";
        }

        return "redirect:/login?registrado";
    }
}