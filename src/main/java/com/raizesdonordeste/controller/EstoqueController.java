package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.EstoqueDTO;
import com.raizesdonordeste.model.entity.Estoque;
import com.raizesdonordeste.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService service;

    public EstoqueController(EstoqueService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Estoque> consultar(
            @RequestParam Long unidadeId,
            @RequestParam Long produtoId
    ) {
        return ResponseEntity.ok(service.buscar(unidadeId, produtoId));
    }

    @PostMapping("/entrada")
    public ResponseEntity<Estoque> adicionar(@RequestBody EstoqueDTO dto) {

        Estoque estoque = service.adicionar(
                dto.getUnidadeId(),
                dto.getProdutoId(),
                dto.getQuantidade()
        );

        return ResponseEntity.ok(estoque);
    }

    @PostMapping("/saida")
    public ResponseEntity<Void> baixar(@RequestBody EstoqueDTO dto) {

        service.baixar(
                dto.getUnidadeId(),
                dto.getProdutoId(),
                dto.getQuantidade()
        );

        return ResponseEntity.ok().build();
    }
}