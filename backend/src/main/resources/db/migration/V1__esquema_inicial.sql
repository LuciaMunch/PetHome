CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(150) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'ADOPTANTE')),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE animal (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(20) NOT NULL CHECK (especie IN ('PERRO', 'GATO')),
    sexo VARCHAR(20) NOT NULL CHECK (sexo IN ('MACHO', 'HEMBRA')),
    tamanio VARCHAR(20) NOT NULL CHECK (tamanio IN ('PEQUENIO', 'MEDIANO', 'GRANDE')),
    edad INT,
    descripcion TEXT,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('DISPONIBLE', 'EN_PROCESO', 'ADOPTADO'))
);

CREATE TABLE foto (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    animal_id BIGINT NOT NULL,
    CONSTRAINT fk_foto_animal FOREIGN KEY (animal_id) REFERENCES animal(id) ON DELETE CASCADE
);

CREATE TABLE evento_sanitario (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(30) NOT NULL CHECK (tipo IN ('VACUNA', 'CASTRACION', 'DESPARASITACION')),
    fecha DATE NOT NULL,
    observaciones TEXT,
    animal_id BIGINT NOT NULL,
    CONSTRAINT fk_evento_animal FOREIGN KEY (animal_id) REFERENCES animal(id) ON DELETE CASCADE
);

CREATE TABLE solicitud_adopcion (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    estado VARCHAR(20) NOT NULL CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA')),
    tipo_vivienda VARCHAR(20) NOT NULL CHECK (tipo_vivienda IN ('CASA', 'DEPARTAMENTO')),
    tiene_patio BOOLEAN NOT NULL,
    integrantes_hogar INT NOT NULL,
    otras_mascotas BOOLEAN NOT NULL,
    experiencia_previa BOOLEAN NOT NULL,
    motivo TEXT,
    usuario_id BIGINT NOT NULL,
    animal_id BIGINT NOT NULL,
    CONSTRAINT fk_solicitud_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_solicitud_animal FOREIGN KEY (animal_id) REFERENCES animal(id)
);
