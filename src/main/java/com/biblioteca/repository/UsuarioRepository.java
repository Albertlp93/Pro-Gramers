package com.biblioteca.repository;

import com.biblioteca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @EntityGraph(attributePaths = "rol")
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    // Zona /user: usuario con su rol y sus libros favoritos ya cargados.
    @EntityGraph(attributePaths = {"rol", "favoritos"})
    Optional<Usuario> findConFavoritosByEmail(String email);
}
