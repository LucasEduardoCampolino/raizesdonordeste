package com.raizesdonordeste.gateway;

import com.raizesdonordeste.model.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PagamentoGatewayMock implements PagamentoGateway {

    @Override
    public String processarPagamento(Pedido pedido) {

        // Simula sucesso sempre
        System.out.println("💳 Processando pagamento do pedido " + pedido.getId());

        return UUID.randomUUID().toString(); // id da transação fake
    }
}