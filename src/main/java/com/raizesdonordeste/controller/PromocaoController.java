package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.PromocaoDTO;
import com.raizesdonordeste.service.PromocaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocoes")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<PromocaoDTO> criar(@Valid @RequestBody PromocaoDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PromocaoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}