package com.merklist.merklist.controlador;

import com.merklist.merklist.modelo.TipoProducto;
import com.merklist.merklist.servicio.TipoProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-producto")
@Tag(name = "Tipos de Producto", description = "Operaciones para la gestión de las categorías o tipos de productos")
public class TipoProductoController {

    private final TipoProductoService tipoProductoService;

    public TipoProductoController(TipoProductoService tipoProductoService) {
        this.tipoProductoService = tipoProductoService;
    }

    @Operation(summary = "Listar todos los tipos de producto", description = "Obtiene una lista completa de las categorías registradas")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de producto obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<TipoProducto>> listar() {
        return ResponseEntity.ok(tipoProductoService.listar());
    }

    @Operation(summary = "Obtener tipo de producto por ID", description = "Busca y retorna un tipo de producto específico mediante su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoProducto> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(tipoProductoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear un tipo de producto", description = "Registra una nueva categoría en la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de producto duplicado")
    })
    @PostMapping
    public ResponseEntity<TipoProducto> crear(@RequestBody TipoProducto tipoProducto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoProductoService.crear(tipoProducto));
    }

    @Operation(summary = "Actualizar un tipo de producto", description = "Modifica los datos de una categoría existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoProducto> actualizar(@PathVariable int id, @RequestBody TipoProducto tipoProducto) {
        return ResponseEntity.ok(tipoProductoService.actualizar(id, tipoProducto));
    }

    @Operation(summary = "Eliminar un tipo de producto", description = "Elimina una categoría del catálogo mediante su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo de producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        tipoProductoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}