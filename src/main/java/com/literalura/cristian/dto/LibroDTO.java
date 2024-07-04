package com.literalura.cristian.dto;

import java.util.List;

public record LibroDTO(
        Long id,
        String titulo,
        List<String> idiomas,
        Double numeroDeDescargas
) {
}
