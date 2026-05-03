package com.raizesdonordeste.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {

    @NotNull(message = "clienteId é obrigatório")
    private Long clienteId;

    @NotNull(message = "unidadeId é obrigatório")
    private Long unidadeId;

    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<ItemDTO> itens;

    @NotBlank(message = "Canal de origem é obrigatório")
    private String canalOrigem;

    private boolean usarPontos;

    private Integer pontosUtilizados;
}