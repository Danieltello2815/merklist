package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.OperacionNoPermitidaException;
import com.merklist.merklist.excepcion.RecursoDuplicadoException;
import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.TipoProducto;
import com.merklist.merklist.repositorio.TipoProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoProductoServiceImplTest {

    @Mock
    private TipoProductoRepository tipoProductoRepository;

    @InjectMocks
    private TipoProductoServiceImpl tipoProductoService;

    private TipoProducto tipoProductoPrueba;

    @BeforeEach
    void setUp() {
        tipoProductoPrueba = new TipoProducto();
        tipoProductoPrueba.setId(1);
        tipoProductoPrueba.setNombre("Lácteos");
    }

    @Test
    void listar_DebeRetornarListaDeTipos() {
        when(tipoProductoRepository.listar()).thenReturn(Arrays.asList(tipoProductoPrueba));

        List<TipoProducto> resultado = tipoProductoService.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(tipoProductoRepository, times(1)).listar();
    }

    @Test
    void obtenerPorId_CuandoExiste_DebeRetornarTipo() {
        when(tipoProductoRepository.obtenerPorId(1)).thenReturn(tipoProductoPrueba);

        TipoProducto resultado = tipoProductoService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Lácteos", resultado.getNombre());
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DebeLanzarExcepcion() {
        when(tipoProductoRepository.obtenerPorId(99)).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> {
            tipoProductoService.obtenerPorId(99);
        });
    }

    @Test
    void crear_ConDatosValidos_DebeCrearTipo() {
        when(tipoProductoRepository.existePorNombre("Lácteos")).thenReturn(false);
        when(tipoProductoRepository.crear(any(TipoProducto.class))).thenReturn(tipoProductoPrueba);

        TipoProducto resultado = tipoProductoService.crear(tipoProductoPrueba);

        assertNotNull(resultado);
        verify(tipoProductoRepository, times(1)).crear(tipoProductoPrueba);
    }

    @Test
    void crear_ConNombreDuplicado_DebeLanzarExcepcion() {
        when(tipoProductoRepository.existePorNombre("Lácteos")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> {
            tipoProductoService.crear(tipoProductoPrueba);
        });

        verify(tipoProductoRepository, never()).crear(any());
    }

    @Test
    void actualizar_ConDatosValidos_DebeActualizar() {
        when(tipoProductoRepository.obtenerPorId(1)).thenReturn(tipoProductoPrueba);
        when(tipoProductoRepository.actualizar(eq(1), any(TipoProducto.class))).thenReturn(tipoProductoPrueba);

        TipoProducto actualizado = new TipoProducto();
        actualizado.setNombre("Lácteos Actualizado");

        TipoProducto resultado = tipoProductoService.actualizar(1, actualizado);

        assertNotNull(resultado);
        verify(tipoProductoRepository, times(1)).actualizar(1, actualizado);
    }

    @Test
    void eliminar_CuandoNoTieneProductos_DebeEliminar() {
        when(tipoProductoRepository.obtenerPorId(1)).thenReturn(tipoProductoPrueba);
        when(tipoProductoRepository.tieneProductosAsociados(1)).thenReturn(false);
        doNothing().when(tipoProductoRepository).eliminar(1);

        assertDoesNotThrow(() -> {
            tipoProductoService.eliminar(1);
        });

        verify(tipoProductoRepository, times(1)).eliminar(1);
    }

    @Test
    void eliminar_CuandoTieneProductos_DebeLanzarExcepcion() {
        when(tipoProductoRepository.obtenerPorId(1)).thenReturn(tipoProductoPrueba);
        when(tipoProductoRepository.tieneProductosAsociados(1)).thenReturn(true);

        assertThrows(OperacionNoPermitidaException.class, () -> {
            tipoProductoService.eliminar(1);
        });

        verify(tipoProductoRepository, never()).eliminar(anyInt());
    }
}