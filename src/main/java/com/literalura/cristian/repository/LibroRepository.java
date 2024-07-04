package com.literalura.cristian.repository;

import com.literalura.cristian.models.Autor;
import com.literalura.cristian.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    @Query(value = "SELECT * FROM libros WHERE idiomas ILIKE %:idioma%", nativeQuery = true)
    List<Libro> buscarPorIdioma(@Param("idioma") String idioma);


}

