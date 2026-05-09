package com.raizesdonordeste.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnidadeResponseDTO {
    private Long id;
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private Boolean ativo;
}