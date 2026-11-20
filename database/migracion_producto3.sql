USE biblioteca;

CREATE TABLE roles (
    id_rol BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,

    PRIMARY KEY (id_rol),
    CONSTRAINT uq_roles_nombre UNIQUE (nombre)
);

INSERT INTO roles (nombre)
VALUES ('ROLE_ADMIN'), ('ROLE_USER');

ALTER TABLE usuarios
    ADD COLUMN password VARCHAR(255) NULL,
    ADD COLUMN activo BIT(1) NOT NULL DEFAULT b'0',
    ADD COLUMN rol_id BIGINT NULL;

UPDATE usuarios
SET password = '!',
    rol_id = (SELECT id_rol FROM roles WHERE nombre = 'ROLE_USER');

ALTER TABLE usuarios
    MODIFY COLUMN password VARCHAR(255) NOT NULL,
    MODIFY COLUMN activo BIT(1) NOT NULL DEFAULT b'1',
    MODIFY COLUMN rol_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_usuarios_rol
        FOREIGN KEY (rol_id) REFERENCES roles (id_rol);

-- Producto 3: libros favoritos de cada usuario (N:M Usuario-Libro).
-- ON DELETE CASCADE: al borrar un libro o un usuario se borran sus favoritos.
CREATE TABLE usuario_libro_favorito (
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,

    PRIMARY KEY (usuario_id, libro_id),

    CONSTRAINT fk_favorito_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id_usuario)
        ON DELETE CASCADE,

    CONSTRAINT fk_favorito_libro
        FOREIGN KEY (libro_id)
        REFERENCES libros (id_libro)
        ON DELETE CASCADE
);

-- Administrador de la biblioteca (contraseña de demostración: Admin2026Biblio).
INSERT INTO usuarios (
    dni, nombre, apellidos, email, telefono, fecha_alta, password, activo, rol_id
)
SELECT
    '00000000T', 'Admin', 'Biblioteca', 'admin@biblioteca.com', NULL, CURDATE(),
    '$2a$10$ph.xPpuRDhBVGVu6c/3yFO0a6iBz18PxLZvJWCGHLkp8DLUzdKDia',
    b'1', id_rol
FROM roles
WHERE nombre = 'ROLE_ADMIN';
