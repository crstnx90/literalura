package com.literalura.cristian.repository;

import com.literalura.cristian.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    Autor findByNombreContainsIgnoreCase(String nombre);

    List<Autor> findByFechaDeNacimientoBefore(int year);
}

