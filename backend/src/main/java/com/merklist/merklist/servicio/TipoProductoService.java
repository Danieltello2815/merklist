package com.merklist.merklist.servicio;

import com.merklist.merklist.modelo.TipoProducto;

import java.util.List;

public interface TipoProductoService {

    List<TipoProducto> listar();

    TipoProducto obtenerPorId(int id);

    TipoProducto crear(TipoProducto tipoProducto);

    TipoProducto actualizar(int id, TipoProducto tipoProducto);

    void eliminar(int id);
}
