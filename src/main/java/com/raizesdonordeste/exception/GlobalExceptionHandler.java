package com.raizesdonordeste.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(
                Map.of(
                        "erro", ex.getMessage(),
                        "tipo", "REGRA_NEGOCIO",
                        "timestamp", LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity.status(500).body(
                Map.of(
                        "erro", "Erro interno do servidor",
                        "tipo", "INTERNAL_ERROR",
                        "timestamp", LocalDateTime.now()
                )
        );
    }
}