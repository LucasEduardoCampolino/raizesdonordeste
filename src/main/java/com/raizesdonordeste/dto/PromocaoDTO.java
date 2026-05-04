package com.raizesdonordeste.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PromocaoDTO {

    private Long id;

    private String nome;

    private Double desconto;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean ativo;
    private Long unidadeId;
}