package com.merklist.merklist.servicio;

import com.merklist.merklist.modelo.RegistroCompra;
import java.util.List;

public interface RegistroCompraService {

    List<RegistroCompra> listar();

    RegistroCompra obtenerPorId(int id);

    List<RegistroCompra> listarPorProductoId(int productoId);

    RegistroCompra crear(RegistroCompra registroCompra);

    RegistroCompra actualizar(int id, RegistroCompra registroCompra);

    void eliminar(int id);

    double calcularVariacionPrecio(int productoId);
}
