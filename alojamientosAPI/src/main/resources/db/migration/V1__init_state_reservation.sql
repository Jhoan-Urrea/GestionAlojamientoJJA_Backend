-- Crear tabla de estados de reserva si no existe (PostgreSQL)
CREATE TABLE IF NOT EXISTS state_reservation (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Insertar estados base evitando duplicados
INSERT INTO state_reservation (name) VALUES ('PENDIENTE') ON CONFLICT (name) DO NOTHING;
INSERT INTO state_reservation (name) VALUES ('CONFIRMADA') ON CONFLICT (name) DO NOTHING;
INSERT INTO state_reservation (name) VALUES ('CANCELADA') ON CONFLICT (name) DO NOTHING;
INSERT INTO state_reservation (name) VALUES ('FINALIZADA') ON CONFLICT (name) DO NOTHING;

