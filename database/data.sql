USE biblioteca;
-- Ejecutar una sola vez. 

-- Usuarios de ejemplo:
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