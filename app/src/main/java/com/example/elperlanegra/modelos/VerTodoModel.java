package com.example.elperlanegra.modelos;

import java.io.Serializable;

public class VerTodoModel implements Serializable {
    String nombre;
    String descripcion;
    String tipo;
    String rating;
    String img_url;
    double precio;

    public VerTodoModel() {
    }

    public VerTodoModel(String nombre, String descripcion, String tipo, String rating, String img_url, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.rating = rating;
        this.img_url = img_url;
        this.precio = precio;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
