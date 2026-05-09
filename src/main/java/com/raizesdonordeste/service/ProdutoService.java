package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.ProdutoDTO;
import com.raizesdonordeste.dto.ProdutoResponseDTO;
import com.raizesdonordeste.model.entity.Categoria;
import com.raizesdonordeste.model.entity.Produto;
import com.raizesdonordeste.repository.CategoriaRepository;
import com.raizesdonordeste.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProdutoResponseDTO salvar(ProdutoDTO dto) {

        Categoria categoria = null;
        if (dto.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        }

        Produto produto = Produto.builder()
                .nome(dto.getNome())
                .precoBase(dto.getPrecoBase())
                .sazonal(dto.getSazonal())
                .ativo(dto.getAtivo())
                .categoria(categoria)
                .build();

        return toDTO(repository.save(produto));
    }

    public List<ProdutoResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private ProdutoResponseDTO toDTO(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .precoBase(produto.getPrecoBase())
                .sazonal(produto.getSazonal())
                .ativo(produto.getAtivo())
                .categoriaNome(
                        produto.getCategoria() != null
                                ? produto.getCategoria().getNome()
                                : null
                )
                .build();
    }
}