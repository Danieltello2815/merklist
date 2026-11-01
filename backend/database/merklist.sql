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
)

CREATE TABLE usuario (
                         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         correo VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE perfil_usuario (
                                id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                telefono VARCHAR(20),
                                ciudad VARCHAR(50),
                                usuario_id INT NOT NULL UNIQUE,
                                CONSTRAINT fk_perfil_usuario_usuario
                                    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE lista_mercado (
                               id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               nombre VARCHAR(100) NOT NULL,
                               fecha_creacion DATE NOT NULL,
                               usuario_id INT NOT NULL,
                               CONSTRAINT fk_lista_mercado_usuario
                                   FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE item_lista (
                            id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            cantidad INT NOT NULL,
                            estado VARCHAR(20) NOT NULL,
                            lista_id INT NOT NULL,
                            producto_id INT NOT NULL,
                            CONSTRAINT fk_item_lista_lista
                                FOREIGN KEY (lista_id) REFERENCES lista_mercado(id),
                            CONSTRAINT fk_item_lista_producto
                                FOREIGN KEY (producto_id) REFERENCES producto(id)
);


INSERT INTO tipo_producto (nombre) VALUES ('Abarrotes');
