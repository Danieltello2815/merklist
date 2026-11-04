package com.merklist.merklist.servicio;

import com.merklist.merklist.modelo.ItemLista;
import com.merklist.merklist.modelo.ListaMercado;
import com.merklist.merklist.modelo.RegistroCompra;

import java.util.List;

public interface ListaMercadoService {

    ListaMercado crear(int usuarioId, ListaMercado lista);

    List<ListaMercado> listarPorUsuario(int usuarioId);

    ListaMercado obtenerPorId(int id);

    ListaMercado actualizar(int id, ListaMercado datos);

    void eliminar(int id);

    ItemLista agregarItem(int listaId, ItemLista item);

    ItemLista marcarComprado(int listaId, int itemId, RegistroCompra compra);

    void eliminarItem(int listaId, int itemId);
}