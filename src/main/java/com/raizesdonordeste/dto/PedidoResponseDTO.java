package com.raizesdonordeste.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PedidoResponseDTO {

    private Long id;
    private String status;
    private Double totalFinal;
    private LocalDateTime dataHora;

    private List<ItemResponseDTO> itens;

    @Data
    @Builder
    public static class ItemResponseDTO {
        private String produtoNome;
        private Integer quantidade;
        private Double preco;
    }
}