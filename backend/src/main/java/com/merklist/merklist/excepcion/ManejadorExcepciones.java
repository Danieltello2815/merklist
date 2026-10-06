package com.merklist.merklist.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException e) {
        return construir(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> duplicado(RecursoDuplicadoException e) {
        return construir(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<Map<String, Object>> operacionNoPermitida(OperacionNoPermitidaException e) {
        return construir(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> solicitudInvalida(IllegalArgumentException e) {
        return construir(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> errorInterno(RuntimeException e) {
        return construir(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<Map<String, Object>> construir(HttpStatus estado, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", estado.value());
        cuerpo.put("mensaje", mensaje);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}