--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
--(en este caso en cada aplicacion se usa solo una tabla, por lo que no hace falta)

--Para giis.demo.tkrun:
--drop table Carreras;
--create table Carreras (id int primary key not null, inicio date not null, fin date not null, fecha date not null, descr varchar(32), check(inicio<=fin), check(fin<fecha));

PRAGMA foreign_keys = OFF;

------------------------------------------------------------
-- DROP: de DETALLE -> MAESTRO (hijas -> padres)
------------------------------------------------------------
DROP TABLE IF EXISTS Pagos;
DROP TABLE IF EXISTS Reduccion;
DROP TABLE IF EXISTS Inscripciones;
DROP TABLE IF EXISTS SesionesActividad;
DROP TABLE IF EXISTS PeriodosInscripcion;
DROP TABLE IF EXISTS Reservas;
DROP TABLE IF EXISTS PlanificacionActividades;

DROP TABLE IF EXISTS Actividades;
DROP TABLE IF EXISTS HorariosInstalacion;

DROP TABLE IF EXISTS Instalaciones;
DROP TABLE IF EXISTS Recibos;
DROP TABLE IF EXISTS Socios;
DROP TABLE IF EXISTS PeriodosGlobales;
DROP TABLE IF EXISTS Configuracion;

PRAGMA foreign_keys = ON;


-- Tabla de configuración (los parámetros ajustables)
CREATE TABLE Configuracion (
    id_config INTEGER PRIMARY KEY AUTOINCREMENT,
    clave TEXT UNIQUE NOT NULL,
    valor TEXT NOT NULL,
    descripcion TEXT
);

-- Tabla de Lista de Espera
CREATE TABLE ListaEspera (
    id_lista_espera INTEGER PRIMARY KEY AUTOINCREMENT,
    id_actividad INTEGER NOT NULL,
    id_socio INTEGER,           
    dni_no_socio TEXT,          
    nombre TEXT NOT NULL,
    fecha_inscripcion DATETIME NOT NULL,
    FOREIGN KEY (id_actividad) REFERENCES Actividades(id_actividad),
    FOREIGN KEY (id_socio) REFERENCES Socios(id_socio)
);

-- Tabla de periodos globales (trimestrales, no solapados)
CREATE TABLE PeriodosGlobales (
    id_periodo_global INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    CHECK (fecha_inicio <= fecha_fin)
);

-- Tabla de socios
CREATE TABLE Socios (
    id_socio INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    contrasena TEXT NOT NULL,
    fecha_registro DATE NOT NULL,
    debe_dinero BOOLEAN DEFAULT 0,
    telefono TEXT,
    direccion TEXT,
    es_admin BOOLEAN DEFAULT 0
);

-- Tabla de Instalaciones
CREATE TABLE Instalaciones (
    id_instalacion INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    tipo TEXT NOT NULL CHECK(tipo IN ('piscina', 'tenis', 'padel', 'futbol', 'otro')),
    capacidad INTEGER,
    en_uso BOOLEAN DEFAULT 1,
    precioInstalacion DECIMAL(10,2) NOT NULL,
    detalles TEXT
);

-- Tabla de horarios de instalaciones
CREATE TABLE HorariosInstalacion (
    id_horario INTEGER PRIMARY KEY AUTOINCREMENT,
    id_instalacion INTEGER NOT NULL,
    tipo_horario TEXT NOT NULL CHECK(tipo_horario IN ('verano', 'otoño', 'invierno')),
    hora_apertura TIME NOT NULL,
    hora_cierre TIME NOT NULL,
    FOREIGN KEY (id_instalacion) REFERENCES Instalaciones(id_instalacion)
);

-- Tabla de reservas
CREATE TABLE Reservas (
    id_reserva INTEGER PRIMARY KEY AUTOINCREMENT,
    id_socio INTEGER NOT NULL,
    id_instalacion INTEGER NOT NULL,
    fecha_hora_inicio DATETIME NOT NULL,
    duracion INTEGER NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    pagado BOOLEAN DEFAULT 0,
    estado TEXT DEFAULT 'activa' CHECK(estado IN ('activa', 'cancelada', 'completada', 'reembolsada')),
    motivo_cancelacion TEXT,
    FOREIGN KEY (id_socio) REFERENCES Socios(id_socio),
    FOREIGN KEY (id_instalacion) REFERENCES Instalaciones(id_instalacion)
);

-- Tabla de periodos de inscripción
CREATE TABLE PeriodosInscripcion (
    id_periodo INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_inicio_socio DATE NOT NULL,
    fecha_fin_socio DATE NOT NULL,
    fecha_fin_noSocio DATE NOT NULL
);

-- Tabla de actividades
CREATE TABLE Actividades (
    id_actividad INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT,
    id_instalacion INTEGER NOT NULL,
    aforo INTEGER NOT NULL,
    costo_socio DECIMAL(10,2) NOT NULL,
    costo_no_socio DECIMAL(10,2) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    id_periodo INTEGER NOT NULL,
    FOREIGN KEY (id_instalacion) REFERENCES Instalaciones(id_instalacion),
    FOREIGN KEY (id_periodo) REFERENCES PeriodosInscripcion(id_periodo)
);

-- Tabla de sesiones de actividades
CREATE TABLE SesionesActividad (
    id_sesion INTEGER PRIMARY KEY AUTOINCREMENT,
    id_actividad INTEGER NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    id_instalacion INTEGER NOT NULL,
    FOREIGN KEY (id_actividad) REFERENCES Actividades(id_actividad),
    FOREIGN KEY (id_instalacion) REFERENCES Instalaciones(id_instalacion)
);
---- Tabla planficacion de actividades
CREATE TABLE PlanificacionActividades (
    id_planificacion INTEGER PRIMARY KEY AUTOINCREMENT,
    id_actividad INTEGER NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    id_instalacion INTEGER NOT NULL,
    FOREIGN KEY (id_actividad) REFERENCES Actividades(id_actividad),
    FOREIGN KEY (id_instalacion) REFERENCES Instalaciones(id_instalacion)
);

-- Tabla de inscripciones a actividades
CREATE TABLE Inscripciones (
    id_inscripcion INTEGER PRIMARY KEY AUTOINCREMENT,
    id_actividad INTEGER NOT NULL,
    id_socio INTEGER, 
    nombre_no_socio TEXT, 
    dni TEXT,
    fecha_inscripcion DATETIME NOT NULL,
    estado TEXT NOT NULL CHECK(estado IN ('admitido', 'lista_espera')),
    pagado BOOLEAN DEFAULT 0,
    tipo TEXT NOT NULL CHECK(tipo IN ('socio', 'no_socio')),
    FOREIGN KEY (id_actividad) REFERENCES Actividades(id_actividad),
    FOREIGN KEY (id_socio) REFERENCES Socios(id_socio)
);

-- Tabla de recibos
CREATE TABLE Recibos (
    id_recibo INTEGER PRIMARY KEY AUTOINCREMENT,
    id_socio INTEGER NOT NULL,
    mes INTEGER NOT NULL,
    anho INTEGER NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    pagado BOOLEAN DEFAULT 0,
    fecha_emision DATE,
    fecha_vencimiento DATE,
    FOREIGN KEY (id_socio) REFERENCES Socios(id_socio)
);

-- Tabla de pagos
CREATE TABLE Pagos (
    id_pago INTEGER PRIMARY KEY AUTOINCREMENT,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATE NOT NULL,
    metodo TEXT NOT NULL CHECK(metodo IN ('tarjeta', 'recibo')),
    id_recibo INTEGER,
    id_reserva INTEGER,
    id_inscripcion INTEGER,
    FOREIGN KEY (id_recibo) REFERENCES Recibos(id_recibo),
    FOREIGN KEY (id_reserva) REFERENCES Reservas(id_reserva),
    FOREIGN KEY (id_inscripcion) REFERENCES Inscripciones(id_inscripcion)
);

-- Tabla de reducción por cancelaciones
CREATE TABLE Reduccion (
    id_credito INTEGER PRIMARY KEY AUTOINCREMENT,
    id_socio INTEGER,
    nombre_no_socio TEXT,
    monto DECIMAL(10,2) NOT NULL,
    fecha_generacion DATE NOT NULL,
    fecha_aplicacion DATE,
    descripcion TEXT,
    FOREIGN KEY (id_socio) REFERENCES Socios(id_socio)
);

