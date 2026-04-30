package com.raizesdonordeste.controller;

import com.raizesdonordeste.dto.PedidoDTO;
import com.raizesdonordeste.model.entity.Pedido;
import com.raizesdonordeste.model.enums.StatusEnum;
import com.raizesdonordeste.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoDTO dto) {
        return ResponseEntity.ok(service.processarCheckout(dto));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<Pedido> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(service.processarPagamento(id));
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
    public ResponseEntity<List<Pedido>> fila() {
        return ResponseEntity.ok(service.filaCozinha());
    }
}