package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Pedido;
import com.raizesdonordeste.model.entity.Promocao;
import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.repository.PromocaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromocaoService {

    private final PromocaoRepository repository;

    public PromocaoService(PromocaoRepository repository) {
        this.repository = repository;
    }

    public List<Promocao> buscarPromocoesAtivas(Unidade unidade) {
        LocalDateTime agora = LocalDateTime.now();

        return repository.findByUnidadeIdAndAtivoTrueAndDataInicioBeforeAndDataFimAfter(
                unidade.getId(),
                agora,
                agora
        );
    }

    public double aplicarDesconto(Unidade unidade, double totalBruto) {

        List<Promocao> promocoes = buscarPromocoesAtivas(unidade);

        if (promocoes.isEmpty()) {
            return totalBruto;
        }

        Promocao promo = promocoes.get(0);

        double totalComDesconto = totalBruto - promo.getValorPromocional();

        return Math.max(totalComDesconto, 0);
    }
}