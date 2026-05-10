package com.raizesdonordeste.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
}