# Merklist — Backend

## Descripción

Merklist es una aplicación web diseñada para la gestión de listas de mercado compartidas, permitiendo organizar productos, administrar listas de compras y llevar un registro de las compras realizadas.

El backend proporciona una API REST encargada de gestionar la lógica de negocio, la persistencia de datos y la comunicación con el frontend.

## Funcionalidades

- Gestión de usuarios y perfiles.
- Gestión de listas de mercado.
- Administración de productos.
- Clasificación de productos por categorías.
- Registro de compras.
- Gestión de cantidades y estados de los productos dentro de las listas.

## Tecnologías utilizadas

- Java 17.
- Spring Boot 4.1.1.
- Spring Web MVC.
- Spring Data JPA.
- Hibernate.
- MySQL.
- Maven.
- Swagger / OpenAPI.
- Git y GitHub.

## Arquitectura del proyecto

El backend está organizado mediante una arquitectura por capas, facilitando la separación de responsabilidades y el mantenimiento del código.

```text
backend/
├── src/
│   └── main/
│       ├── java/com/merklist/merklist/
│       │   ├── configuracion/
│       │   ├── controlador/
│       │   ├── excepcion/
│       │   ├── modelo/
│       │   ├── repositorio/
│       │   ├── servicio/
│       │   └── MerklistApplication.java
│       └── resources/
└── pom.xml
```

| Capa | Responsabilidad |
|---|---|
| Configuración | Configuración general del backend. |
| Controlador | Gestión de solicitudes y respuestas HTTP. |
| Excepción | Manejo de errores. |
| Modelo | Representación de los objetos y entidades del dominio. |
| Repositorio | Acceso y persistencia de datos. |
| Servicio | Implementación de la lógica de negocio. |

## Base de datos

El proyecto utiliza MySQL como sistema de gestión de bases de datos relacional.

La información se organiza en las siguientes tablas:

- usuario
- perfil_usuario
- lista_mercado
- item_lista
- producto
- tipo_producto
- registro_compra

## API REST

La API utiliza los métodos HTTP GET, POST, PUT y DELETE para gestionar los recursos del sistema.

### Controladores

| Controlador | Responsabilidad |
|---|---|
| UsuarioController | Gestión de usuarios. |
| ListaMercadoController | Gestión de listas de mercado. |
| ProductoController | Gestión de productos. |
| TipoProductoController | Gestión de categorías de productos. |
| RegistroCompraController | Gestión de registros de compras. |

## Documentación de la API

La documentación interactiva se encuentra disponible mediante Swagger UI, permitiendo visualizar y probar los endpoints del backend.

**URL base de la API:**

http://localhost:8080/api/v1

**Swagger UI:**

http://localhost:8080/api/v1/swagger-ui/index.html

## Control de versiones

El proyecto utiliza Git y GitHub para el desarrollo colaborativo.

- `main`: rama principal.
- `develop`: rama de desarrollo e integración.
- `feature/*`: desarrollo de funcionalidades.
- `fix/*`: corrección de errores.
- `docs/*`: actualización de documentación.
