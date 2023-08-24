-- Tabla 'usuarios' con columna 'username' y contraseña
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password INT(255) NOT NULL,
    saldo DOUBLE NOT NULL
);

-- Tabla 'historico' con columna 'username' referenciando la tabla 'usuarios'
CREATE TABLE IF NOT EXISTS historico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    username VARCHAR(255) NOT NULL,
    tipo_operacion VARCHAR(50) NOT NULL,
    cantidad DOUBLE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Inserciones de ejemplo en la tabla 'usuarios'
INSERT INTO usuarios (username, password, saldo) VALUES
('juanperez', 1234, 1000.0),
('anaramirez', 5678, 2500.0),
('carlosgomez', 9812, 500.0),
('martatorres', 3456, 750.0),
('luisafernandez', 7890, 3000.0);

-- Inserciones de ejemplo en la tabla 'historico'
INSERT INTO historico (usuario_id, username, tipo_operacion, cantidad) VALUES (1, 'juanperez', 'Deposito', 1000.0);
INSERT INTO historico (usuario_id, username, tipo_operacion, cantidad) VALUES (2, 'anaramirez', 'Deposito', 2500.0);
INSERT INTO historico (usuario_id, username, tipo_operacion, cantidad) VALUES (3, 'carlosgomez', 'Deposito', 500.0);
INSERT INTO historico (usuario_id, username, tipo_operacion, cantidad) VALUES (4, 'martatorres', 'Deposito', 750.0);
INSERT INTO historico (usuario_id, username, tipo_operacion, cantidad) VALUES (5, 'luisafernandez', 'Deposito', 3000.0);
