package com.biblioteca.repository;

import com.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    @Override
    @EntityGraph(attributePaths = "autores")
    List<Libro> findAll();

    @Override
    @EntityGraph(attributePaths = "autores")
    Optional<Libro> findById(Long id);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    @Query(
        value = "SELECT COUNT(*) FROM prestamos WHERE libro_id = :id",
        nativeQuery = true
    )
    long contarPrestamos(@Param("id") Long id);
}