package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.UnidadeDTO;
import com.raizesdonordeste.dto.UnidadeResponseDTO;
import com.raizesdonordeste.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<UnidadeResponseDTO> criar(@Valid @RequestBody UnidadeDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UnidadeResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}