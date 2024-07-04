package com.literalura.cristian.principal;

import com.literalura.cristian.models.Autor;
import com.literalura.cristian.models.Datos;
import com.literalura.cristian.models.DatosLibros;
import com.literalura.cristian.models.Libro;
import com.literalura.cristian.repository.AutorRepository;
import com.literalura.cristian.repository.LibroRepository;
import com.literalura.cristian.service.ConsumoAPI;
import com.literalura.cristian.service.ConvierteDatos;
import com.literalura.cristian.service.LibroService;
import org.aspectj.apache.bcel.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    private Optional<Libro> libroBuscado;
    private Optional<Autor> autorBuscado;
    private LibroRepository repositorioLibro;
    private AutorRepository repositorioAutor;


    @Autowired
    private LibroService libroService;
    @Autowired
    LibroRepository libroRepository;
    @Autowired
    AutorRepository autorRepository;


    public void muestraMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu =  "   _     _ _             _    _                   \n" +
                    "  | |   (_) |_ ___ _ __ / \\  | |_   _ _ __ __ _   \n" +
                    "  | |   | | __/ _ \\ '__/ _ \\| | | | | '__/ _` |  \n" +
                    "  | |___| | ||  __/ | / ___ \\ | |_| | | | (_| |  \n" +
                    "  |_____|_|\\__\\___|_|/_/   \\_\\_\\__,_|_|  \\__,_|  \n" +
                    "------------------------------------------------\n" +
                    "              Bienvenido a LiterAlura            \n" +
                    "------------------------------------------------\n" +
                    "  1 - Mostrar todos los libros de la API          \n" +
                    "  2 - Buscar Libros por título y guardar en BD    \n" +
                    "  3 - Buscar libros guardados en BD               \n" +
                    "  4 - Buscar autores                              \n" +
                    "  5 - Buscar autores vivos en determinado año     \n" +
                    "  6 - Mostrar libros por idioma (Inglés o Español)\n" +
                    "  7 - Top 10 libros más descargados               \n" +
                    "  0 - Salir                                       \n" +
                    "------------------------------------------------\n" +
                    "Ingrese su opción: ";


            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    mostrarTodosLosLibros();
                    break;

                case 2:
                    BuscarLibros();
                    break;

                case 3:
                    BuscarLibrosDB();
                    break;

                case 4:
                    BuscarAutores();
                    break;

                case 5:
                    mostrarAutoresPorAgno();
                    break;

                case 6:
                    //mostrarLibrosPorIdioma();
                    break;

                case 7:
                    //top10LibrosDescargados();
                    break;

                case 0:
                    System.out.println("Cerrando la app...");
                    break;

                default:
                    System.out.println("Opción inválida");

            }
        }
    }


    //OPCION 1 MUESTRA TODOS LOS LIBROS DE LA API
    private void mostrarTodosLosLibros() {
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);
    }

    //OPCION 2 METODO DE METODO BUSCA Y GUARDA EL LIBRO Y AUTOR EN AMBAS BASES DE DATOS
    private DatosLibros getDatosLibros() {
        System.out.println("Escribe el nombre del libro que deseas buscar en la base de datos:");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));

        //convierte JSON a datos
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("*******************************\n" +
                    "* Libro encontrado! *\n" +
                    "*******************************");
            DatosLibros datosLibros = libroBuscado.get();
            // Convertir DatosLibros a Libro y establecer autores
            Libro libro = new Libro(datosLibros);
            System.out.println(libro);
            return datosLibros;

        } else {
            System.out.println("*******************************\n" +
                    "* Libro no encontrado! *\n" +
                    "*******************************");
            return null;
        }
    }

    private void BuscarLibros() {
        DatosLibros datos = getDatosLibros();
        if (datos != null) {
            libroService.agregarLibro(datos);
        }
    }

    //OPCION 3 METODO QUE BUSCA LIBROS ALOJADOS EN BD
    private void BuscarLibrosDB() {
        System.out.println("Escribe el nombre del libro que deseas buscar en la base de datos");
        var nombreLibro = teclado.nextLine();
        libroBuscado = libroRepository.findByTituloContainsIgnoreCase(nombreLibro);

        if (libroBuscado.isPresent()) {
            System.out.println("El libro buscado es: " +"\n"+ libroBuscado.get());
        } else {
            System.out.println("*******************************\n" +
                    "* Libro no encontrado en BD! *\n" +
                    "*******************************");
        }
    }

    //OPCION 4 METODO PARA BUSCAR AUTORES EN BD
    private void BuscarAutores() {
        System.out.println("Escribe el nombre del autor que deseas buscar en la base de datos");
        var nombreAutor = teclado.nextLine();
        autorBuscado = Optional.ofNullable(autorRepository.findByNombreContainsIgnoreCase(nombreAutor));

        autorBuscado.ifPresentOrElse(
                a -> {
                    System.out.println("El autor buscado es:\n" + a.getNombre());
                },
                () -> {
                    System.out.println("*******************************\n" +
                            "* Autor no encontrado en BD! *\n" +
                            "*******************************");
                }
        );
    }

    //OPCION 5 METODO DE BUSQUEDA DE AUTORES POR AÑO BD
    private void mostrarAutoresPorAgno() {
        System.out.println("Ingrese el año para buscar autores nacidos antes de la fecha indicada:");
        var fecha=teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores= autorRepository.findByFechaDeNacimientoBefore(fecha);
        if (!autores.isEmpty()) {
            System.out.println("Autores con fecha de nacimiento anterior a " + fecha + ":");
            for (Autor autor : autores) {
                System.out.println(autor);
            }
        } else {
            System.out.println("No se encontraron autores con fecha de nacimiento anterior a " + fecha);
        }
    }

    //OPCION 6 METODO DE BUSQUEDA POR IDIOMAS EN LA BD
   /*private void mostrarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma del libro que desea buscar (EN= para Inglés, ES= para Español):");
        var idiomas=teclado.nextLine();
        List<Libro> librosEncontrados=libroRepository.buscarPorIdioma(idiomas);
       if (!librosEncontrados.isEmpty()) {
           System.out.println("Libros encontrados en el idioma " + idiomas + ":");
           for (Libro libro : librosEncontrados) {
               System.out.println(libro);
           }
       } else {
           System.out.println("No se encontraron libros en el idioma seleccionado: " + idiomas);
       }
    }*/



}
