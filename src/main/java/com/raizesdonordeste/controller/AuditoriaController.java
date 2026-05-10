package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.AuditoriaResponseDTO;
import com.raizesdonordeste.model.entity.AuditoriaLog;
import com.raizesdonordeste.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaLogRepository repository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditoriaResponseDTO>> listar() {
        return ResponseEntity.ok(
                repository.findAll()
                        .stream()
                        .map(log -> AuditoriaResponseDTO.builder()
                                .id(log.getId())
                                .usuarioId(log.getUsuarioId())
                                .acao(log.getAcao())
                                .tabela(log.getTabelaAfetada())
                                .registroId(log.getRegistroId())
                                .dataHora(log.getDataHora())
                                .build()
                        ).toList()
        );
    }
}