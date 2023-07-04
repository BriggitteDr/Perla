package com.example.elperlanegra.modelos;

import java.io.Serializable;

public class DetallesPedidoModel implements Serializable {
    String nombre;
    String precio;
    String cantidad;
    double precioTotal;
    String documentId;

    public DetallesPedidoModel() {
    }

    public DetallesPedidoModel(String nombre, String precio, String cantidad, double precioTotal) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
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

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
