package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.EstoqueDTO;
import com.raizesdonordeste.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService service;

    @PostMapping("/entrada")
    @PreAuthorize("hasAnyRole('ATENDENTE','GERENTE','ADMIN')")
    public ResponseEntity<EstoqueDTO> adicionar(@Valid @RequestBody EstoqueDTO dto) {
        return ResponseEntity.ok(service.adicionar(dto));
    }

    @PostMapping("/saida")
    @PreAuthorize("hasAnyRole('ATENDENTE','GERENTE','ADMIN')")
    public ResponseEntity<Void> baixar(@Valid @RequestBody EstoqueDTO dto) {
        service.baixar(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EstoqueDTO> buscar(
            @RequestParam Long unidadeId,
            @RequestParam Long produtoId
    ) {
        return ResponseEntity.ok(service.buscar(unidadeId, produtoId));
    }
}