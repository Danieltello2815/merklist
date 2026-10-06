package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.RecursoDuplicadoException;
import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.TipoProducto;
import com.merklist.merklist.repositorio.TipoProductoRepository;
import org.springframework.stereotype.Service;
import com.merklist.merklist.excepcion.OperacionNoPermitidaException;

import java.util.List;

@Service
public class TipoProductoServiceImpl implements TipoProductoService {

    private final TipoProductoRepository tipoProductoRepository;

    public TipoProductoServiceImpl(TipoProductoRepository tipoProductoRepository) {
        this.tipoProductoRepository = tipoProductoRepository;
    }

    @Override
    public List<TipoProducto> listar() {
        return tipoProductoRepository.listar();
    }

    @Override
    public TipoProducto obtenerPorId(int id) {
        TipoProducto tipo = tipoProductoRepository.obtenerPorId(id);
        if (tipo == null) {
            throw new RecursoNoEncontradoException("No existe un tipo de producto con id " + id);
        }
        return tipo;
    }

    @Override
    public TipoProducto crear(TipoProducto tipoProducto) {
        validarNombre(tipoProducto);
        tipoProducto.setNombre(tipoProducto.getNombre().trim());
        if (tipoProductoRepository.existePorNombre(tipoProducto.getNombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe un tipo de producto llamado " + tipoProducto.getNombre());
        }
        return tipoProductoRepository.crear(tipoProducto);
    }

    @Override
    public TipoProducto actualizar(int id, TipoProducto tipoProducto) {
        TipoProducto actual = obtenerPorId(id); // lanza 404 si no existe
        validarNombre(tipoProducto);
        tipoProducto.setNombre(tipoProducto.getNombre().trim());
        boolean cambioNombre = !actual.getNombre().equalsIgnoreCase(tipoProducto.getNombre());
        if (cambioNombre && tipoProductoRepository.existePorNombre(tipoProducto.getNombre())) {
            throw new RecursoDuplicadoException(
                    "Ya existe un tipo de producto llamado " + tipoProducto.getNombre());
        }
        return tipoProductoRepository.actualizar(id, tipoProducto);
    }

    @Override
    public void eliminar(int id) {
        obtenerPorId(id); // lanza 404 si no existe
        if (tipoProductoRepository.tieneProductosAsociados(id)) {
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar el tipo de producto con id " + id + " porque tiene productos asociados");
        }
        tipoProductoRepository.eliminar(id);
    }

    private void validarNombre(TipoProducto tipoProducto) {
        String nombre = tipoProducto.getNombre();
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del tipo de producto es obligatorio");
        }
        if (nombre.trim().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres");
        }
    }
}
