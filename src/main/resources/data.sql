--Datos para carga inicial de la base de datos

PRAGMA foreign_keys = ON;

------------------------------------------------------------
-- CONFIGURACION
------------------------------------------------------------
INSERT INTO Configuracion (id_config, clave, valor, descripcion) VALUES
(1, 'duracion_reserva_min', '60', 'Duración estándar de una reserva en minutos'),
(2, 'max_reservas_por_dia', '2', 'Número máximo de reservas por socio y día'),
(3, 'politica_cancelacion_horas', '24', 'Horas mínimas para cancelar sin penalización'),
(4, 'moneda', 'EUR', 'Moneda usada en el sistema'),
(5, 'precio_base_pista', '12.00', 'Precio base orientativo por reserva de pista (1h)'),
(6, 'precio_base_piscina', '8.00', 'Precio base orientativo por reserva de piscina (1h)'),
(7, 'limite_lista_espera', '10', 'Máximo de personas en lista de espera por actividad');

------------------------------------------------------------
-- PERIODOS GLOBALES (3) - no solapados y coherentes con los datos
------------------------------------------------------------
INSERT INTO PeriodosGlobales (id_periodo_global, nombre, fecha_inicio, fecha_fin) VALUES
(1, 'Enero',      '2026-01-01', '2026-05-31'),
(2, 'Junio',      '2026-06-01', '2026-08-31'),
(3, 'Septiembre', '2026-09-01', '2026-12-31');

------------------------------------------------------------
-- SOCIOS (17)
------------------------------------------------------------
INSERT INTO Socios (id_socio, nombre, email, contrasena, fecha_registro, debe_dinero, telefono, direccion, es_admin) VALUES
(1, 'Laura Martín', 'laura.martin@mail.com', 'hash_laura', '2025-09-10', 0, '600111222', 'C/ Alcalá 120, Madrid', 0),
(2, 'Carlos Pérez', 'carlos.perez@mail.com', 'hash_carlos', '2025-10-02', 0, '611222333', 'Av. América 45, Madrid', 0),
(3, 'Ana Gómez', 'ana.gomez@mail.com', 'hash_ana', '2025-11-15', 1, '622333444', 'C/ Goya 17, Madrid', 0),
(4, 'Miguel Ruiz', 'miguel.ruiz@mail.com', 'hash_miguel', '2026-01-05', 0, '633444555', 'C/ Serrano 200, Madrid', 0),
(5, 'Sofía López', 'sofia.lopez@mail.com', 'hash_sofia', '2026-02-01', 0, '644555666', 'C/ Princesa 8, Madrid', 0),
(6, 'Javier Ortega', 'javier.ortega@mail.com', 'hash_javier', '2025-08-21', 0, '655666777', 'C/ Atocha 30, Madrid', 0),
(7, 'Elena Navarro', 'elena.navarro@mail.com', 'hash_elena', '2025-07-13', 0, '666777888', 'C/ Bravo Murillo 91, Madrid', 0),
(8, 'Daniel Torres', 'daniel.torres@mail.com', 'hash_daniel', '2025-12-20', 0, '677888999', 'C/ Fuencarral 55, Madrid', 0),
(9, 'Paula Romero', 'paula.romero@mail.com', 'hash_paula', '2025-10-28', 0, '688999000', 'C/ Ibiza 14, Madrid', 0),
(10, 'Hugo Sánchez', 'hugo.sanchez@mail.com', 'hash_hugo', '2025-09-30', 0, '699000111', 'C/ Ferraz 40, Madrid', 0),
(11, 'Nerea Castillo', 'nerea.castillo@mail.com', 'hash_nerea', '2026-01-20', 0, '610123456', 'C/ Donnell 77, Madrid', 0),
(12, 'Alberto Molina', 'alberto.molina@mail.com', 'hash_alberto', '2025-06-18', 0, '620234567', 'C/ Toledo 12, Madrid', 0),
(13, 'Claudia Vega', 'claudia.vega@mail.com', 'hash_claudia', '2025-11-02', 0, '630345678', 'C/ Arturo Soria 210, Madrid', 0),
(14, 'Iván Ramos', 'ivan.ramos@mail.com', 'hash_ivan', '2025-12-05', 0, '640456789', 'C/ Menéndez Pelayo 60, Madrid', 0),
(15, 'Marina Gil', 'marina.gil@mail.com', 'hash_marina', '2026-02-10', 0, '650567890', 'C/ Velázquez 25, Madrid', 0),
(16, 'admin', 'admin@mail.com', '123', '2026-02-27', 0, NULL, NULL, 1),
(17, 'socio1', 'socio1@mail.com', 'socio', '2026-02-28', 0, NULL, NULL, 0);

------------------------------------------------------------
-- INSTALACIONES (8)  (incluye precioInstalacion)
------------------------------------------------------------
INSERT INTO Instalaciones (id_instalacion, nombre, tipo, capacidad, en_uso, precioInstalacion, detalles) VALUES
(1, 'Piscina Cubierta', 'piscina', 60, 1, 8.00,  'Piscina climatizada 25m, calles disponibles'),
(2, 'Pista de Tenis 1', 'tenis', 4, 1, 12.00, 'Pista rápida, iluminación nocturna'),
(3, 'Pista de Pádel 1', 'padel', 4, 1, 16.00, 'Césped artificial, muro de vidrio'),
(4, 'Campo Fútbol 7', 'futbol', 14, 1, 35.00, 'Césped artificial, vestuarios cercanos'),
(5, 'Pista de Tenis 2', 'tenis', 4, 1, 12.00, 'Pista de tierra batida'),
(6, 'Pista de Pádel 2', 'padel', 4, 1, 16.00, 'Iluminación LED, zona de gradas'),
(7, 'Zona Multiusos', 'otro', 30, 1, 10.00, 'Sala para clases dirigidas (yoga, pilates)'),
(8, 'Piscina Exterior', 'piscina', 80, 0, 8.00, 'Cerrada por temporada (invierno)');

------------------------------------------------------------
-- HORARIOS INSTALACION (invierno/verano/otoño) - TODO EN PUNTO
------------------------------------------------------------
INSERT INTO HorariosInstalacion (id_horario, id_instalacion, tipo_horario, hora_apertura, hora_cierre) VALUES
(1, 1, 'invierno', '07:00', '22:00'),
(2, 2, 'invierno', '08:00', '21:00'),
(3, 3, 'invierno', '08:00', '22:00'),
(4, 4, 'invierno', '09:00', '23:00'),
(5, 5, 'invierno', '08:00', '21:00'),
(6, 6, 'invierno', '08:00', '22:00'),
(7, 7, 'invierno', '09:00', '22:00'),
(8, 8, 'invierno', '00:00', '00:00'),

(9, 1, 'verano', '07:00', '23:00'),
(10, 2, 'verano', '08:00', '22:00'),
(11, 3, 'verano', '08:00', '23:00'),
(12, 4, 'verano', '09:00', '00:00'),
(13, 5, 'verano', '08:00', '22:00'),
(14, 6, 'verano', '08:00', '23:00'),
(15, 7, 'verano', '09:00', '22:00'),
(16, 8, 'verano', '09:00', '21:00'),

(17, 1, 'otoño', '08:00', '23:00'),
(18, 2, 'otoño', '08:00', '22:00'),
(19, 3, 'otoño', '08:00', '23:00'),
(20, 4, 'otoño', '09:00', '23:00'),
(21, 5, 'otoño', '08:00', '22:00'),
(22, 6, 'otoño', '08:00', '23:00'),
(23, 7, 'otoño', '09:00', '22:00'),
(24, 8, 'otoño', '00:00', '00:00');

------------------------------------------------------------
-- PERIODOS INSCRIPCION (6)
------------------------------------------------------------
INSERT INTO PeriodosInscripcion (id_periodo, nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_noSocio) VALUES
(1, 'Socios Feb-Mar', 'Inscripción para (Aquagym).',        '2026-03-20', '2026-03-28', '2026-04-05'),
(2, 'Socios Primavera Tenis', 'Inscripción para (Tenis).',  '2026-03-22', '2026-03-30', '2026-04-08'),
(3, 'Liga Padel', 'Inscripción para (Liga Pádel).',         '2026-03-10', '2026-03-20', '2026-03-28'),
(4, 'Clase Pilates', 'Inscripción (Pilates).',              '2026-03-12', '2026-03-24', '2026-03-31'),
(5, 'Sesiones Yoga', 'Inscripción (Yoga).',                 '2026-03-01', '2026-03-10', '2026-03-18'),
(6, 'Entreno Fútbol', 'Inscripción para (Tecnificación).',  '2026-03-28', '2026-04-05', '2026-04-12');

------------------------------------------------------------
-- ACTIVIDADES (9)
------------------------------------------------------------
INSERT INTO Actividades (id_actividad, nombre, descripcion, id_instalacion, aforo, costo_socio, costo_no_socio, fecha_inicio, fecha_fin, id_periodo, edicion, estado, motivo_cancelacion) VALUES
(1, 'Aquagym',              'Clase dirigida en piscina (nivel básico).',        1, 20, 15.00, 25.00, '2026-02-01', '2026-03-31', 1, 1, 'activa', NULL),
(2, 'Escuela de Tenis',     'Iniciación y técnica para adultos.',               2,  8, 20.00, 35.00, '2026-02-15', '2026-05-15', 2, 1, 'activa', NULL),
(3, 'Liga de Pádel',        'Partidos semanales por parejas.',                  6, 16, 10.00, 18.00, '2026-03-01', '2026-04-30', 3, 1, 'activa', NULL),
(4, 'Pilates',              'Fortalecimiento y movilidad (nivel intermedio).',  7, 18, 12.00, 20.00, '2026-02-10', '2026-04-10', 4, 1, 'activa', NULL),
(5, 'Yoga',                 'Yoga suave para todos los niveles.',               3, 22, 10.00, 18.00, '2026-02-05', '2026-04-05', 5, 1, 'activa', NULL),
(6, 'Clase de Padel',       'Clase Padel principiantes.',                       4, 14, 18.00, 30.00, '2026-08-20', '2026-12-20', 6, 1, 'activa', NULL),
(7, 'Escuela Fútbol',       'Entrenamiento fútbol para niños.',                 4, 23, 15.00, 25.00, '2026-10-20', '2026-11-20', 6, 1, 'activa', NULL),
(8, 'Cursillos Natación',   'Cursillos de natación.',                           3, 14, 12.00, 16.00, '2026-02-20', '2026-05-20', 6, 1, 'activa', NULL),
(9, 'Tecnificación Fútbol', 'Entrenamiento técnico veraniego.',                 4, 14, 20.00, 25.00, '2026-06-20', '2026-08-20', 6, 1, 'activa', NULL);
------------------------------------------------------------
-- SESIONES ACTIVIDAD (20) - TODO EN PUNTO
------------------------------------------------------------
INSERT INTO SesionesActividad (id_sesion, id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) VALUES
(1, 1, '2026-02-03', '18:00', '19:00', 1),
(2, 1, '2026-02-10', '18:00', '19:00', 1),
(3, 1, '2026-02-17', '18:00', '19:00', 1),
(4, 1, '2026-02-24', '18:00', '19:00', 1),
(5, 1, '2026-03-03', '18:00', '19:00', 1),

(6, 2, '2026-02-17', '19:00', '21:00', 2),
(7, 2, '2026-02-24', '19:00', '21:00', 2),
(8, 2, '2026-03-03', '19:00', '21:00', 5),
(9, 2, '2026-03-10', '19:00', '21:00', 2),

(10, 3, '2026-03-05', '20:00', '22:00', 6),
(11, 3, '2026-03-12', '20:00', '22:00', 6),
(12, 3, '2026-03-19', '20:00', '22:00', 3),

(13, 4, '2026-02-12', '18:00', '19:00', 7),
(14, 4, '2026-02-19', '18:00', '19:00', 7),
(15, 4, '2026-02-26', '18:00', '19:00', 7),

(16, 5, '2026-02-06', '19:00', '20:00', 7),
(17, 5, '2026-02-13', '19:00', '20:00', 7),
(18, 5, '2026-02-20', '19:00', '20:00', 7),

(19, 6, '2026-02-27', '17:00', '19:00', 4),
(20, 6, '2026-03-06', '17:00', '19:00', 4);

------------------------------------------------------------
-- PLANIFICACION ACTIVIDADES (10) - TODO EN PUNTO
------------------------------------------------------------
INSERT INTO PlanificacionActividades (id_planificacion, id_actividad, fecha, hora_inicio, hora_fin, id_instalacion) VALUES
(1, 1, '2026-03-10', '18:00', '19:00', 1),
(2, 1, '2026-03-17', '18:00', '19:00', 1),

(3, 2, '2026-03-17', '19:00', '21:00', 2),
(4, 2, '2026-03-24', '19:00', '21:00', 5),

(5, 3, '2026-03-26', '20:00', '22:00', 6),
(6, 3, '2026-04-02', '20:00', '22:00', 3),

(7, 4, '2026-03-05', '18:00', '19:00', 7),
(8, 4, '2026-03-12', '18:00', '19:00', 7),

(9, 6, '2026-03-13', '17:00', '19:00', 4),
(10, 6, '2026-03-20', '17:00', '19:00', 4);

------------------------------------------------------------
-- RESERVAS (30) - TODO EN PUNTO
------------------------------------------------------------
INSERT INTO Reservas (id_reserva, id_socio, id_instalacion, fecha_hora_inicio, duracion, costo, pagado, estado, motivo_cancelacion) VALUES
(1, 1, 2, '2026-02-10 18:00', 60, 12.00, 1, 'completada', NULL),
(2, 2, 6, '2026-02-12 20:00', 90, 18.00, 1, 'activa', NULL),
(3, 3, 1, '2026-02-14 10:00', 60, 8.00, 1, 'cancelada', 'Lesión - cancelación con antelación'),
(4, 4, 4, '2026-02-16 21:00', 90, 35.00, 0, 'activa', NULL),
(5, 5, 3, '2026-02-18 19:00', 60, 16.00, 1, 'completada', NULL),
(6, 6, 5, '2026-02-19 20:00', 60, 12.00, 1, 'completada', NULL),
(7, 7, 2, '2026-02-20 18:00', 90, 18.00, 0, 'activa', NULL),
(8, 8, 6, '2026-02-21 10:00', 60, 16.00, 1, 'completada', NULL),
(9, 9, 1, '2026-02-21 12:00', 60, 8.00, 1, 'reembolsada', 'Cierre puntual por mantenimiento'),
(10, 10, 3, '2026-02-22 11:00', 90, 24.00, 1, 'completada', NULL),

(11, 11, 2, '2026-02-23 19:00', 60, 12.00, 1, 'completada', NULL),
(12, 12, 5, '2026-02-24 20:00', 90, 18.00, 1, 'activa', NULL),
(13, 13, 6, '2026-02-25 21:00', 60, 16.00, 0, 'activa', NULL),
(14, 14, 1, '2026-02-26 07:00', 60, 8.00, 1, 'completada', NULL),
(15, 15, 4, '2026-02-27 20:00', 90, 35.00, 1, 'cancelada', 'Lluvia intensa (cambio de horario no aceptado)'),

(16, 1, 3, '2026-02-28 12:00', 60, 16.00, 1, 'completada', NULL),
(17, 2, 2, '2026-03-01 10:00', 60, 12.00, 1, 'completada', NULL),
(18, 3, 5, '2026-03-01 11:00', 60, 12.00, 0, 'activa', NULL),
(19, 4, 6, '2026-03-02 20:00', 90, 18.00, 1, 'completada', NULL),
(20, 5, 1, '2026-03-03 09:00', 60, 8.00, 1, 'completada', NULL),

(21, 6, 2, '2026-03-04 19:00', 60, 12.00, 1, 'completada', NULL),
(22, 7, 3, '2026-03-05 20:00', 60, 16.00, 0, 'activa', NULL),
(23, 8, 4, '2026-03-06 21:00', 90, 35.00, 1, 'completada', NULL),
(24, 9, 6, '2026-03-07 10:00', 60, 16.00, 1, 'cancelada', 'Problema personal'),
(25, 10, 5, '2026-03-08 11:00', 60, 12.00, 1, 'reembolsada', 'Instalación no disponible'),

(26, 11, 2, '2026-03-09 18:00', 60, 12.00, 1, 'completada', NULL),
(27, 12, 1, '2026-03-10 07:00', 60, 8.00, 1, 'completada', NULL),
(28, 13, 3, '2026-03-10 20:00', 90, 24.00, 1, 'activa', NULL),
(29, 14, 6, '2026-03-11 21:00', 60, 16.00, 0, 'activa', NULL),
(30, 15, 5, '2026-03-12 19:00', 60, 12.00, 1, 'completada', NULL),

--Mas casos para pruebas
(31, 2, 2, '2026-03-04 10:00', 60, 12.00, 1, 'completada', NULL),
(32, 9, 2, '2026-03-04 12:00', 60, 12.00, 1, 'completada', NULL),
(33, 13, 2, '2026-03-04 20:00', 60, 12.00, 0, 'activa', NULL),
(34, 5, 1, '2026-03-04 08:00', 60, 8.00, 1, 'completada', NULL),
(35, 11, 1, '2026-03-04 11:00', 60, 8.00, 1, 'completada', NULL),
(36, 8, 6, '2026-03-04 21:00', 60, 16.00, 1, 'activa', NULL),
(37, 1, 3, '2026-03-05 19:00', 60, 16.00, 1, 'activa', NULL),
(38, 10, 3, '2026-03-05 21:00', 60, 16.00, 0, 'activa', NULL),
(39, 14, 7, '2026-03-05 10:00', 60, 10.00, 1, 'completada', NULL),
(40, 15, 7, '2026-03-05 17:00', 60, 10.00, 1, 'activa', NULL),
(41, 6, 4, '2026-03-06 19:00', 60, 35.00, 1, 'activa', NULL),
(42, 4, 4, '2026-03-06 22:00', 60, 35.00, 0, 'activa', NULL),
(43, 12, 5, '2026-03-06 10:00', 60, 12.00, 1, 'completada', NULL),
(44, 3, 6, '2026-03-07 11:00', 60, 16.00, 1, 'completada', NULL),
(45, 7, 6, '2026-03-07 12:00', 60, 16.00, 1, 'completada', NULL),
(46, 9, 1, '2026-03-07 13:00', 60, 8.00, 1, 'completada', NULL),
(47, 13, 5, '2026-03-08 12:00', 60, 12.00, 1, 'activa', NULL),
(48, 14, 5, '2026-03-08 19:00', 60, 12.00, 1, 'activa', NULL),
(49, 11, 3, '2026-03-08 10:00', 60, 16.00, 1, 'completada', NULL),
(50, 15, 2, '2026-03-09 10:00', 60, 12.00, 1, 'completada', NULL),
(51, 8, 2, '2026-03-09 20:00', 60, 12.00, 0, 'activa', NULL),
(52, 4, 1, '2026-03-09 08:00', 60, 8.00, 1, 'completada', NULL),
(53, 7, 1, '2026-03-10 09:00', 60, 8.00, 1, 'completada', NULL),
(54, 12, 1, '2026-03-10 12:00', 60, 8.00, 1, 'activa', NULL),
(55, 6, 3, '2026-03-10 19:00', 60, 16.00, 1, 'activa', NULL),
(56, 10, 3, '2026-03-10 21:00', 60, 16.00, 1, 'activa', NULL),
(57, 12, 1, '2026-03-30 12:00', 60, 8.00, 1, 'activa', NULL),
(58, 6, 3, '2026-04-10 19:00', 60, 16.00, 1, 'activa', NULL),
(59, 10, 3, '2026-04-12 21:00', 60, 16.00, 1, 'activa', NULL),
(60, 10, 3, '2026-03-23 19:00', 60, 16.00, 1, 'activa', NULL); 

------------------------------------------------------------
-- INSCRIPCIONES (25) - TODO EN PUNTO
------------------------------------------------------------
INSERT INTO Inscripciones (id_inscripcion, id_actividad, id_socio, nombre_no_socio, dni, fecha_inscripcion, estado, pagado, tipo) VALUES
(1, 1, 1, NULL, NULL, '2026-01-22 12:00', 'admitido', 1, 'socio'),
(2, 1, 2, NULL, NULL, '2026-01-23 10:00', 'admitido', 1, 'socio'),
(3, 1, 3, NULL, NULL, '2026-01-24 16:00', 'lista_espera', 0, 'socio'),
(4, 1, 4, NULL, NULL, '2026-01-25 09:00', 'admitido', 1, 'socio'),
(5, 1, 5, NULL, NULL, '2026-01-25 11:00', 'admitido', 0, 'socio'),

(6, 2, 6, NULL, NULL, '2026-02-06 09:00', 'admitido', 0, 'socio'),
(7, 2, 7, NULL, NULL, '2026-02-07 12:00', 'lista_espera', 0, 'socio'),
(8, 2, 8, NULL, NULL, '2026-02-08 18:00', 'admitido', 1, 'socio'),
(9, 2, 9, NULL, NULL, '2026-02-09 13:00', 'admitido', 1, 'socio'),

(10, 3, 10, NULL, NULL, '2026-02-25 16:00', 'admitido', 0, 'socio'),
(11, 3, 11, NULL, NULL, '2026-02-26 09:00', 'admitido', 1, 'socio'),
(12, 3, 12, NULL, NULL, '2026-02-26 20:00', 'lista_espera', 0, 'socio'),

(13, 4, 13, NULL, NULL, '2026-02-03 10:00', 'admitido', 1, 'socio'),
(14, 4, 14, NULL, NULL, '2026-02-04 11:00', 'admitido', 1, 'socio'),
(15, 4, 15, NULL, NULL, '2026-02-05 18:00', 'lista_espera', 0, 'socio'),

(16, 5, 1, NULL, NULL, '2026-01-29 19:00', 'admitido', 1, 'socio'),
(17, 5, 2, NULL, NULL, '2026-01-30 09:00', 'admitido', 0, 'socio'),

(18, 6, 3, NULL, NULL, '2026-02-10 08:00', 'admitido', 0, 'socio'),
(19, 6, 4, NULL, NULL, '2026-02-12 12:00', 'admitido', 1, 'socio'),

-- No socios (con DNI)
(20, 1, NULL, 'Javier Santos', '66778899P', '2026-01-27 18:00', 'admitido', 1, 'no_socio'),
(21, 1, NULL, 'Marta Díaz', '77889900Q', '2026-01-28 11:00', 'admitido', 1, 'no_socio'),
(22, 2, NULL, 'Lucía Herrera', '88990011R', '2026-02-10 10:00', 'admitido', 1, 'no_socio'),
(23, 4, NULL, 'Óscar Núñez', '99001122S', '2026-02-08 09:00', 'lista_espera', 0, 'no_socio'),
(24, 5, NULL, 'Paula Rivas', '10111213T', '2026-02-02 20:00', 'admitido', 1, 'no_socio'),
(25, 6, NULL, 'Diego Torres', '12131415U', '2026-02-18 17:00', 'admitido', 1, 'no_socio');

------------------------------------------------------------
-- RECIBOS (24)
------------------------------------------------------------
INSERT INTO Recibos (id_recibo, id_socio, mes, anho, total, pagado, fecha_emision, fecha_vencimiento) VALUES
(1, 1, 1, 2026, 45.00, 1, '2026-01-01', '2026-01-10'),
(2, 2, 1, 2026, 30.00, 1, '2026-01-01', '2026-01-10'),
(3, 3, 1, 2026, 55.00, 0, '2026-01-01', '2026-01-10'),
(4, 4, 1, 2026, 40.00, 1, '2026-01-01', '2026-01-10'),
(5, 5, 1, 2026, 35.00, 1, '2026-01-01', '2026-01-10'),
(6, 6, 1, 2026, 30.00, 1, '2026-01-01', '2026-01-10'),
(7, 7, 1, 2026, 30.00, 1, '2026-01-01', '2026-01-10'),
(8, 8, 1, 2026, 35.00, 1, '2026-01-01', '2026-01-10'),

(9, 1, 2, 2026, 45.00, 1, '2026-02-01', '2026-02-10'),
(10, 2, 2, 2026, 30.00, 0, '2026-02-01', '2026-02-10'),
(11, 3, 2, 2026, 55.00, 0, '2026-02-01', '2026-02-10'),
(12, 4, 2, 2026, 40.00, 1, '2026-02-01', '2026-02-10'),
(13, 5, 2, 2026, 35.00, 1, '2026-02-01', '2026-02-10'),
(14, 6, 2, 2026, 30.00, 1, '2026-02-01', '2026-02-10'),
(15, 7, 2, 2026, 30.00, 0, '2026-02-01', '2026-02-10'),
(16, 8, 2, 2026, 35.00, 1, '2026-02-01', '2026-02-10'),

(17, 1, 3, 2026, 45.00, 0, '2026-03-01', '2026-03-10'),
(18, 2, 3, 2026, 30.00, 0, '2026-03-01', '2026-03-10'),
(19, 3, 3, 2026, 55.00, 0, '2026-03-01', '2026-03-10'),
(20, 4, 3, 2026, 40.00, 0, '2026-03-01', '2026-03-10'),
(21, 5, 3, 2026, 35.00, 0, '2026-03-01', '2026-03-10'),
(22, 6, 3, 2026, 30.00, 0, '2026-03-01', '2026-03-10'),
(23, 7, 3, 2026, 30.00, 0, '2026-03-01', '2026-03-10'),
(24, 8, 3, 2026, 35.00, 0, '2026-03-01', '2026-03-10');

------------------------------------------------------------
-- PAGOS (35)
------------------------------------------------------------
INSERT INTO Pagos (id_pago, monto, fecha_pago, metodo, id_recibo, id_reserva, id_inscripcion) VALUES
(1, 45.00, '2026-01-05', 'recibo', 1, NULL, NULL),
(2, 30.00, '2026-01-06', 'recibo', 2, NULL, NULL),
(3, 40.00, '2026-01-07', 'recibo', 4, NULL, NULL),
(4, 35.00, '2026-01-08', 'recibo', 5, NULL, NULL),
(5, 30.00, '2026-01-08', 'recibo', 6, NULL, NULL),
(6, 30.00, '2026-01-09', 'recibo', 7, NULL, NULL),
(7, 35.00, '2026-01-09', 'recibo', 8, NULL, NULL),

(8, 45.00, '2026-02-05', 'recibo', 9, NULL, NULL),
(9, 40.00, '2026-02-06', 'recibo', 12, NULL, NULL),
(10, 35.00, '2026-02-06', 'recibo', 13, NULL, NULL),
(11, 30.00, '2026-02-07', 'recibo', 14, NULL, NULL),
(12, 35.00, '2026-02-08', 'recibo', 16, NULL, NULL),

(13, 12.00, '2026-02-10', 'tarjeta', NULL, 1, NULL),
(14, 18.00, '2026-02-12', 'tarjeta', NULL, 2, NULL),
(15, 16.00, '2026-02-18', 'tarjeta', NULL, 5, NULL),
(16, 12.00, '2026-02-19', 'tarjeta', NULL, 6, NULL),
(17, 16.00, '2026-02-21', 'tarjeta', NULL, 8, NULL),
(18, 8.00,  '2026-02-26', 'tarjeta', NULL, 14, NULL),
(19, 16.00, '2026-02-28', 'tarjeta', NULL, 16, NULL),
(20, 12.00, '2026-03-01', 'tarjeta', NULL, 17, NULL),
(21, 18.00, '2026-03-02', 'tarjeta', NULL, 19, NULL),
(22, 8.00,  '2026-03-03', 'tarjeta', NULL, 20, NULL),
(23, 12.00, '2026-03-04', 'tarjeta', NULL, 21, NULL),
(24, 35.00, '2026-03-06', 'tarjeta', NULL, 23, NULL),
(25, 12.00, '2026-03-09', 'tarjeta', NULL, 26, NULL),
(26, 8.00,  '2026-03-10', 'tarjeta', NULL, 27, NULL),

(27, 15.00, '2026-01-22', 'tarjeta', NULL, NULL, 1),
(28, 15.00, '2026-01-23', 'tarjeta', NULL, NULL, 2),
(29, 12.00, '2026-02-03', 'tarjeta', NULL, NULL, 13),
(30, 10.00, '2026-01-29', 'tarjeta', NULL, NULL, 16),
(31, 25.00, '2026-01-27', 'tarjeta', NULL, NULL, 20),
(32, 25.00, '2026-01-28', 'tarjeta', NULL, NULL, 21),
(33, 35.00, '2026-02-10', 'tarjeta', NULL, NULL, 22),
(34, 20.00, '2026-02-02', 'tarjeta', NULL, NULL, 24),
(35, 30.00, '2026-02-18', 'tarjeta', NULL, NULL, 25);

------------------------------------------------------------
-- REDUCCION (8)
------------------------------------------------------------
INSERT INTO Reduccion (id_credito, id_socio, nombre_no_socio, monto, fecha_generacion, fecha_aplicacion, descripcion) VALUES
(1, 3, NULL, 8.00, '2026-02-14', NULL, 'Crédito por cancelación de reserva #3 (pago previo)'),
(2, NULL, 'Javier Santos', 10.00, '2026-02-02', '2026-02-05', 'Compensación por cambio de horario (Aquagym)'),
(3, 9, NULL, 16.00, '2026-02-21', '2026-02-25', 'Crédito por reembolso de reserva #9 (cierre mantenimiento)'),
(4, 15, NULL, 35.00, '2026-02-27', NULL, 'Crédito por cancelación de reserva #15'),
(5, 10, NULL, 12.00, '2026-03-08', NULL, 'Crédito por reembolso de reserva #25 (instalación no disponible)'),
(6, NULL, 'Óscar Núñez', 12.00, '2026-02-09', NULL, 'Crédito por no admisión (lista de espera)'),
(7, 7, NULL, 18.00, '2026-02-20', '2026-03-01', 'Crédito por incidencia en pista (pádel)'),
(8, 2, NULL, 6.00, '2026-02-11', NULL, 'Ajuste por error de cobro (diferencia)');




------------------------------------------------------------
-- CASOS EXTRA HU 34013 - RESACT
------------------------------------------------------------

INSERT INTO Actividades
(id_actividad, nombre, descripcion, id_instalacion, aforo, costo_socio, costo_no_socio, fecha_inicio, fecha_fin, id_periodo, edicion, estado, motivo_cancelacion)
VALUES
(101, 'Prueba1resact', 'Actividad de prueba con conflicto solo con otra actividad.', 7, 18, 12.00, 20.00, '2026-03-10', '2026-03-20', 4, 1, 'activa', NULL),
(102, 'Prueba2resact', 'Actividad de prueba con conflicto con actividad y con reserva de socio.', 7, 18, 12.00, 20.00, '2026-03-01', '2026-03-15', 4, 1, 'activa', NULL);

INSERT INTO SesionesActividad
(id_sesion, id_actividad, fecha, hora_inicio, hora_fin, id_instalacion)
VALUES
(101, 101, '2026-03-12', '18:00', '19:00', 7),
(102, 102, '2026-03-05', '17:00', '19:00', 7);



------------------------------------------------------------
-- PRUEBAS DE PABLO -
------------------------------------------------------------
-- 1. Crear un periodo de inscripción que esté ABIERTO HOY (17 de abril de 2026)
INSERT INTO PeriodosInscripcion 
(id_periodo, nombre, descripcion, fecha_inicio_socio, fecha_fin_socio, fecha_fin_noSocio) 
VALUES
(7, 'Periodo Pruebas Abril', 'Inscripción abierta para pruebas de UI.', '2026-04-15', '2026-04-20', '2026-04-25');

-- 2. Crear la actividad vinculada a ese periodo con AFORO = 2
INSERT INTO Actividades 
(id_actividad, nombre, descripcion, id_instalacion, aforo, costo_socio, costo_no_socio, fecha_inicio, fecha_fin, id_periodo, edicion, estado, motivo_cancelacion) 
VALUES
(103, 'Test Aforo JTable', 'Actividad para probar la lista de espera.', 7, 2, 10.00, 15.00, '2026-04-25', '2026-05-31', 7, 1, 'activa', NULL);

-- 3. Llenar el aforo (2 personas) para que la actividad ya figure como "llena" en tu sistema
INSERT INTO Inscripciones 
(id_inscripcion, id_actividad, id_socio, nombre_no_socio, dni, fecha_inscripcion, estado, pagado, tipo) 
VALUES
(50, 103, 1, NULL, NULL, '2026-04-15 09:00', 'admitido', 1, 'socio'),
(51, 103, 2, NULL, NULL, '2026-04-15 10:00', 'admitido', 1, 'socio');

-- 4. Generar datos en la tabla ListaEspera para que tu vista los muestre
INSERT INTO ListaEspera 
(id_actividad, id_socio, dni_no_socio, nombre, fecha_inscripcion) 
VALUES
(103, 3, NULL, 'Ana Gómez', '2026-04-16 11:00'),
(103, 4, NULL, 'Miguel Ruiz', '2026-04-16 12:30'),
(103, NULL, '11223344X', 'Visitante Prueba', '2026-04-16 17:45');