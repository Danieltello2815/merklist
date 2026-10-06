package com.merklist.merklist.modelo;

import java.time.LocalDate;

public class RegistroCompra {

    private int id;
    private double precio;
    private LocalDate fecha;
    private String lugarCompra;
    private int productoId;

    public RegistroCompra() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getLugarCompra() {
        return lugarCompra;
    }

    public void setLugarCompra(String lugarCompra) {
        this.lugarCompra = lugarCompra;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }
}