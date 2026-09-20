package com.merklist.merklist.servicio;

import com.merklist.merklist.modelo.Producto;
import java.util.List;

public interface ProductoService {
    List<Producto> listar();

    Producto obtenerPorId(int id);
    Producto crear(Producto producto);
    Producto actualizar(int id,Producto producto);
    void eliminar(int id);

}
