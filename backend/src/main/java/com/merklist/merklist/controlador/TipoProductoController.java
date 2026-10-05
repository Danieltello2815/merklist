package com.merklist.merklist.controlador;

import com.merklist.merklist.modelo.TipoProducto;
import com.merklist.merklist.servicio.TipoProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-producto")
public class TipoProductoController {

    private final TipoProductoService tipoProductoService;

    public TipoProductoController(TipoProductoService tipoProductoService) {
        this.tipoProductoService = tipoProductoService;
    }

    @GetMapping
    public ResponseEntity<List<TipoProducto>> listar() {
        return ResponseEntity.ok(tipoProductoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoProducto> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(tipoProductoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoProducto> crear(@RequestBody TipoProducto tipoProducto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoProductoService.crear(tipoProducto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoProducto> actualizar(@PathVariable int id, @RequestBody TipoProducto tipoProducto) {
        return ResponseEntity.ok(tipoProductoService.actualizar(id, tipoProducto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        tipoProductoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}