package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.*;
import com.alura.LiterAlura.service.ConsumoApi;
import com.alura.LiterAlura.service.ConvierteDatos;

import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

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
                    buscarLibro();
                    break;
                case 2:
                    buscarAutor();
                    break;
                case 3:
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosLibroResult getDatosLibro() {
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search="+ nombreLibro.replace(" ", "+"));
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, DatosLibroResult.class);
        return datos;
    }
    private void buscarLibro() {
        System.out.println("Ingresa el nombre del libro que desea buscar: ");
        var datosLibro = getDatosLibro();
        Optional<DatosLibro> libroBuscado = datosLibro.resultadosLibro().stream()
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("se ha encontrado el libro");
            System.out.println("Libro: " + libroBuscado.get().titulo());
            System.out.println("Autor: " + libroBuscado.get().autoresLibro().stream()
                    .map(DatosAutor::nombre).collect(Collectors.joining(", ")));
            System.out.println("Descargas: " + libroBuscado.get().descargas());
        }else {
            System.out.println("No se ha encontrado el libro");
        }
    }

    private Datos getDatosAutor() {
        var nombreAutor = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search="+ nombreAutor.replace(" ", "+"));
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        return datos;

    }
    private void buscarAutor() {
        System.out.println("Ingresa el nombre del autor que desea buscar: ");
        var datosAutor = getDatosAutor();
        Optional<DatosAutorResult> autorBuscado = datosAutor.resultadosDatos().stream()
                .findFirst();
        if (autorBuscado.isPresent()) {
            System.out.println("se ha encontrado el autor");
            System.out.println("Autor: " + autorBuscado.get().resultadoAutores().stream()
                    .map(DatosAutor::nombre).collect(Collectors.joining(", ")));
            System.out.println("Año de nacimiento: " + autorBuscado.get().resultadoAutores().stream()
                    .map(DatosAutor::anioNacimiento).collect(Collectors.joining(", ")));
            System.out.println("Año de fallecimiento: " + autorBuscado.get().resultadoAutores().stream()
                    .map(DatosAutor::anioFallecimiento).collect(Collectors.joining(", ")));
        }else {
            System.out.println("No se ha encontrado el autor");
        }
    }






}
