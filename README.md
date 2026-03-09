# 📚 Literalura

Literalura es una aplicación de consola desarrollada en **Java con Spring Boot** que permite buscar libros utilizando la API de Gutendex (Project Gutenberg) y almacenarlos en una base de datos PostgreSQL.

El programa permite consultar libros, autores y realizar búsquedas utilizando diferentes criterios.

---

## 🚀 Funcionalidades

La aplicación ofrece un menú interactivo con las siguientes opciones:

1. Buscar libro por título
2. Listar libros registrados
3. Listar autores registrados
4. Listar autores vivos en un determinado año
5. Listar libros por idioma
6. Top 10 libros más descargados
7. Estadísticas de libros por idioma

---

## 🛠️ Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- API Gutendex (Project Gutenberg)

---

## 🌐 API utilizada

La aplicación consume datos de:

**Gutendex API**

https://gutendex.com/

Esta API proporciona acceso a miles de libros del proyecto **Project Gutenberg**.

---

## 🗄️ Base de datos

Se utiliza **PostgreSQL** para almacenar la información de:

- Libros
- Autores

Relación entre entidades:
