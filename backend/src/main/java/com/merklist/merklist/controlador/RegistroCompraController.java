package com.merklist.merklist.controlador;

import com.merklist.merklist.modelo.RegistroCompra;
import com.merklist.merklist.servicio.RegistroCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registros-compra")
public class RegistroCompraController {

    private final RegistroCompraService registroCompraService;

    public RegistroCompraController(RegistroCompraService registroCompraService) {
        this.registroCompraService = registroCompraService;
    }

    @GetMapping
    public ResponseEntity<List<RegistroCompra>> listar() {
        return ResponseEntity.ok(registroCompraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroCompra> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(registroCompraService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<RegistroCompra>> listarPorProducto(@PathVariable int productoId) {
        return ResponseEntity.ok(registroCompraService.listarPorProductoId(productoId));
    }

    @PostMapping
    public ResponseEntity<RegistroCompra> crear(@RequestBody RegistroCompra registroCompra) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroCompraService.crear(registroCompra));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroCompra> actualizar(@PathVariable int id, @RequestBody RegistroCompra registroCompra) {
        return ResponseEntity.ok(registroCompraService.actualizar(id, registroCompra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        registroCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

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
