package com.raizesdonordeste.gateway;

import com.raizesdonordeste.model.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class PagamentoGatewayMock implements PagamentoGateway {

    private final Random random = new Random();

    @Override
    public String processarPagamento(Pedido pedido) {

        System.out.println("Processando pagamento do pedido " + pedido.getId());

        // cria uma aleatoriedade de falha no pagamento para testar o fluxo
        boolean falha = random.nextInt(10) < 2;

        if (falha) {
            System.out.println("Pagamento recusado");
            throw new RuntimeException("Pagamento recusado pelo gateway");
        }

        String transacao = UUID.randomUUID().toString();

        System.out.println("Pagamento aprovado: " + transacao);

        return transacao;
    }
}