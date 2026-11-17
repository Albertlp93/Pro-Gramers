package com.biblioteca.service;

import com.biblioteca.dto.RegistroForm;
import com.biblioteca.model.Rol;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.RolRepository;
import com.biblioteca.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Locale;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrar(RegistroForm formulario) {
        String email = formulario.email().strip().toLowerCase(Locale.ROOT);
        String dni = formulario.dni().strip().toUpperCase(Locale.ROOT);
        String password = formulario.password();

        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new RegistroInvalido(
                    "password", "La contraseña no puede superar 72 bytes.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new RegistroInvalido(
                    "email", "Ya existe una cuenta con ese correo.");
        }

        if (usuarioRepository.existsByDni(dni)) {
            throw new RegistroInvalido(
                    "dni", "Ya existe una cuenta con ese DNI.");
        }

        Rol rolUsuario = rolRepository.findByNombre(Rol.USER)
                .orElseThrow(() -> new IllegalStateException(
                        "El rol ROLE_USER no está configurado en la base de datos."));

        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setNombre(formulario.nombre().strip());
        usuario.setApellidos(formulario.apellidos().strip());
        usuario.setEmail(email);
        usuario.setTelefono(
                formulario.telefono() == null || formulario.telefono().isBlank()
                        ? null
                        : formulario.telefono().strip());
        usuario.setFechaAlta(LocalDate.now());
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setActivo(true);
        usuario.setRol(rolUsuario);

        usuarioRepository.save(usuario);
    }

    public static class RegistroInvalido extends RuntimeException {

        private final String campo;

        public RegistroInvalido(String campo, String mensaje) {
            super(mensaje);
            this.campo = campo;
        }

        public String getCampo() {
            return campo;
        }
    }
}