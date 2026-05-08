package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.EstoqueDTO;
import com.raizesdonordeste.model.entity.Estoque;
import com.raizesdonordeste.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @PostMapping("/entrada")
    public ResponseEntity<Estoque> adicionar(@RequestBody EstoqueDTO dto) {
        return ResponseEntity.ok(
                service.adicionar(
                        dto.getUnidadeId(),
                        dto.getProdutoId(),
                        dto.getQuantidade()
                )
        );
    }

    @PostMapping("/saida")
    public ResponseEntity<Void> baixar(@RequestBody EstoqueDTO dto) {
        service.baixar(
                dto.getUnidadeId(),
                dto.getProdutoId(),
                dto.getQuantidade()
        );
        return ResponseEntity.noContent().build();
    }
}