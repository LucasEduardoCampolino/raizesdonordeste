package com.raizesdonordeste.controller;

import com.raizesdonordeste.model.entity.Promocao;
import com.raizesdonordeste.repository.PromocaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocoes")
public class PromocaoController {

    private final PromocaoRepository repository;

    public PromocaoController(PromocaoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Promocao> criar(@RequestBody Promocao promocao) {
        return ResponseEntity.ok(repository.save(promocao));
    }

    @GetMapping
    public ResponseEntity<List<Promocao>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}