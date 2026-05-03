package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.PedidoDTO;
import com.raizesdonordeste.model.entity.Pedido;
import com.raizesdonordeste.model.enums.StatusEnum;
import com.raizesdonordeste.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Pedido> criar(
            @RequestBody @Valid PedidoDTO dto,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                service.processarCheckout(dto, auth.getName())
        );
    }

    @PostMapping("/{id}/pagar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Pedido> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.processarPagamento(id));
    }

    @PutMapping("/{id}/preparar")
    @PreAuthorize("hasAnyRole('ATENDENTE','COZINHA','GERENTE','ADMIN')")
    public ResponseEntity<Void> iniciarPreparo(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.EM_PREPARACAO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/pronto")
    @PreAuthorize("hasAnyRole('COZINHA','GERENTE','ADMIN')")
    public ResponseEntity<Void> marcarPronto(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.PRONTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/entregar")
    @PreAuthorize("hasAnyRole('ATENDENTE','GERENTE','ADMIN')")
    public ResponseEntity<Void> entregar(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.ENTREGUE);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('GERENTE','ADMIN')")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.CANCELADO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fila-cozinha")
    @PreAuthorize("hasAnyRole('COZINHA','GERENTE','ADMIN')")
    public ResponseEntity<List<Pedido>> fila() {
        return ResponseEntity.ok(service.filaCozinha());
    }
}