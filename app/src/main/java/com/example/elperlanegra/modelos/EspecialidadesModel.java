package com.example.elperlanegra.modelos;

public class EspecialidadesModel {
    String nombre;
    String descripcion;
    String rating;
    String img_url;
    String tipo;

    public EspecialidadesModel() {
    }

    public EspecialidadesModel(String nombre, String descripcion, String rating, String img_url, String tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rating = rating;
        this.img_url = img_url;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
