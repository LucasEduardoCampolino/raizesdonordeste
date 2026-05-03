package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Estoque;
import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.model.entity.Produto;
import com.raizesdonordeste.repository.EstoqueRepository;
import com.raizesdonordeste.repository.UnidadeRepository;
import com.raizesdonordeste.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository repository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final AuditoriaService auditoriaService;

    public Estoque buscar(Long unidadeId, Long produtoId) {
        return repository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
    }

    public Estoque adicionar(Long unidadeId, Long produtoId, int quantidade) {

        Estoque estoque = repository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElse(null);

        if (estoque == null) {

            Unidade unidade = unidadeRepository.findById(unidadeId)
                    .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            estoque = Estoque.builder()
                    .unidade(unidade)
                    .produto(produto)
                    .quantidade(0)
                    .build();
        }

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);

        Estoque salvo = repository.save(estoque);

        auditoriaService.registrarLog(
                null,
                "ENTRADA_ESTOQUE",
                "estoque",
                salvo.getId(),
                salvo
        );

        return salvo;
    }

    public void baixar(Long unidadeId, Long produtoId, int quantidade) {

        Estoque estoque = buscar(unidadeId, produtoId);

        if (estoque.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);

        repository.save(estoque);

        auditoriaService.registrarLog(
                null,
                "SAIDA_ESTOQUE",
                "estoque",
                estoque.getId(),
                estoque
        );
    }

    public void validarEBaixarEstoque(Long unidadeId, Long produtoId, int quantidade) {
        baixar(unidadeId, produtoId, quantidade);
    }
}