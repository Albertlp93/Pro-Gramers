USE biblioteca;

CREATE TABLE roles (
    id_rol BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,

    PRIMARY KEY (id_rol),
    CONSTRAINT uq_roles_nombre UNIQUE (nombre)
);

CREATE TABLE usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    dni VARCHAR(9) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    fecha_alta DATE NOT NULL,
    password VARCHAR(255) NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    rol_id BIGINT NOT NULL,

    PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuarios_dni UNIQUE (dni),
    CONSTRAINT uq_usuarios_email UNIQUE (email),
    CONSTRAINT fk_usuarios_rol
        FOREIGN KEY (rol_id) REFERENCES roles (id_rol)
);

CREATE TABLE autores (
    id_autor BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    nacionalidad VARCHAR(60),

    PRIMARY KEY (id_autor)
);

CREATE TABLE libros (
    id_libro BIGINT NOT NULL AUTO_INCREMENT,
    isbn VARCHAR(13) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    editorial VARCHAR(100),
    anio_publicacion INT,
    genero VARCHAR(50),
    ejemplares_disponibles INT NOT NULL DEFAULT 0,

    PRIMARY KEY (id_libro),
    CONSTRAINT uq_libros_isbn UNIQUE (isbn),
    CONSTRAINT chk_libros_ejemplares
        CHECK (ejemplares_disponibles >= 0)
);

CREATE TABLE prestamos (
    id_prestamo BIGINT NOT NULL AUTO_INCREMENT,
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion_prevista DATE NOT NULL,
    fecha_devolucion DATE,
    estado VARCHAR(20) NOT NULL,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,

    PRIMARY KEY (id_prestamo),

    CONSTRAINT fk_prestamos_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuarios (id_usuario),

    CONSTRAINT fk_prestamos_libro
        FOREIGN KEY (libro_id)
        REFERENCES libros (id_libro),

    CONSTRAINT chk_prestamos_fecha_prevista
        CHECK (fecha_devolucion_prevista >= fecha_prestamo),

    CONSTRAINT chk_prestamos_fecha_devolucion
        CHECK (
            fecha_devolucion IS NULL
            OR fecha_devolucion >= fecha_prestamo
        )
);

CREATE TABLE libro_autor (
    libro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,

    PRIMARY KEY (libro_id, autor_id),

    CONSTRAINT fk_libro_autor_libro
        FOREIGN KEY (libro_id)
        REFERENCES libros (id_libro),

    CONSTRAINT fk_libro_autor_autor
        FOREIGN KEY (autor_id)
        REFERENCES autores (id_autor)
);

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