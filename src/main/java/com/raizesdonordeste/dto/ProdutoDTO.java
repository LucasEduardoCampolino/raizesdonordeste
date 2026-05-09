package com.raizesdonordeste.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProdutoDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private Double precoBase;

    @NotNull(message = "Sazonal é obrigatório")
    private Boolean sazonal;

    @NotNull(message = "Ativo é obrigatório")
    private Boolean ativo;

    private Long categoriaId;
}