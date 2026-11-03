package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.Producto;
import com.merklist.merklist.modelo.RegistroCompra;
import com.merklist.merklist.repositorio.ProductoRepository;
import com.merklist.merklist.repositorio.RegistroCompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistroCompraServiceImplTest {

    @Mock
    private RegistroCompraRepository registroCompraRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private RegistroCompraServiceImpl registroCompraService;

    private RegistroCompra registroPrueba;
    private Producto productoPrueba;

    @BeforeEach
    void setUp() {
        productoPrueba = new Producto();
        productoPrueba.setId(1);
        productoPrueba.setNombre("Leche Entera");

        registroPrueba = new RegistroCompra();
        registroPrueba.setId(1);
        registroPrueba.setPrecio(4500.0);
        registroPrueba.setFecha(LocalDate.now());
        registroPrueba.setLugarCompra("Supermercado");
        registroPrueba.setProductoId(1);
    }

    @Test
    void listar_DebeRetornarListaDeRegistros() {
        when(registroCompraRepository.listar()).thenReturn(Arrays.asList(registroPrueba));

        List<RegistroCompra> resultado = registroCompraService.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(registroCompraRepository, times(1)).listar();
    }

    @Test
    void obtenerPorId_CuandoExiste_DebeRetornarRegistro() {
        when(registroCompraRepository.obtenerPorId(1)).thenReturn(registroPrueba);

        RegistroCompra resultado = registroCompraService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals(4500.0, resultado.getPrecio());
    }

    @Test
    void obtenerPorId_КогдаNoExiste_DebeLanzarExcepcion() {
        when(registroCompraRepository.obtenerPorId(99)).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> {
            registroCompraService.obtenerPorId(99);
        });
    }

    @Test
    void crear_ConDatosValidos_DebeCrearRegistro() {
        when(productoRepository.obtenerPorId(1)).thenReturn(productoPrueba);
        when(registroCompraRepository.crear(any(RegistroCompra.class))).thenReturn(registroPrueba);

        RegistroCompra resultado = registroCompraService.crear(registroPrueba);

        assertNotNull(resultado);
        assertEquals(4500.0, resultado.getPrecio());
        verify(registroCompraRepository, times(1)).crear(registroPrueba);
    }

    @Test
    void crear_ConPrecioInvalido_DebeLanzarExcepcion() {
        registroPrueba.setPrecio(0.0);

        assertThrows(IllegalArgumentException.class, () -> {
            registroCompraService.crear(registroPrueba);
        });

        verify(registroCompraRepository, never()).crear(any());
    }

    @Test
    void actualizar_CuandoExiste_DebeActualizar() {
        when(registroCompraRepository.obtenerPorId(1)).thenReturn(registroPrueba);
        when(productoRepository.obtenerPorId(1)).thenReturn(productoPrueba);
        when(registroCompraRepository.actualizar(eq(1), any(RegistroCompra.class))).thenReturn(registroPrueba);

        RegistroCompra resultado = registroCompraService.actualizar(1, registroPrueba);

        assertNotNull(resultado);
        verify(registroCompraRepository, times(1)).actualizar(eq(1), any(RegistroCompra.class));
    }

    @Test
    void eliminar_CuandoExiste_DebeEliminar() {
        when(registroCompraRepository.obtenerPorId(1)).thenReturn(registroPrueba);
        doNothing().when(registroCompraRepository).eliminar(1);

        assertDoesNotThrow(() -> {
            registroCompraService.eliminar(1);
        });

        verify(registroCompraRepository, times(1)).eliminar(1);
    }

    @Test
    void calcularVariacionPrecio_ConSuficienteHistorial_DebeCalcularDiferencia() {
        RegistroCompra r1 = new RegistroCompra();
        r1.setPrecio(5000.0);
        RegistroCompra r2 = new RegistroCompra();
        r2.setPrecio(4500.0);

        when(productoRepository.obtenerPorId(1)).thenReturn(productoPrueba);
        when(registroCompraRepository.listarPorProductoId(1)).thenReturn(Arrays.asList(r1, r2));

        double variacion = registroCompraService.calcularVariacionPrecio(1);

        assertEquals(500.0, variacion);
    }
}