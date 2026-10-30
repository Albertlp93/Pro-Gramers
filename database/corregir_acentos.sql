USE biblioteca;
SET NAMES utf8mb4;

START TRANSACTION;

UPDATE autores
SET nacionalidad = 'Española'
WHERE id_autor IN (1, 3, 7, 8);

UPDATE autores
SET apellidos = 'García Lorca'
WHERE id_autor = 3;

UPDATE autores
SET apellidos = 'Cortázar'
WHERE id_autor = 4;

UPDATE autores
SET apellidos = 'Alas «Clarín»'
WHERE id_autor = 7;

UPDATE autores
SET nombre = 'Ramón',
    apellidos = 'del Valle-Inclán'
WHERE id_autor = 8;

UPDATE usuarios
SET apellidos = 'Ruiz Gómez'
WHERE id_usuario = 3;

UPDATE usuarios
SET apellidos = 'López Navarro'
WHERE id_usuario = 5;

UPDATE usuarios
SET apellidos = 'Sánchez Rey'
WHERE id_usuario = 6;

COMMIT;