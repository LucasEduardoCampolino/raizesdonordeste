package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.PedidoDTO;
import com.raizesdonordeste.dto.PedidoResponseDTO;
import com.raizesdonordeste.model.enums.CanalEnum;
import com.raizesdonordeste.model.enums.StatusEnum;
import com.raizesdonordeste.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> criar(
            @Valid @RequestBody PedidoDTO dto,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                service.processarCheckout(dto, auth.getName())
        );
    }

    @PostMapping("/{id}/pagar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PedidoResponseDTO> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.processarPagamento(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) CanalEnum canalPedido
    ) {
        if (canalPedido != null) {
            return ResponseEntity.ok(service.buscarPorCanal(canalPedido));
        }
        return ResponseEntity.ok(service.filaCozinha());
    }

    @PutMapping("/{id}/preparar")
    public ResponseEntity<Void> iniciarPreparo(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.EM_PREPARACAO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/pronto")
    public ResponseEntity<Void> marcarPronto(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.PRONTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<Void> entregar(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.ENTREGUE);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.alterarStatus(id, StatusEnum.CANCELADO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/fila-cozinha")
    public ResponseEntity<List<PedidoResponseDTO>> fila() {
        return ResponseEntity.ok(service.filaCozinha());
    }
}