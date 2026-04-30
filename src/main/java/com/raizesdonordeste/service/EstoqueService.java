package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Estoque;
import com.raizesdonordeste.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;

    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    public Estoque buscar(Long unidadeId, Long produtoId) {
        return repository.findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
    }

    public Estoque adicionar(Long unidadeId, Long produtoId, int quantidade) {
        Estoque estoque = repository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElse(null);

        if (estoque == null) {
            throw new RuntimeException("Estoque não existe ainda para esse produto/unidade");
        }

        estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        return repository.save(estoque);
    }

    public void baixar(Long unidadeId, Long produtoId, int quantidade) {

        Estoque estoque = buscar(unidadeId, produtoId);

        if (estoque.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);

        repository.save(estoque);
    }

    public void validarEBaixarEstoque(Long unidadeId, Long produtoId, int quantidade) {
        baixar(unidadeId, produtoId, quantidade);
    }
}
