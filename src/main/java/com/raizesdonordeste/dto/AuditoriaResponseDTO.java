package com.raizesdonordeste.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditoriaResponseDTO {
    private Long id;
    private Long usuarioId;
    private String acao;
    private String tabela;
    private Long registroId;
    private LocalDateTime dataHora;
}