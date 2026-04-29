package com.raizesdonordeste.controller;

import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.service.UnidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService service;

    public UnidadeController(UnidadeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Unidade> criar(@RequestBody Unidade unidade) {
        return ResponseEntity.ok(service.salvar(unidade));
    }

    @GetMapping
    public ResponseEntity<List<Unidade>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}