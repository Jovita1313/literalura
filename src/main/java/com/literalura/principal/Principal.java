package com.literalura.principal;

import com.literalura.entity.Autor;
import com.literalura.entity.Libro;
import com.literalura.model.ConvierteDatos;
import com.literalura.model.Datos;
import com.literalura.model.DatosAutor;
import com.literalura.model.DatosLibro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import com.literalura.service.ConsumoAPI;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private final String URL_BASE = "https://gutendex.com/books/?search=";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("""
                    
                    -------- LITERALURA --------
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros más descargados
                    7 - Buscar autor por nombre
                    8 - Estadísticas de descargas
                    0 - Salir
                    
                    Elige una opción:
                    """);

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
                    listarAutores();
                    break;

                case 4:
                    autoresVivos();
                    break;

                case 5:
                    librosPorIdioma();
                    break;

                case 6:
                    top10Libros();
                    break;

                case 7:
                    buscarAutorPorNombre();
                    break;

                case 8:
                    estadisticasDescargas();
                    break;

                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;

                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibro() {

        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = teclado.nextLine();

        String json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));

        Datos datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado = datos.resultados().stream().findFirst();

        if (libroBuscado.isPresent()) {

            DatosLibro datosLibro = libroBuscado.get();

            List<Libro> librosExistentes =
                    libroRepository.findByTituloIgnoreCase(datosLibro.titulo());

            if (!librosExistentes.isEmpty()) {
                System.out.println("El libro ya está registrado en la base de datos.");
                return;
            }

            DatosAutor datosAutor = datosLibro.autores().get(0);

            Optional<Autor> autorExistente =
                    autorRepository.findByNombre(datosAutor.nombre());

            Autor autor;

            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
            } else {

                autor = new Autor(
                        datosAutor.nombre(),
                        datosAutor.anioNacimiento(),
                        datosAutor.anioFallecimiento()
                );

                autorRepository.save(autor);
            }

            Libro libro = new Libro(
                    datosLibro.titulo(),
                    datosLibro.idiomas().get(0),
                    datosLibro.numeroDescargas()
            );

            libro.setAutor(autor);

            libroRepository.save(libro);

            System.out.println("Libro guardado:");
            System.out.println(libro);

        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void listarLibros() {

        List<Libro> libros = libroRepository.findAll();

        libros.forEach(System.out::println);
    }

    private void listarAutores() {

        List<Autor> autores = autorRepository.findAll();

        autores.forEach(System.out::println);
    }

    private void autoresVivos() {

        System.out.println("Ingrese el año:");
        int anio = teclado.nextInt();

        List<Autor> autores = autorRepository.autoresVivosEnAnio(anio);

        autores.forEach(System.out::println);
    }

    private void librosPorIdioma() {

        System.out.println("""
                Idiomas disponibles:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);

        System.out.println("Ingrese el idioma:");
        String idioma = teclado.nextLine();

        List<Libro> libros = libroRepository.findByIdioma(idioma);

        libros.forEach(System.out::println);
    }

    private void top10Libros() {

        List<Libro> libros = libroRepository.findTop10ByOrderByNumeroDescargasDesc();

        System.out.println("Top 10 libros más descargados:");

        libros.forEach(libro ->
                System.out.println(libro.getTitulo() +
                        " - Descargas: " + libro.getNumeroDescargas())
        );
    }

    private void buscarAutorPorNombre() {

        System.out.println("Ingrese el nombre del autor:");
        String nombre = teclado.nextLine();

        List<Autor> autores = autorRepository.findByNombreContainingIgnoreCase(nombre);

        if (autores.isEmpty()) {
            System.out.println("Autor no encontrado.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void estadisticasDescargas() {

        DoubleSummaryStatistics estadisticas = libroRepository.findAll()
                .stream()
                .mapToDouble(Libro::getNumeroDescargas)
                .summaryStatistics();

        System.out.println("Estadísticas de descargas:");

        System.out.println("Cantidad de libros: " + estadisticas.getCount());
        System.out.println("Promedio de descargas: " + estadisticas.getAverage());
        System.out.println("Máximo de descargas: " + estadisticas.getMax());
        System.out.println("Mínimo de descargas: " + estadisticas.getMin());
    }
}