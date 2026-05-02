package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.UsuarioDTO;
import com.raizesdonordeste.dto.UsuarioResponseDTO;
import com.raizesdonordeste.model.enums.PerfilEnum;
import com.raizesdonordeste.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.toDTO(service.criar(dto)));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> criarAdmin(
            @RequestBody UsuarioDTO dto,
            @RequestParam PerfilEnum perfil
    ) {
        return ResponseEntity.ok(
                service.toDTO(service.criarComPerfil(dto, perfil))
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(
                service.listarTodos().stream().map(service::toDTO).toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id.toString() == authentication.name or hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.toDTO(service.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto
    ) {
        return ResponseEntity.ok(service.toDTO(service.atualizar(id, dto)));
    }

    @PutMapping("/{id}/perfil")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(
            @PathVariable Long id,
            @RequestParam PerfilEnum perfil
    ) {
        return ResponseEntity.ok(
                service.toDTO(service.atualizarPerfil(id, perfil))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}