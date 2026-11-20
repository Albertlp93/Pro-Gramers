package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Lógica de la relación N:M Usuario-Libro (libros favoritos del usuario).
@Service
public class FavoritoService {

    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public FavoritoService(
            UsuarioRepository usuarioRepository,
            LibroRepository libroRepository) {

        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional(readOnly = true)
    public List<Libro> listarFavoritos(String email) {
        return libroRepository.findByUsuariosFavoritoEmailOrderByTituloAsc(email);
    }

    // Ids de los favoritos, para pintar la estrella en el listado de libros.
    @Transactional(readOnly = true)
    public Set<Long> idsFavoritos(String email) {
        return listarFavoritos(email).stream()
                .map(Libro::getId)
                .collect(Collectors.toSet());
    }

    // Marca el libro como favorito si no lo era y lo quita si ya lo era.
    // Devuelve true si el libro ha quedado como favorito.
    @Transactional
    public boolean alternarFavorito(String email, Long libroId) {
        Usuario usuario = usuarioRepository.findConFavoritosByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado."));

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado."));

        if (usuario.getFavoritos().contains(libro)) {
            usuario.quitarFavorito(libro);
            return false;
        }

        usuario.agregarFavorito(libro);
        return true;
    }
}
