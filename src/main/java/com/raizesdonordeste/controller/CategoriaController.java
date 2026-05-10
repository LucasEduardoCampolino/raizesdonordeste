package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.CategoriaDTO;
import com.raizesdonordeste.dto.CategoriaResponseDTO;
import com.raizesdonordeste.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(service.salvar(dto));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}