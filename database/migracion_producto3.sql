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
        