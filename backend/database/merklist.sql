CREATE DATABASE IF NOT EXISTS merklist;
USE merklist;

CREATE TABLE tipo_producto (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   nombre VARCHAR(50) NOT NULL
);

CREATE TABLE producto (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  marca VARCHAR(100),
  tipo_producto_id INT NOT NULL,
  CONSTRAINT fk_producto_tipo_producto
  FOREIGN KEY (tipo_producto_id) REFERENCES tipo_producto(id)
);

CREATE TABLE registro_compra (
     id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     precio DECIMAL(12,2) NOT NULL,
     fecha DATE NOT NULL,
     lugar_compra VARCHAR(100),
     producto_id INT NOT NULL,
     CONSTRAINT fk_registro_compra_producto
     FOREIGN KEY (producto_id) REFERENCES producto(id)
);


INSERT INTO tipo_producto (nombre) VALUES ('Abarrotes');
