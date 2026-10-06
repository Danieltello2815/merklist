package com.merklist.merklist.repositorio;

import com.merklist.merklist.modelo.Producto;
import java.util.List;

    public interface ProductoRepository {
        List<Producto> listar();
        Producto obtenerPorId(int id);
        Producto crear(Producto producto);
        Producto actualizar(int id, Producto producto);
        void eliminar(int id);
    }

