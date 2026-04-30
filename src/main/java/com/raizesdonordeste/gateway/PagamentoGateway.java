package com.raizesdonordeste.gateway;

import com.raizesdonordeste.model.entity.Pedido;

public interface PagamentoGateway {

    String processarPagamento(Pedido pedido);

}