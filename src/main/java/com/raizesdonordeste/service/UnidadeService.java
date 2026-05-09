package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.UnidadeDTO;
import com.raizesdonordeste.dto.UnidadeResponseDTO;
import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService {

    private final UnidadeRepository repository;

    public UnidadeService(UnidadeRepository repository) {
        this.repository = repository;
    }

    public UnidadeResponseDTO salvar(UnidadeDTO dto) {

        Unidade unidade = Unidade.builder()
                .nomeFantasia(dto.getNomeFantasia())
                .cnpj(dto.getCnpj())
                .endereco(dto.getEndereco())
                .ativo(dto.getAtivo())
                .build();

        return toDTO(repository.save(unidade));
    }

    public List<UnidadeResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private UnidadeResponseDTO toDTO(Unidade u) {
        return UnidadeResponseDTO.builder()
                .id(u.getId())
                .nomeFantasia(u.getNomeFantasia())
                .cnpj(u.getCnpj())
                .endereco(u.getEndereco())
                .ativo(u.getAtivo())
                .build();
    }
}