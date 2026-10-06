package com.merklist.merklist.repositorio;

import com.merklist.merklist.modelo.TipoProducto;

import java.util.List;

public interface TipoProductoRepository {

    List<TipoProducto> listar();

    TipoProducto obtenerPorId(int id);

    boolean existePorNombre(String nombre);

    boolean tieneProductosAsociados(int id);

    TipoProducto crear(TipoProducto tipoProducto);

    TipoProducto actualizar(int id, TipoProducto tipoProducto);

    void eliminar(int id);
}
