package com.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, unique = true, length = 9)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // Relación 1:N con Prestamo (lado inverso; el propietario es Prestamo.usuario).
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos = new ArrayList<>();

    // Relación nueva del producto 3: libros favoritos del usuario (N:M con Libro).
    @ManyToMany
    @JoinTable(
        name = "usuario_libro_favorito",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "libro_id")
    )
    private Set<Libro> favoritos = new HashSet<>();

    public Usuario() {
    }

    public boolean esAdmin() {
        return rol != null && Rol.ADMIN.equals(rol.getNombre());
    }

    public void agregarFavorito(Libro libro) {
        favoritos.add(libro);
        libro.getUsuariosFavorito().add(this);
    }

    public void quitarFavorito(Libro libro) {
        favoritos.remove(libro);
        libro.getUsuariosFavorito().remove(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public Set<Libro> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(Set<Libro> favoritos) {
        this.favoritos = favoritos;
    }
}