package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.RegistroCompra;
import com.merklist.merklist.repositorio.ProductoRepository;
import com.merklist.merklist.repositorio.RegistroCompraRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistroCompraServiceImpl implements RegistroCompraService {

    private final RegistroCompraRepository registroCompraRepository;
    private final ProductoRepository productoRepository;

    public RegistroCompraServiceImpl(RegistroCompraRepository registroCompraRepository,
                                     ProductoRepository productoRepository) {
        this.registroCompraRepository = registroCompraRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<RegistroCompra> listar() {
        return registroCompraRepository.listar();
    }

    @Override
    public RegistroCompra obtenerPorId(int id) {
        RegistroCompra registro = registroCompraRepository.obtenerPorId(id);
        if (registro == null) {
            throw new RecursoNoEncontradoException("No se encontró el registro de compra con id: " + id);
        }
        return registro;
    }

    @Override
    public List<RegistroCompra> listarPorProductoId(int productoId) {
        return registroCompraRepository.listarPorProductoId(productoId);
    }

    @Override
    public RegistroCompra crear(RegistroCompra registroCompra) {
        if (registroCompra.getFecha() == null) {
            registroCompra.setFecha(LocalDate.now());
        }
        validar(registroCompra);
        return registroCompraRepository.crear(registroCompra);
    }

    @Override
    public RegistroCompra actualizar(int id, RegistroCompra registroCompra) {
        RegistroCompra actual = obtenerPorId(id); // lanza 404 si no existe
        if (registroCompra.getFecha() == null) {
            registroCompra.setFecha(actual.getFecha()); // conserva la fecha original
        }
        validar(registroCompra);
        return registroCompraRepository.actualizar(id, registroCompra);
    }

    @Override
    public void eliminar(int id) {
        obtenerPorId(id);
        registroCompraRepository.eliminar(id);
    }

    @Override
    public double calcularVariacionPrecio(int productoId) {
        if (productoRepository.obtenerPorId(productoId) == null) {
            throw new RecursoNoEncontradoException("No existe un producto con id " + productoId);
        }

        List<RegistroCompra> historial = registroCompraRepository.listarPorProductoId(productoId);

        if (historial.size() < 2) {
            return 0.0;
        }

        double precioUltimo = historial.get(0).getPrecio();
        double precioAnterior = historial.get(1).getPrecio();

        return precioUltimo - precioAnterior;
    }

    private void validar(RegistroCompra registro) {
        if (registro.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (registro.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de compra no puede ser futura");
        }
        if (productoRepository.obtenerPorId(registro.getProductoId()) == null) {
            throw new IllegalArgumentException("El producto con id " + registro.getProductoId() + " no existe");
        }
    }
}