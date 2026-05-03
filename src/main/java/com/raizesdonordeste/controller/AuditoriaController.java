package com.raizesdonordeste.controller;

import com.raizesdonordeste.model.entity.AuditoriaLog;
import com.raizesdonordeste.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaLogRepository repository;

    @GetMapping
    public ResponseEntity<List<AuditoriaLog>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}