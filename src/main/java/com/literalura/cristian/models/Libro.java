package com.literalura.cristian.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private List<String> idiomas;
    private Double numeroDeDescargas;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(){}//Constructor vacio del JPA

    public Libro(DatosLibros datosLibros){
        this.titulo= datosLibros.titulo();
        this.idiomas=datosLibros.idiomas();
        this.numeroDeDescargas= datosLibros.numeroDeDescargas();
        this.autor = new Autor(datosLibros.autor().get(0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString(){
        return "******************************\n" +
                "* **Libro**                  *\n" +
                "* -------------------------- *\n" +
                "* Titulo= " + titulo + "\n" +
                "* Idiomas= " + idiomas + "\n" +
                "* Número de descargas= " + numeroDeDescargas + "\n" +
                "* " + autor + "\n" +
                "******************************";
    }

}
