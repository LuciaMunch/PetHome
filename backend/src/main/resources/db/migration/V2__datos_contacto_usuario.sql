ALTER TABLE usuario
    ADD COLUMN nombre_completo VARCHAR(150),
    ADD COLUMN direccion VARCHAR(255),
    ADD COLUMN ciudad VARCHAR(100),
    ADD COLUMN provincia VARCHAR(100),
    ADD COLUMN telefono VARCHAR(30),
    ADD COLUMN edad INT;