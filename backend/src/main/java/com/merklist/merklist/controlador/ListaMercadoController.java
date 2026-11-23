package com.merklist.merklist.controlador;

import com.merklist.merklist.modelo.ItemLista;
import com.merklist.merklist.modelo.ListaMercado;
import com.merklist.merklist.modelo.RegistroCompra;
import com.merklist.merklist.servicio.ListaMercadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ListaMercadoController {

    private final ListaMercadoService listaService;

    public ListaMercadoController(ListaMercadoService listaService) {
        this.listaService = listaService;
    }

    @PostMapping("/usuarios/{usuarioId}/listas")
    public ResponseEntity<ListaMercado> crear(@PathVariable int usuarioId,
                                              @RequestBody ListaMercado lista) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listaService.crear(usuarioId, lista));
    }

    @GetMapping("/usuarios/{usuarioId}/listas")
    public ResponseEntity<List<ListaMercado>> listarPorUsuario(@PathVariable int usuarioId) {
        return ResponseEntity.ok(listaService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/listas/{id}")
    public ResponseEntity<ListaMercado> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(listaService.obtenerPorId(id));
    }

    @PutMapping("/listas/{id}")
    public ResponseEntity<ListaMercado> actualizar(@PathVariable int id,
                                                   @RequestBody ListaMercado lista) {
        return ResponseEntity.ok(listaService.actualizar(id, lista));
    }

    @DeleteMapping("/listas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        listaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/listas/{listaId}/items")
    public ResponseEntity<ItemLista> agregarItem(@PathVariable int listaId,
                                                 @RequestBody ItemLista item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listaService.agregarItem(listaId, item));
    }

    @PutMapping("/listas/{listaId}/items/{itemId}/comprado")
    public ResponseEntity<ItemLista> marcarComprado(@PathVariable int listaId,
                                                    @PathVariable int itemId,
                                                    @RequestBody RegistroCompra compra) {
        return ResponseEntity.ok(listaService.marcarComprado(listaId, itemId, compra));
    }

    @DeleteMapping("/listas/{listaId}/items/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable int listaId, @PathVariable int itemId) {
        listaService.eliminarItem(listaId, itemId);
        return ResponseEntity.noContent().build();
    }
}
