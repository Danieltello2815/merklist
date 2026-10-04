package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.Producto;
import com.merklist.merklist.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.listar();
    }

    @Override
    public Producto obtenerPorId(int id) {
        Producto producto = productoRepository.obtenerPorId(id);
        if (producto == null) {
            throw new RecursoNoEncontradoException("No existe un producto con id " + id);
        }
        return producto;
    }

    @Override
    public Producto crear(Producto producto) {
        validar(producto);
        return productoRepository.crear(producto);
    }

    @Override
    public Producto actualizar(int id, Producto producto) {
        obtenerPorId(id); // lanza 404 si no existe
        validar(producto);
        return productoRepository.actualizar(id, producto);
    }

    @Override
    public void eliminar(int id) {
        obtenerPorId(id); // lanza 404 si no existe
        productoRepository.eliminar(id);
    }

    private void validar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getTipoProductoId() <= 0) {
            throw new IllegalArgumentException("Debes indicar un tipo de producto válido");
        }
    }
}