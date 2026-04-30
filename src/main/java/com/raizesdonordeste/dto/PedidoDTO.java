package com.raizesdonordeste.dto;

import com.raizesdonordeste.model.enums.CanalEnum;
import lombok.Data;

import java.util.List;

@Data
public class PedidoDTO {
    private Long clienteId;
    private Long unidadeId;
    private List<ItemDTO> itens;
    private CanalEnum canalOrigem;
    private boolean usarPontos;
    private int pontosUtilizados;
}
