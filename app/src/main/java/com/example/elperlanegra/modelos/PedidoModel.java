package com.example.elperlanegra.modelos;

import java.util.List;

public class PedidoModel {

    String idPedido;
    String fecha;
    String estado;
    String hora;
    String montoTotal;
    List<CarritoModel> productoModelList;
    double precioTotal;


    public PedidoModel() {
    }

    public PedidoModel(String idPedido, String fecha, String estado, String hora, String montoTotal, List<CarritoModel> productoModelList) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.estado = estado;
        this.montoTotal = montoTotal;
        this.hora = hora;
        this.productoModelList = productoModelList;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setDocumentId(String documentId) {
    }

    public List<CarritoModel> getProductoModelList() {
        return productoModelList;
    }

    public void setProductoModelList(List<CarritoModel> productoModelList) {
        this.productoModelList = productoModelList;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
