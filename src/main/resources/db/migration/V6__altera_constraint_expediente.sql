ALTER TABLE expedientes DROP CONSTRAINT uk_expediente_profissional_dia;

ALTER TABLE expedientes
ADD CONSTRAINT uk_expediente_profissional_dia_inicio
UNIQUE (profissional_id, dia_semana, hora_inicio);