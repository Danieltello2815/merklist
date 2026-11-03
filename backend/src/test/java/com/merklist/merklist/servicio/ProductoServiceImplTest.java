package com.merklist.merklist.servicio;

import com.merklist.merklist.excepcion.RecursoNoEncontradoException;
import com.merklist.merklist.modelo.Producto;
import com.merklist.merklist.modelo.TipoProducto;
import com.merklist.merklist.repositorio.ProductoRepository;
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
public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private TipoProductoRepository tipoProductoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto productoPrueba;
    private TipoProducto tipoProductoPrueba;

    @BeforeEach
    void setUp() {
        // Configuramos datos ficticios que se ejecutarán antes de cada prueba
        tipoProductoPrueba = new TipoProducto();
        tipoProductoPrueba.setId(1);
        tipoProductoPrueba.setNombre("Lácteos");

        productoPrueba = new Producto();
        productoPrueba.setId(1);
        productoPrueba.setNombre("Leche Entera");
        productoPrueba.setMarca("Colanta");
        productoPrueba.setTipoProductoId(1);
    }

    @Test
    void listar_DebeRetornarListaDeProductos() {
        when(productoRepository.listar()).thenReturn(Arrays.asList(productoPrueba));

        List<Producto> resultado = productoService.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(productoRepository, times(1)).listar();
    }

    @Test
    void obtenerPorId_CuandoExiste_DebeRetornarProducto() {
        when(productoRepository.obtenerPorId(1)).thenReturn(productoPrueba);

        Producto resultado = productoService.obtenerPorId(1);

        assertNotNull(resultado);
        assertEquals("Leche Entera", resultado.getNombre());
    }

    @Test
    void obtenerPorId_CuandoNoExiste_DebeLanzarExcepcion() {
        when(productoRepository.obtenerPorId(99)).thenReturn(null);

        assertThrows(RecursoNoEncontradoException.class, () -> {
            productoService.obtenerPorId(99);
        });
    }

    @Test
    void crear_ConDatosValidos_DebeCrearProducto() {
        // Simulamos que el tipo de producto existe y la BD retorna el producto creado
        when(tipoProductoRepository.obtenerPorId(1)).thenReturn(tipoProductoPrueba);
        when(productoRepository.crear(any(Producto.class))).thenReturn(productoPrueba);

        Producto resultado = productoService.crear(productoPrueba);

        assertNotNull(resultado);
        assertEquals("Leche Entera", resultado.getNombre());
        verify(productoRepository, times(1)).crear(productoPrueba);
    }

    @Test
    void crear_SinNombre_DebeLanzarExcepcion() {
        productoPrueba.setNombre("");

        assertThrows(IllegalArgumentException.class, () -> {
            productoService.crear(productoPrueba);
        });

        // Verificamos que NUNCA se llame a la base de datos si falla la validación
        verify(productoRepository, never()).crear(any());
    }

    @Test
    void actualizar_CuandoProductoExisteYDatosSonValidos_DebeActualizar() {
        when(productoRepository.obtenerPorId(1)).thenReturn(productoPrueba);
        when(tipoProductoRepository.obtenerPorId(1)).thenReturn(tipoProductoPrueba);
        when(productoRepository.actualizar(eq(1), any(Producto.class))).thenReturn(productoPrueba);

        Producto resultado = productoService.actualizar(1, productoPrueba);

        assertNotNull(resultado);
        verify(productoRepository, times(1)).actualizar(eq(1), any(Producto.class));
    }

    @Test
    void eliminar_CuandoExiste_DebeEliminarProducto() {
        when(productoRepository.obtenerPorId(1)).thenReturn(productoPrueba);
        doNothing().when(productoRepository).eliminar(1);

        assertDoesNotThrow(() -> {
            productoService.eliminar(1);
        });

        verify(productoRepository, times(1)).eliminar(1);
    }
}