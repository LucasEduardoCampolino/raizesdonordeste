package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    public String processarPagamento(Pedido pedido) {

        if (pedido.getTotalFinal() <= 0) {
            throw new RuntimeException("Valor inválido para pagamento");
        }

        return "TRANSACAO_" + System.currentTimeMillis();
    }
}