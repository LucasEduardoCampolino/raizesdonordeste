package com.raizesdonordeste.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String erro;
    private String tipo;
    private LocalDateTime timestamp;

    public ErrorResponse(String erro, String tipo) {
        this.erro = erro;
        this.tipo = tipo;
        this.timestamp = LocalDateTime.now();
    }

    public String getErro() {
        return erro;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}