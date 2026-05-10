package com.raizesdonordeste.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private Double precoBase;
    private Boolean sazonal;
    private Boolean ativo;
    private String categoriaNome;
}