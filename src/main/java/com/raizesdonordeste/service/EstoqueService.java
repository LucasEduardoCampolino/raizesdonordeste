package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.EstoqueDTO;
import com.raizesdonordeste.model.entity.Estoque;
import com.raizesdonordeste.repository.EstoqueRepository;
import com.raizesdonordeste.repository.ProdutoRepository;
import com.raizesdonordeste.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.raizesdonordeste.exception.BusinessException;
import com.raizesdonordeste.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository repository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueDTO adicionar(EstoqueDTO dto) {
        Estoque estoque = repository
                .findByUnidadeIdAndProdutoId(dto.getUnidadeId(), dto.getProdutoId())
                .orElse(null);

        if (estoque == null) {
            estoque = new Estoque();

            estoque.setUnidade(
                    unidadeRepository.findById(dto.getUnidadeId())
                            .orElseThrow(() -> new NotFoundException("Unidade não encontrada"))
            );

            estoque.setProduto(
                    produtoRepository.findById(dto.getProdutoId())
                            .orElseThrow(() -> new NotFoundException("Produto não encontrado"))
            );

            estoque.setQuantidade(0);
        }

        estoque.setQuantidade(estoque.getQuantidade() + dto.getQuantidade());

        return toDTO(repository.save(estoque));
    }

    public void baixar(EstoqueDTO dto) {
        Estoque estoque = repository
                .findByUnidadeIdAndProdutoId(dto.getUnidadeId(), dto.getProdutoId())
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado"));

        if (estoque.getQuantidade() < dto.getQuantidade()) {
            throw new BusinessException("Estoque insuficiente");
        }

        estoque.setQuantidade(estoque.getQuantidade() - dto.getQuantidade());

        repository.save(estoque);
    }

    public EstoqueDTO buscar(Long unidadeId, Long produtoId) {
        Estoque estoque = repository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado"));

        return toDTO(estoque);
    }

    private EstoqueDTO toDTO(Estoque e) {
        EstoqueDTO dto = new EstoqueDTO();

        dto.setUnidadeId(e.getUnidade().getId());
        dto.setProdutoId(e.getProduto().getId());
        dto.setQuantidade(e.getQuantidade());

        return dto;
    }
}