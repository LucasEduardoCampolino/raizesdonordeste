package com.raizesdonordeste.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoDTO {

    @NotNull(message = "unidadeId é obrigatório")
    private Long unidadeId;

    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<@Valid ItemDTO> itens;

    @NotBlank(message = "Canal de origem é obrigatório")
    private String canalOrigem;

    private boolean usarPontos;

    private Integer pontosUtilizados;
}