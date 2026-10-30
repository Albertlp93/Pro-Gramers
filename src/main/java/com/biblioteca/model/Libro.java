package com.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Long id;

    @NotBlank(message = "El ISBN es obligatorio.")
    @Pattern(
        regexp = "[0-9]{13}",
        message = "El ISBN debe contener 13 dígitos, sin espacios ni guiones."
    )
    @Column(nullable = false, unique = true, length = 13)
    private String isbn;

    @NotBlank(message = "El título es obligatorio.")
    @Size(
        max = 100,
        message = "El título no puede superar los 100 caracteres."
    )
    @Column(nullable = false, length = 100)
    private String titulo;

    @Size(
        max = 100,
        message = "La editorial no puede superar los 100 caracteres."
    )
    @Column(length = 100)
    private String editorial;

    @Min(value = 1, message = "El año debe ser mayor que cero.")
    @Column(name = "anio_publicacion")
    private Integer anioPublicacion;

    @Size(
        max = 50,
        message = "El género no puede superar los 50 caracteres."
    )
    @Column(length = 50)
    private String genero;

    @NotNull(message = "Indica la cantidad de ejemplares disponibles.")
    @Min(value = 0, message = "La cantidad no puede ser negativa.")
    @Column(name = "ejemplares_disponibles", nullable = false)
    private Integer ejemplaresDisponibles = 0;

    @NotEmpty(message = "Selecciona al menos un autor.")
    @ManyToMany
    @JoinTable(
        name = "libro_autor",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    public Libro() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Integer getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Integer anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEjemplaresDisponibles() {
        return ejemplaresDisponibles;
    }

    public void setEjemplaresDisponibles(Integer ejemplaresDisponibles) {
        this.ejemplaresDisponibles = ejemplaresDisponibles;
    }

    public Set<Autor> getAutores() {
        return autores;
    }

    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }
}