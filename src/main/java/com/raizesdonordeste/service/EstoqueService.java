package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Estoque;
import com.raizesdonordeste.model.entity.Produto;
import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.repository.EstoqueRepository;
import com.raizesdonordeste.repository.ProdutoRepository;
import com.raizesdonordeste.repository.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueService(
            EstoqueRepository repository,
            UnidadeRepository unidadeRepository,
            ProdutoRepository produtoRepository
    ) {
        this.repository = repository;
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
    }

    public Estoque buscar(Long unidadeId, Long produtoId) {
        return repository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
    }

    @Transactional
    public Estoque adicionar(Long unidadeId, Long produtoId, int quantidade) {

        Estoque estoque = repository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
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

        return salvo;
    }

    @Transactional
    public void baixar(Long unidadeId, Long produtoId, int quantidade) {

        Estoque estoque = buscar(unidadeId, produtoId);

        if (estoque.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);

        repository.save(estoque);
        
        Estoque salvo = repository.save(estoque);

        return;
    }
}