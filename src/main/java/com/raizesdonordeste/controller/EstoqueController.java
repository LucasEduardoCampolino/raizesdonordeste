package com.raizesdonordeste.controller;

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
    public ResponseEntity<Estoque> adicionar(
            @RequestParam Long unidadeId,
            @RequestParam Long produtoId,
            @RequestParam int quantidade
    ) {
        return ResponseEntity.ok(service.adicionar(unidadeId, produtoId, quantidade));
    }

    @PostMapping("/saida")
    public ResponseEntity<Void> baixar(
            @RequestParam Long unidadeId,
            @RequestParam Long produtoId,
            @RequestParam int quantidade
    ) {
        service.baixar(unidadeId, produtoId, quantidade);
        return ResponseEntity.ok().build();
    }
}