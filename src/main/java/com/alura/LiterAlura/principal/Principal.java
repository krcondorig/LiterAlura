package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.DatosAutor;
import com.alura.LiterAlura.model.DatosLibro;

import com.alura.LiterAlura.model.DatosResponse;
import com.alura.LiterAlura.service.ConsumoApi;
import com.alura.LiterAlura.service.ConvierteDatos;

import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final Scanner teclado = new Scanner(System.in);
    //private LibroRepository repositorio;

//    public Principal() {
//        this.repositorio = repository;
//    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar Libro 
                    2 - Buscar autor
                    3 - Mostrar series buscadas
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorNombre();
                    break;
                case 2:
                    buscarAutor();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }


    private DatosResponse getDatosLibro() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, DatosResponse.class);
        System.out.println("ent");
        System.out.println(datos);
        return datos;
    }

    private void buscarLibroPorNombre() {
        System.out.println("*****************************************************");
        System.out.println("Escribe el nombre del libro que deseas buscar");
        String nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro);
        var datosBusqueda = conversor.obtenerDatos(json, DatosResponse.class);
        //System.out.println(datosBusqueda);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(libro -> libro.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("Se encontro el libro");
            System.out.println("Titulo: " + libroBuscado.get().titulo());
            System.out.println("Autores: " + libroBuscado.get().autores());
            for (var autor : libroBuscado.get().autores()) {
                System.out.println("Nombre: " + autor.nombre());
            }
            System.out.println("Idiomas: " + libroBuscado.get().idiomas());
            System.out.println("Descargas: " + libroBuscado.get().descargas());
        } else {
            System.out.println("No se encontro el libro");
        }

    }

    private void buscarAutor(){
        System.out.println("*****************************************************");
        System.out.println("Escribe el nombre del autor que deseas buscar");
        String nombreAutor = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreAutor);
        var datosBusqueda = conversor.obtenerDatos(json, DatosResponse.class);
        //System.out.println(datosBusqueda);

        Optional<DatosLibro> autorBuscado = datosBusqueda.resultados().stream()
                .filter(libro -> libro.titulo().toUpperCase().contains(nombreAutor.toUpperCase()))
                .findFirst();
        if (autorBuscado.isPresent()) {
            System.out.println("Se encontro el autor");
            for (var autor : autorBuscado.get().autores()) {
                System.out.println("Nombre: " + autor.nombre());
                System.out.println("Nacimiento: " + autor.fechaNacimiento());
                System.out.println("Descargas: " + autor.fechaFallecimiento());
            }
        } else {
            System.out.println("No se encontro el autor");
        }

    }







}
