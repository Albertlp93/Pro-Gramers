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