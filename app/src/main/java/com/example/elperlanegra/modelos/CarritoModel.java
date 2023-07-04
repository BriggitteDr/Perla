package com.example.elperlanegra.modelos;

import java.io.Serializable;

public class CarritoModel implements Serializable {
    String nombre;
    String precio;
    String fecha;
    String hora;
    String cantidad;
    String img;
    double precioTotal;
    String documentId;

    public CarritoModel() {
    }

    public CarritoModel(String nombre, String precio, String fecha, String hora, String cantidad, String img, double precioTotal) {
        this.nombre = nombre;
        this.precio = precio;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidad = cantidad;
        this.img = img;
        this.precioTotal = precioTotal;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
