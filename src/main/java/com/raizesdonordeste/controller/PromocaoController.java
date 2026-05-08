package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.PromocaoDTO;
import com.raizesdonordeste.model.entity.Promocao;
import com.raizesdonordeste.repository.PromocaoRepository;
import com.raizesdonordeste.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocoes")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoRepository repository;
    private final UnidadeRepository unidadeRepository;

    @PostMapping
    public ResponseEntity<PromocaoDTO> criar(@RequestBody PromocaoDTO dto) {

        Promocao promocao = new Promocao();
        promocao.setNomeEvento(dto.getNome());
        promocao.setValorPromocional(dto.getDesconto());
        promocao.setDataInicio(dto.getDataInicio());
        promocao.setDataFim(dto.getDataFim());
        promocao.setAtivo(dto.getAtivo());

        if (dto.getUnidadeId() != null) {
            promocao.setUnidade(
                    unidadeRepository.findById(dto.getUnidadeId())
                            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"))
            );
        }

        Promocao salvo = repository.save(promocao);

        dto.setId(salvo.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<PromocaoDTO>> listar() {
        return ResponseEntity.ok(
                repository.findAll().stream().map(p -> {

                    PromocaoDTO dto = new PromocaoDTO();
                    dto.setId(p.getId());
                    dto.setNome(p.getNomeEvento());
                    dto.setDesconto(p.getValorPromocional());
                    dto.setDataInicio(p.getDataInicio());
                    dto.setDataFim(p.getDataFim());
                    dto.setAtivo(p.getAtivo());
                    dto.setUnidadeId(
                            p.getUnidade() != null ? p.getUnidade().getId() : null
                    );

                    return dto;
                }).toList()
        );
    }
}