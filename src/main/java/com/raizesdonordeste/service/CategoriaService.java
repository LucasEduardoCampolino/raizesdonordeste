package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.CategoriaDTO;
import com.raizesdonordeste.dto.CategoriaResponseDTO;
import com.raizesdonordeste.model.entity.Categoria;
import com.raizesdonordeste.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public CategoriaResponseDTO salvar(CategoriaDTO dto) {

        Categoria categoria = Categoria.builder()
                .nome(dto.getNome())
                .build();

        return toDTO(repository.save(categoria));
    }

    public List<CategoriaResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private CategoriaResponseDTO toDTO(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .build();
    }
}