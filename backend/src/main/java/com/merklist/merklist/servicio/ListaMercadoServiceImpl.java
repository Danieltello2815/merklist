package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.OperacionNoPermitidaException;
import com.merklist.merklist.excepcion.RecursoDuplicadoException;
import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.EstadoItem;
import com.merklist.merklist.modelo.ItemLista;
import com.merklist.merklist.modelo.ListaMercado;
import com.merklist.merklist.modelo.RegistroCompra;
import com.merklist.merklist.modelo.Usuario;
import com.merklist.merklist.repositorio.ProductoRepository;
import com.merklist.merklist.repositorio.jpa.ListaMercadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListaMercadoServiceImpl implements ListaMercadoService {

    private final ListaMercadoRepository listaRepository;
    private final UsuarioService usuarioService;
    private final ProductoRepository productoRepository;
    private final RegistroCompraService registroCompraService;

    public ListaMercadoServiceImpl(ListaMercadoRepository listaRepository,
                                   UsuarioService usuarioService,
                                   ProductoRepository productoRepository,
                                   RegistroCompraService registroCompraService) {
        this.listaRepository = listaRepository;
        this.usuarioService = usuarioService;
        this.productoRepository = productoRepository;
        this.registroCompraService = registroCompraService;
    }

    @Override
    @Transactional
    public ListaMercado crear(int usuarioId, ListaMercado lista) {
        Usuario usuario = usuarioService.obtenerPorId(usuarioId);
        validarNombre(lista);
        lista.setId(null);
        lista.setNombre(lista.getNombre().trim());
        lista.setFechaCreacion(LocalDate.now());
        lista.setUsuario(usuario);
        lista.setItems(new ArrayList<>());
        return listaRepository.save(lista);
    }

    @Override
    public List<ListaMercado> listarPorUsuario(int usuarioId) {
        usuarioService.obtenerPorId(usuarioId); // lanza 404 si el usuario no existe
        return listaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public ListaMercado obtenerPorId(int id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una lista de mercado con id " + id));
    }

    @Override
    @Transactional
    public ListaMercado actualizar(int id, ListaMercado datos) {
        ListaMercado actual = obtenerPorId(id);
        validarNombre(datos);
        actual.setNombre(datos.getNombre().trim());
        return actual;
    }

    @Override
    @Transactional
    public void eliminar(int id) {
        ListaMercado lista = obtenerPorId(id);
        listaRepository.delete(lista);
    }

    @Override
    @Transactional
    public ItemLista agregarItem(int listaId, ItemLista item) {
        ListaMercado lista = obtenerPorId(listaId);
        if (item.getCantidad() == null || item.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (item.getProductoId() == null
                || productoRepository.obtenerPorId(item.getProductoId()) == null) {
            throw new IllegalArgumentException(
                    "El producto con id " + item.getProductoId() + " no existe");
        }
        boolean repetido = lista.getItems().stream()
                .anyMatch(i -> i.getProductoId().equals(item.getProductoId()));
        if (repetido) {
            throw new RecursoDuplicadoException(
                    "El producto con id " + item.getProductoId() + " ya está en la lista");
        }
        item.setId(null);
        item.setEstado(EstadoItem.PENDIENTE);
        lista.agregarItem(item);
        listaRepository.flush();
        return item;
    }

    @Override
    @Transactional
    public ItemLista marcarComprado(int listaId, int itemId, RegistroCompra compra) {
        ListaMercado lista = obtenerPorId(listaId);
        ItemLista item = buscarItem(lista, itemId);
        if (item.getEstado() == EstadoItem.COMPRADO) {
            throw new OperacionNoPermitidaException(
                    "El ítem con id " + itemId + " ya fue marcado como comprado");
        }
        compra.setProductoId(item.getProductoId());
        registroCompraService.crear(compra); // valida precio y fecha; guarda en el historial
        item.setEstado(EstadoItem.COMPRADO);
        return item;
    }

    @Override
    @Transactional
    public void eliminarItem(int listaId, int itemId) {
        ListaMercado lista = obtenerPorId(listaId);
        ItemLista item = buscarItem(lista, itemId);
        lista.getItems().remove(item);
    }

    private ItemLista buscarItem(ListaMercado lista, int itemId) {
        return lista.getItems().stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un ítem con id " + itemId + " en la lista " + lista.getId()));
    }

    private void validarNombre(ListaMercado lista) {
        if (lista.getNombre() == null || lista.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la lista es obligatorio");
        }
    }
}
