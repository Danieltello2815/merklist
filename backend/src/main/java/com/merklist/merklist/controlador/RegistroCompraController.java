package com.merklist.merklist.controlador;

import com.merklist.merklist.modelo.RegistroCompra;
import com.merklist.merklist.servicio.RegistroCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registros-compra")
@Tag(name = "Registro de Compras", description = "Operaciones para gestionar el historial de compras y precios de los productos")
public class RegistroCompraController {

    private final RegistroCompraService registroCompraService;

    public RegistroCompraController(RegistroCompraService registroCompraService) {
        this.registroCompraService = registroCompraService;
    }

    @Operation(summary = "Listar todos los registros de compra", description = "Obtiene el historial completo de todas las compras registradas en el sistema")
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    @GetMapping
    public ResponseEntity<List<RegistroCompra>> listar() {
        return ResponseEntity.ok(registroCompraService.listar());
    }

    @Operation(summary = "Obtener registro de compra por ID", description = "Busca y retorna un registro de compra específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RegistroCompra> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(registroCompraService.obtenerPorId(id));
    }

    @Operation(summary = "Listar registros por producto", description = "Obtiene todo el historial de compras asociado a un producto específico")
    @ApiResponse(responseCode = "200", description = "Historial del producto obtenido exitosamente")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<RegistroCompra>> listarPorProducto(@PathVariable int productoId) {
        return ResponseEntity.ok(registroCompraService.listarPorProductoId(productoId));
    }

    @Operation(summary = "Crear un registro de compra", description = "Registra una nueva compra con su precio, fecha y lugar")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro de compra creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<RegistroCompra> crear(@RequestBody RegistroCompra registroCompra) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroCompraService.crear(registroCompra));
    }

    @Operation(summary = "Actualizar un registro de compra", description = "Modifica los datos de un registro de compra existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RegistroCompra> actualizar(@PathVariable int id, @RequestBody RegistroCompra registroCompra) {
        return ResponseEntity.ok(registroCompraService.actualizar(id, registroCompra));
    }

    @Operation(summary = "Eliminar un registro de compra", description = "Elimina un registro del historial mediante su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        registroCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener variación de precio", description = "Analiza el historial de un producto y calcula si subió o bajó de precio respecto a compras anteriores")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Análisis de variación calculado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado o sin historial suficiente")
    })
    @GetMapping("/variacion-precio/producto/{productoId}")
    public ResponseEntity<Map<String, Object>> obtenerVariacionPrecio(@PathVariable int productoId) {
        double variacion = registroCompraService.calcularVariacionPrecio(productoId);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("productoId", productoId);
        respuesta.put("variacionPrecio", variacion);

        if (variacion > 0) {
            respuesta.put("mensaje", "El precio subió " + variacion + " respecto a la última compra.");
        } else if (variacion < 0) {
            respuesta.put("mensaje", "El precio bajó " + Math.abs(variacion) + " respecto a la última compra.");
        } else {
            respuesta.put("mensaje", "El precio se mantiene igual o no hay suficiente historial para comparar.");
        }

        return ResponseEntity.ok(respuesta);
    }
}
