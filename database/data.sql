-- Ejecutar una sola vez. 

USE biblioteca;
SET NAMES utf8mb4;

-- Autores de ejemplo:
INSERT INTO autores (nombre, apellidos, nacionalidad)
VALUES
    ('Miguel', 'de Cervantes Saavedra', 'Española'),
    ('Gabriel', 'García Márquez', 'Colombiana'),
    ('Federico', 'García Lorca', 'Española'),
    ('Julio', 'Cortázar', 'Argentina'),
    ('Jorge Luis', 'Borges', 'Argentina'),
    ('Adolfo', 'Bioy Casares', 'Argentina'),
    ('Leopoldo', 'Alas «Clarín»', 'Española'),
    ('Ramón', 'del Valle-Inclán', 'Española');

-- Libro de ejemplo: 
INSERT INTO libros (
    isbn,
    titulo,
    editorial,
    anio_publicacion,
    genero,
    ejemplares_disponibles
)
VALUES (
    '9788420412146',
    'Don Quijote de la Mancha',
    'Alfaguara',
    2015,
    'Novela',
    2
);

-- Asociar el libro con Cervantes:
INSERT INTO libro_autor (libro_id, autor_id)
SELECT l.id_libro, a.id_autor
FROM libros l
CROSS JOIN autores a
WHERE l.isbn = '9788420412146'
  AND a.nombre = 'Miguel'
  AND a.apellidos = 'de Cervantes Saavedra';

-- Usuarios de ejemplo:
INSERT INTO usuarios (
    dni, nombre, apellidos, email, telefono, fecha_alta
)
VALUES
    (
        '12345678Z', 'Laura', 'Martínez Soler',
        'laura.martinez@example.com', '612 345 678', '2026-09-01'
    ),
    (
        '23456789D', 'Jordi', 'Puig Ferrer',
        'jordi.puig@example.com', '634 128 905', '2026-09-01'
    ),
    (
        '34567890V', 'Ana', 'Ruiz Gómez',
        'ana.ruiz@example.com', '655 902 341', '2026-09-02'
    ),
    (
        '45678901G', 'Marc', 'Vidal Casas',
        'marc.vidal@example.com', '677 410 236', '2026-09-03'
    ),
    (
        '56789012B', 'Carmen', 'López Navarro',
        'carmen.lopez@example.com', '698 553 017', '2026-09-04'
    ),
    (
        '67890123B', 'David', 'Sánchez Rey',
        'david.sanchez@example.com', '620 774 589', '2026-09-05'
    );

-- Prestamos de ejemplo: 
START TRANSACTION;

-- Préstamo pendiente: vence dentro de 14 días.
INSERT INTO prestamos (
    fecha_prestamo,
    fecha_devolucion_prevista,
    fecha_devolucion,
    estado,
    usuario_id,
    libro_id
)
SELECT
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 14 DAY),
    NULL,
    'Pendiente',
    u.id_usuario,
    l.id_libro
FROM usuarios u
CROSS JOIN libros l
WHERE u.email = 'laura.martinez@example.com'
  AND l.isbn = '9788420412146';

-- Préstamo retrasado: venció hace 7 días.
INSERT INTO prestamos (
    fecha_prestamo,
    fecha_devolucion_prevista,
    fecha_devolucion,
    estado,
    usuario_id,
    libro_id
)
SELECT
    DATE_SUB(CURDATE(), INTERVAL 21 DAY),
    DATE_SUB(CURDATE(), INTERVAL 7 DAY),
    NULL,
    'Retrasado',
    u.id_usuario,
    l.id_libro
FROM usuarios u
CROSS JOIN libros l
WHERE u.email = 'jordi.puig@example.com'
  AND l.isbn = '9788420412146';

-- Préstamo ya devuelto.
INSERT INTO prestamos (
    fecha_prestamo,
    fecha_devolucion_prevista,
    fecha_devolucion,
    estado,
    usuario_id,
    libro_id
)
SELECT
    DATE_SUB(CURDATE(), INTERVAL 40 DAY),
    DATE_SUB(CURDATE(), INTERVAL 26 DAY),
    DATE_SUB(CURDATE(), INTERVAL 28 DAY),
    'Devuelto',
    u.id_usuario,
    l.id_libro
FROM usuarios u
CROSS JOIN libros l
WHERE u.email = 'ana.ruiz@example.com'
  AND l.isbn = '9788420412146';

-- Los dos ejemplares están actualmente prestados.
UPDATE libros
SET ejemplares_disponibles = 0
WHERE isbn = '9788420412146';

COMMIT;