-- BLOQUE 1: Scripts SQL para IBM Db2

-- Tabla Vehiculo
CREATE TABLE Vehiculo (
    id_vehiculo INT GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    estado VARCHAR(20) NOT NULL,
    nivel_bateria INT NOT NULL
);

-- Tabla Viaje
CREATE TABLE Viaje (
    id_viaje INT GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    id_vehiculo INT NOT NULL,
    usuario_id VARCHAR(100) NOT NULL,
    fecha_inicio TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    estado_viaje VARCHAR(20) NOT NULL,
    CONSTRAINT fk_vehiculo FOREIGN KEY (id_vehiculo) REFERENCES Vehiculo(id_vehiculo)
);

-- Datos de Prueba (Escenarios solicitados)
-- Escenario 1: Vehículo disponible y buena batería (ID = 1)
INSERT INTO Vehiculo (codigo, estado, nivel_bateria) VALUES ('V-001', 'Disponible', 85);

-- Escenario 2: Vehículo disponible pero baja batería para fallo por RN6 (ID = 2)
INSERT INTO Vehiculo (codigo, estado, nivel_bateria) VALUES ('V-002', 'Disponible', 5);

-- Vehículo en mantenimiento (ID = 3)
INSERT INTO Vehiculo (codigo, estado, nivel_bateria) VALUES ('V-003', 'Mantenimiento', 100);
