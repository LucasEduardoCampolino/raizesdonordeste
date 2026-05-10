package com.raizesdonordeste.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstoqueDTO {

    @NotNull(message = "UnidadeId é obrigatório")
    private Long unidadeId;

    @NotNull(message = "ProdutoId é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que 0")
    private Integer quantidade;
}