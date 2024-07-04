package com.literalura.cristian.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json,Class<T> clase);
}
