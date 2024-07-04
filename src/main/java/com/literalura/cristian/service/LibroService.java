package com.literalura.cristian.service;

import com.literalura.cristian.dto.AutorDTO;
import com.literalura.cristian.dto.LibroDTO;
import com.literalura.cristian.models.Autor;
import com.literalura.cristian.models.DatosAutor;
import com.literalura.cristian.models.DatosLibros;
import com.literalura.cristian.models.Libro;
import com.literalura.cristian.principal.Principal;
import com.literalura.cristian.repository.AutorRepository;
import com.literalura.cristian.repository.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public void agregarLibro(DatosLibros datosLibros) {
        Libro libro = new Libro(datosLibros);

        for (DatosAutor datosAutor : datosLibros.autor()) {
            Autor autor = autorRepository.findByNombreContainsIgnoreCase(datosAutor.nombre());
            if (autor == null) {
                autor = new Autor(datosAutor);

            } else {
                autor = autorRepository.save(autor); // Esto asegura que el autor esté gestionado por el contexto de persistencia
            }
            libro.getAutor();
        }
        libroRepository.save(libro);
    }

    public List<LibroDTO> convierteDatos(List<Libro> libro){
        return libro.stream()
                .map(l->new LibroDTO(l.getId(),l.getTitulo(),l.getIdiomas(),l.getNumeroDeDescargas()))
                .collect(Collectors.toList());
    }

    public List<LibroDTO> obtenerTodosLosLibrosBD(){
        return convierteDatos(libroRepository.findAll());
    }


    public List<AutorDTO> convierteDatosAutor(List<Autor> autor){
        return autor.stream()
                .map(a->new AutorDTO(a.getNombre(),a.getFechaDeNacimiento()))
                .collect(Collectors.toList());
    }

    public List<AutorDTO> obtenerTodosLosAutores(){return convierteDatosAutor(autorRepository.findAll());}

    public List<Libro> buscarLibrosPorIdioma(String idioma) {
        return libroRepository.buscarPorIdioma(idioma);
    }

}
