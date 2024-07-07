# Challenge 3: Literalura 👩‍💻
##  ¡Bienvenid@s al tercer desafío del bootcamp Oracle Next Education! 📚

Este proyecto es una aplicacion Java para consultar libros y autores. La aplicacion consume la API externa de Gutendex que nos proporciona informacion sobre una gran variedad de libros y las almacena en una base de datos PostgreSQL para luego poder realizar consultas

## Tecnologias usadas

✅ JDK 17

✅ Spring Boot

✅ Spring Data JPA

✅ Hibernate

✅ PostgreSQL

✅ Maven

✅ Jackson Databind

## ¿Qué puedes hacer con Literalura? 📋

- Buscar Libro por titulo.
- Listar libros registrados.
- Listar autores registrados.
- Listar autores vivos en un año en especifico.
- Listar libros por idioma
- Obtener el top 10 de libros con más descargas
- Generar algunas estadísticas sobre las descargas de libros.

## ¿Cómo empezar?

1. Clona el repositorio en tu maquina local

    ```bash
    git clone https://github.com/tu_usuario/literalura.git
    ```

2. Agrega variables de entorno:

    Deberas agregar las siguientes variables de entorno para que la aplicacion pueda funcionar, a continuacion el nombre que cada variable de entorno debe tener

    <table border="1">
        <tr style="text-align: center;">
            <td>VARIABLE</td>
            <td>DESCRIPCIÓN</td>
        </tr>
        <tr>
            <td>DB_HOST</td>
            <td>Ruta a la base de datos con su puerto</td>
        </tr>
        <tr>
            <td>DB_USER</td>
            <td>Usuario de la base de datos</td>
        </tr>
        <tr>
            <td>DB_PASSWORD</td>
            <td>contraseña de acceso a la base de datos</td>
        </tr>
        <tr>
    </table>

3. Configura la base de datos PostgreSQL:

    - Crea una base de datos para el proyecto:

        ```sql
        CREATE DATABASE literalura;
        ```
    - Asegúrate de tener el usuario y contraseña configurados en PostgreSQL.

4. Navega al directorio del proyecto:

    ```bash
    cd literalura
    ```

5. Instala las dependencias

    ```bash
    mvn install
    ```
6. Ejecuta la aplicacion

    ```bash
    mvn spring-boot:run
    ```
## Imagenes del funcionamiento 

- Menú principal

![1](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/6950d489-a1a6-481a-8a71-430de0e28c68)

- Buscar libro por titulo

![2](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/c41fc246-6c16-4d37-be97-834d5a467a3c)

- Listar libros registrados.

![3](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/fb20ac39-81a3-4a45-8a0b-50accf5b7a29)

- Listar autores registrados.

![4](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/b810d113-f0ec-4119-821f-584b6f19a321)

- Listar autores vivos en un año en especifico.

![5](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/0e8b4aa2-63ec-4190-a52d-7639c38485ce)

- Listar libros por idioma

![6](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/bf3ed8ec-edcd-4938-86a0-19e6e0fd071e)

- Obtener el top 10 de libros con más descargas

![7](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/b1aa3a33-704b-41cc-8f34-da0bc1c5c8f2)

- Generar algunas estadísticas sobre las descargas de libros.

![8](https://github.com/krcondorig/ConversorMonedas-ONE/assets/38484885/a042de5d-fec6-48f2-9668-b69e9d35da42)








