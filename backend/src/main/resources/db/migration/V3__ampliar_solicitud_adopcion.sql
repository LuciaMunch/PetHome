ALTER TABLE solicitud_adopcion
    ADD COLUMN cantidad_otras_mascotas INT,
    ADD COLUMN cuales_otras_mascotas TEXT,
    ADD COLUMN tiene_mascotas_actualmente BOOLEAN,
    ADD COLUMN cuales_mascotas_actuales TEXT,
    ADD COLUMN tiene_trabajo BOOLEAN,
    ADD COLUMN cual_trabajo VARCHAR(255),
    ADD COLUMN viaja_seguido BOOLEAN,
    ADD COLUMN quien_cuida_en_viajes TEXT;