package com.raizesdonordeste.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromocaoDTO {

    private Long id;

    @NotBlank(message = "Nome da promoção é obrigatório")
    private String nome;

    @NotNull(message = "Desconto é obrigatório")
    @Positive(message = "Desconto deve ser positivo")
    private Double desconto;

    @NotNull(message = "Data início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data fim é obrigatória")
    private LocalDate dataFim;

    @NotNull(message = "Status ativo é obrigatório")
    private Boolean ativo;

    private Long unidadeId;
}