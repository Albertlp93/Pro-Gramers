package com.biblioteca.repository;

import com.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository
        extends JpaRepository<Prestamo, Long> {

    @Override
    @EntityGraph(attributePaths = {"usuario", "libro"})
    List<Prestamo> findAll();
}