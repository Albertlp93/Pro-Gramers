package com.biblioteca.controller;

import java.time.Year;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.AutorRepository;
import com.biblioteca.repository.LibroRepository;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final MessageSource messageSource;

    public LibroController(
            LibroRepository libroRepository,
            AutorRepository autorRepository,
            MessageSource messageSource) {

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
        this.messageSource = messageSource;
    }

    @InitBinder("libro")
    public void configurarFormulario(WebDataBinder binder) {
        binder.setAllowedFields(
            "titulo",
            "isbn",
            "editorial",
            "anioPublicacion",
            "genero",
            "ejemplaresDisponibles",
            "autores"
        );
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("libros", libroRepository.findAll());
        return "libros";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autores", autorRepository.findAll());

        return "libro-form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("libro") Libro libro,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!resultado.hasFieldErrors("isbn")
                && libroRepository.existsByIsbn(libro.getIsbn())) {

            resultado.rejectValue(
                "isbn",
                "libro.isbn.duplicado"
            );
        }

        validarAnio(libro, resultado);

        if (resultado.hasErrors()) {
            model.addAttribute("autores", autorRepository.findAll());
            return "libro-form";
        }

        libroRepository.save(libro);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            texto("libro.creado")
        );

        return "redirect:/libros";
    }

    @GetMapping("/{id}/editar")
        public String editar(@PathVariable Long id, Model model) {
            Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    texto("libro.noEncontrado")
                ));

            model.addAttribute("libro", libro);
            model.addAttribute("autores", autorRepository.findAll());

            return "libro-form";
        }

    @PostMapping("/{id}/guardar")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("libro") Libro formulario,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        Libro existente = libroRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                texto("libro.noEncontrado")
            ));

        if (!resultado.hasFieldErrors("isbn")
                && libroRepository.existsByIsbnAndIdNot(
                    formulario.getIsbn(), id)) {

            resultado.rejectValue(
                "isbn",
                "libro.isbn.duplicadoOtro"
            );
        }

        validarAnio(formulario, resultado);

        if (resultado.hasErrors()) {
            formulario.setId(id);
            model.addAttribute("autores", autorRepository.findAll());
            return "libro-form";
        }

        existente.setTitulo(formulario.getTitulo());
        existente.setIsbn(formulario.getIsbn());
        existente.setEditorial(formulario.getEditorial());
        existente.setAnioPublicacion(formulario.getAnioPublicacion());
        existente.setGenero(formulario.getGenero());
        existente.setEjemplaresDisponibles(
            formulario.getEjemplaresDisponibles()
        );
        existente.setAutores(formulario.getAutores());

        libroRepository.save(existente);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            texto("libro.actualizado")
        );

        return "redirect:/libros";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (!libroRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                texto("libro.noEncontrado")
            );
        }

        if (libroRepository.contarPrestamos(id) > 0) {
            redirectAttributes.addFlashAttribute(
                "error",
                texto("libro.eliminar.conPrestamos")
            );

            return "redirect:/libros";
        }

        try {
            libroRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                "mensaje",
                texto("libro.eliminado")
            );
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute(
                "error",
                texto("libro.eliminar.conDatos")
            );
        }

        return "redirect:/libros";
    }

    private void validarAnio(Libro libro, BindingResult resultado) {
        Integer anio = libro.getAnioPublicacion();
        int anioActual = Year.now().getValue();

        if (anio != null
                && !resultado.hasFieldErrors("anioPublicacion")
                && anio > anioActual) {

            resultado.rejectValue(
                "anioPublicacion",
                "libro.anio.futuro",
                new Object[] { String.valueOf(anioActual) },
                null
            );
        }
    }

    // Devuelve el texto de messages.properties para la clave indicada
    private String texto(String clave) {
        return messageSource.getMessage(
            clave, null, LocaleContextHolder.getLocale()
        );
    }
}