USE biblioteca;

CREATE TABLE usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    dni VARCHAR(9) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefono VARCHAR(20),
    fecha_alta DATE NOT NULL,

    PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuarios_dni UNIQUE (dni),
    CONSTRAINT uq_usuarios_email UNIQUE (email)
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