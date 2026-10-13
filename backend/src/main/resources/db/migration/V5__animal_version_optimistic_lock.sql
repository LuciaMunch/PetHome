-- Control de concurrencia optimista para la tabla animal.
-- Agrega la columna 'version' que usa Hibernate (@Version) para detectar
-- modificaciones simultáneas sobre el mismo animal (p. ej. dos admins aprobando
-- solicitudes del mismo animal a la vez).
-- Las filas existentes arrancan en 0; de ahí en más Hibernate la incrementa solo.
ALTER TABLE animal ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
