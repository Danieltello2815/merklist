package com.merklist.merklist.servicio;

import com.merklist.merklist.modelo.Producto;
import com.merklist.merklist.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository = new ProductoRepository();

    @Override
    public List<Producto> listar() {
        return productoRepository.listar();
    }

    @Override
    public Producto obtenerPorId(int id) {
        return productoRepository.obtenerPorId(id);
    }

    @Override
    public Producto crear(Producto producto) {
        return productoRepository.crear(producto);
    }

    @Override
    public Producto actualizar(int id, Producto producto) {
        return productoRepository.actualizar(id, producto);
    }

    @Override
    public void eliminar(int id) {
        productoRepository.eliminar(id);
    }
}
