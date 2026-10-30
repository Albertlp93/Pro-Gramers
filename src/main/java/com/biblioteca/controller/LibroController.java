package com.biblioteca.controller;

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

@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroController(
            LibroRepository libroRepository,
            AutorRepository autorRepository) {

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
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
                "isbn.duplicado",
                "Ya existe un libro con ese ISBN."
            );
        }

        if (resultado.hasErrors()) {
            model.addAttribute("autores", autorRepository.findAll());
            return "libro-form";
        }

        libroRepository.save(libro);

        redirectAttributes.addFlashAttribute(
            "mensaje",
            "Libro creado correctamente."
        );

        return "redirect:/libros";
    }

    @GetMapping("/{id}/editar")
        public String editar(@PathVariable Long id, Model model) {
            Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Libro no encontrado."
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
                "Libro no encontrado."
            ));

        if (!resultado.hasFieldErrors("isbn")
                && libroRepository.existsByIsbnAndIdNot(
                    formulario.getIsbn(), id)) {

            resultado.rejectValue(
                "isbn",
                "isbn.duplicado",
                "Ya existe otro libro con ese ISBN."
            );
        }

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
            "Libro actualizado correctamente."
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
                "Libro no encontrado."
            );
        }

        if (libroRepository.contarPrestamos(id) > 0) {
            redirectAttributes.addFlashAttribute(
                "error",
                "No se puede eliminar el libro porque tiene préstamos asociados."
            );

            return "redirect:/libros";
        }

        try {
            libroRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                "mensaje",
                "Libro eliminado correctamente."
            );
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute(
                "error",
                "No se puede eliminar el libro porque tiene datos asociados."
            );
        }

        return "redirect:/libros";
    }
}