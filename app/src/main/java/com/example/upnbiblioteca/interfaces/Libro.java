package com.example.upnbiblioteca.interfaces;

import java.io.Serializable;

public class Libro implements Serializable {
    private int id;
    private String portada;
    private String titulo;
    private String autor;
    private int year;
    private String genero;
    private String idioma;

    public Libro() {
    }

    public Libro(int id, String portada, String titulo, String autor, int year, String genero, String idioma) {
        this.id = id;
        this.portada = portada;
        this.titulo = titulo;
        this.autor = autor;
        this.year = year;
        this.genero = genero;
        this.idioma = idioma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
