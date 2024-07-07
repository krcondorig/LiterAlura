package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.*;
import com.alura.LiterAlura.repository.AutorRepository;
import com.alura.LiterAlura.repository.LibroRepository;
import com.alura.LiterAlura.service.ConsumoApi;
import com.alura.LiterAlura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_CODIGOS_IDIOMAS = "https://krcondorig.github.io/lenguajes-iso639-1-espaniol-json/lenguajes-iso636-1.json";
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final Scanner teclado = new Scanner(System.in);
    private String nombreLibro;
    private List<Libro> libros;
    private List<Autor> autores;
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;


    public Principal(LibroRepository repositorioLibro, AutorRepository repositorioAutor) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioAutor = repositorioAutor;

    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ---------------------------------------------
                                   MENU PRINCIPAL
                    ---------------------------------------------
                    1 - Buscar Libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos por X año
                    5 - Listar libros por idioma
                    6 - Top 10 libros con más descargas
                    7 - Algunas estadísticas
                                 
                    0 - Salir
                    ---------------------------------------------                 
                    """;
            System.out.print(menu);
            System.out.print("Elija una opcion: ");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();

                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;

                case 4:
                    listarAutoresVivosPorAnio();
                    break;

                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    listarTop10LibrosMasDescargados();
                    break;
                case 7: mostrarEstadisticas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private Datos getDatosLibro() {
        nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search="+ nombreLibro.replace(" ", "+"));
        //System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        return datos;
    }

    private void buscarLibro() {
        System.out.print("Escriba el nombre del libro: ");
        var datos = getDatosLibro();
        //System.out.println(datos);

        Optional<DatosLibro> libroBuscado = datos.datosResultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            //System.out.println("libro encontrado");
            DatosAutor datosAutor = libroBuscado.get().autores().get(0);
            //System.out.println(datosAutor);

            //System.out.println(libroBuscado.get().idLibro());
            if (repositorioLibro.findByIdLibro(libroBuscado.get().idLibro()).isPresent()) {

                System.out.println("Libro ya registrado en la bd");
            }else {
                var libroEncontrado = """
                        **************************************
                                    Datos del Libro
                        **************************************
                        
                        Titulo: %s
                        Autor: %s                    
                        Idiomas: %s
                        Descargas: %s
                        """;
                System.out.printf(
                        (libroEncontrado) + "%n", libroBuscado.get().titulo(),
                        libroBuscado.get().autores().get(0).nombre(),
                        libroBuscado.get().idiomas().get(0),
                        libroBuscado.get().descargas()
                );

                Optional<Autor> autor= repositorioAutor.findByNombre(datosAutor.nombre());
                if (autor.isPresent()) {
                    System.out.println("Autor encontrado");
                    Libro libro = new Libro(libroBuscado.get());
                    libro.setAutor(autor.get());
                    //repositorioLibro.save(libro);
                } else {
                    System.out.println("Autor no encontrado");
                    libros = libroBuscado.stream()
                            .map(Libro::new)
                            .collect(Collectors.toList());
                    Autor autorClase = new Autor(datosAutor);
                    autorClase.setLibros(libros);
                    repositorioAutor.save(autorClase);
                }
                System.out.println("Libro registrado");

            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void datosAutor(List<Autor> listaAutor) {
        var muestraAutor = """
                **************************************
                            Datos del autor
                **************************************
                
                Nombre: %s
                Año de nacimiento: %s
                Año de fallecimiento: %s
                """;
        listaAutor.forEach(a -> System.out.printf(
                (muestraAutor) + "%n", a.getNombre(),
                (a.getAnioNacimiento() == null) ? "Sin datos" : a.getAnioNacimiento(),
                (a.getAnioFallecimiento() == null) ? "Sin datos" : a.getAnioFallecimiento(),
                repositorioLibro.obtenerLibrosPorAutor(a.getIdAutor()).stream()
                        .map(Libro::getTitulo)
                        .collect(Collectors.joining(", "))
        ));
    }

    private void datosLibro(List<Libro> listaLibro) {
        var muestraLibro = """
                **************************************
                            Datos del Libro
                **************************************
                
                Titulo: %s
                Autor: %s
                Idiomas: %s
                Descargas: %s
                
                """;
        listaLibro.forEach(l -> System.out.println(
                muestraLibro.formatted(
                        l.getTitulo(),
                        l.getAutor().getNombre(),
                        l.getIdioma(),
                        l.getNumeroDescargas()
                )
        ));
    }


    private void listarLibros() {
        libros = repositorioLibro.findAll();
        var muestraListaLibros = """
                ****************************************************
                            Lista de libros en Literalura
                ****************************************************       
                """;
        System.out.print("\n" + muestraListaLibros + "\n");
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            var cuentaLibros = libros.size();
            datosLibro(libros);
            System.out.println("Total de libros registrados: " + cuentaLibros);
        }
    }

    private void listarAutoresRegistrados() {
        var muestraListaAutores = """
                **************************************
                    Lista de autores en Literalura
                **************************************
                """;
        System.out.print("\n" + muestraListaAutores + "\n");
        autores = repositorioAutor.findAllByOrderByNombreAsc();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados");
        } else {
            var cuentaAutores = autores.size();
            datosAutor(autores);
            System.out.println("Total de autores registrados: " + cuentaAutores);
        }
    }

    private void listarAutoresVivosPorAnio(){
        try {
            System.out.println("Ingresa el año");
            var anio = teclado.nextInt();
            teclado.nextLine();
            autores = repositorioAutor.obtenerAutorVivoAnio(anio);
            var muestraAnioAutor = """
                *************************************************************
                    Lista de autores vivos para el año %s en Literalura
                *************************************************************
                """;
            System.out.printf((muestraAnioAutor) + "%n", anio);
            //System.out.println("\n" + muestraAnioAutor + anio + "\n");
            if (autores.isEmpty()) {
                System.out.println("No hay autores vivos para el año registrado");
            } else {
                datosAutor(autores);
                System.out.println("Total de autores registrados: " + autores.size());
            }
        }catch (InputMismatchException e){
            System.out.println("El año ingresado no es valido");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        var muestraIdioma = """
                **************************************
                    Lista de idiomas disponibles
                **************************************
                """;
        var idiomasLibro = repositorioLibro.obtenerListaUnicaIdioma();
        var idiomasJson = consumoApi.obtenerDatos(URL_CODIGOS_IDIOMAS);
        var datosIdiomas = conversor.obtenerDatos(idiomasJson, DatosIdioma.class);
        //System.out.println(datosIdiomas);
        List<Idioma> idiomaDisponibles = new ArrayList<>();
        if (idiomasLibro.isEmpty()) {
            System.out.println("No hay libros con ese idioma registrado");
        } else {
            for (String codigoIdioma : idiomasLibro) {
                var idiomaEncontrado = datosIdiomas.idiomas().stream()
                        .filter(i -> i.codigoIdioma().contains(codigoIdioma))
                        .collect(Collectors.toList());
                idiomaDisponibles.add(idiomaEncontrado.get(0));
            }
            System.out.println(muestraIdioma);
            idiomaDisponibles.forEach(i -> System.out.println(i.codigoIdioma()
                    + " - " + i.idioma()));
            System.out.println("Ingrese el codigo del idioma del libro a buscar");
            String inputCodigoIdioma = teclado.nextLine();
            if (inputCodigoIdioma.isEmpty()) {
                System.out.println("Ingrese un codigo de idioma");
            } else {
                libros = repositorioLibro.findByIdioma(inputCodigoIdioma);
                if (libros.isEmpty()) {
                    System.out.println("No hay libros con ese idioma registrado");
                } else {
                    var cuentaLibros = libros.size();
                    datosLibro(libros);
                    System.out.println("Total de libros registrados: " + cuentaLibros);
                }

            }
        }
    }

    private void listarTop10LibrosMasDescargados() {
        libros = repositorioLibro.findTop10ByOrderByNumeroDescargasDesc();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados");
        } else {
            var muestraTop10Libros = """
                            ********************************************************
                                        Top 10 libros con más descargados
                            ********************************************************
                            
                            """;
            System.out.print(muestraTop10Libros);
            var cuentaLibros = libros.size();
            datosLibro(libros);
            System.out.println("Total de libros registrados: " + cuentaLibros);
        }
    }


    private void mostrarEstadisticas() {
        libros = repositorioLibro.findAll();

        var muestraEstadisticas = """
                ***************************************
                    Datos estadisticos de Literalura
                ***************************************
                
                Total de librps: %s
                Libro mas descargado: %s
                Libro menos descargado: %s
                Promedio de descargas: %s
                
                """;

        LongSummaryStatistics estadisticas = libros.stream()
                .filter(l -> l.getNumeroDescargas() > 0)
                .collect(Collectors.summarizingLong(l -> l.getNumeroDescargas().longValue()));

        System.out.println(muestraEstadisticas.formatted(
                estadisticas.getCount(),
                estadisticas.getMax(),
                estadisticas.getMin(),
                Math.round(estadisticas.getAverage())
        ));
    }

}
