package com.biblioteca.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroForm(

    @NotBlank(message = "Introduce tu DNI.")
    @Pattern(regexp = "[0-9]{8}[A-Za-z]",
             message = "El DNI debe tener ocho números y una letra.")
    String dni,

    @NotBlank(message = "Introduce tu nombre.")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
    String nombre,

    @NotBlank(message = "Introduce tus apellidos.")
    @Size(max = 150, message = "Los apellidos no pueden superar 150 caracteres.")
    String apellidos,

    @NotBlank(message = "Introduce tu correo electrónico.")
    @Email(message = "Introduce un correo electrónico válido.")
    @Size(max = 150, message = "El correo no puede superar 150 caracteres.")
    String email,

    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres.")
    String telefono,

    @NotBlank(message = "Introduce una contraseña.")
    @Size(min = 12, message = "La contraseña debe tener al menos 12 caracteres.")
    String password

) {
}