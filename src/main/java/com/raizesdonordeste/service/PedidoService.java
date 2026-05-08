package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.ItemDTO;
import com.raizesdonordeste.dto.PedidoDTO;
import com.raizesdonordeste.dto.PedidoResponseDTO;
import com.raizesdonordeste.gateway.PagamentoGateway;
import com.raizesdonordeste.model.entity.*;
import com.raizesdonordeste.model.enums.CanalEnum;
import com.raizesdonordeste.model.enums.StatusEnum;
import com.raizesdonordeste.repository.PedidoRepository;
import com.raizesdonordeste.repository.ProdutoRepository;
import com.raizesdonordeste.repository.UnidadeRepository;
import com.raizesdonordeste.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final UsuarioRepository usuarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    private final EstoqueService estoqueService;
    private final FidelidadeService fidelidadeService;
    private final PromocaoService promocaoService;
    private final PagamentoGateway pagamentoGateway;
    private final AuditoriaService auditoriaService;

    public List<PedidoResponseDTO> filaCozinha() {
        return pedidoRepository.findByStatusIn(
                List.of(StatusEnum.PAGO, StatusEnum.EM_PREPARACAO)
        ).stream().map(this::toDTO).toList();
    }

    public List<PedidoResponseDTO> buscarPorCanal(CanalEnum canal) {
        return pedidoRepository.findByCanalOrigem(canal)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public PedidoResponseDTO processarCheckout(PedidoDTO dto, String emailCliente) {

        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUnidade(unidade);
        pedido.setStatus(StatusEnum.CRIADO);
        pedido.setCanalOrigem(CanalEnum.valueOf(dto.getCanalOrigem().toUpperCase()));
        pedido.setDataHora(LocalDateTime.now());

        List<ItemPedido> itens = new ArrayList<>();

        for (ItemDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            estoqueService.buscar(unidade.getId(), produto.getId());

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoAplicado(produto.getPrecoBase());

            itens.add(item);
        }

        pedido.setItens(itens);

        double total = itens.stream()
                .mapToDouble(i -> i.getPrecoAplicado() * i.getQuantidade())
                .sum();

        pedido.setTotalBruto(total);

        double totalComDesconto = promocaoService.aplicarDesconto(unidade, total);

        if (dto.isUsarPontos() && dto.getPontosUtilizados() != null) {

            fidelidadeService.validarResgate(cliente, dto.getPontosUtilizados());

            double desconto = fidelidadeService.calcularDesconto(dto.getPontosUtilizados());

            totalComDesconto -= desconto;

            pedido.setDescontoFidelidade(desconto);
        }

        pedido.setTotalFinal(Math.max(totalComDesconto, 0));

        Pedido salvo = pedidoRepository.save(pedido);

        auditoriaService.registrarLog(
                cliente.getId(),
                "CRIAR_PEDIDO",
                "pedido",
                salvo.getId(),
                salvo
        );

        return toDTO(salvo);
    }

    @Transactional
    public PedidoResponseDTO processarPagamento(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusEnum.CRIADO) {
            throw new RuntimeException("Pedido não pode ser pago");
        }

        if (pedido.getTransacaoGatewayId() != null) {
            throw new RuntimeException("Pedido já foi pago");
        }

        try {
            String transacao = pagamentoGateway.processarPagamento(pedido);

            pedido.setTransacaoGatewayId(transacao);
            pedido.setStatus(StatusEnum.PAGO);

            for (ItemPedido item : pedido.getItens()) {
                estoqueService.baixar(
                        pedido.getUnidade().getId(),
                        item.getProduto().getId(),
                        item.getQuantidade()
                );
            }

            if (pedido.getDescontoFidelidade() != null && pedido.getDescontoFidelidade() > 0) {
                int pontos = (int) (pedido.getDescontoFidelidade() / 0.1);
                fidelidadeService.debitarPontos(pedido.getCliente(), pontos);
            }

            int pontos = fidelidadeService.calcularPontos(pedido.getTotalFinal());
            fidelidadeService.adicionarPontos(pedido.getCliente(), pontos);

            pedidoRepository.save(pedido);

            auditoriaService.registrarLog(
                    pedido.getCliente().getId(),
                    "PAGAR_PEDIDO",
                    "pedido",
                    pedido.getId(),
                    pedido
            );

        } catch (Exception e) {
            pedido.setStatus(StatusEnum.CRIADO);
            pedidoRepository.save(pedido);
            throw new RuntimeException("Falha no pagamento: " + e.getMessage());
        }

        return toDTO(pedido);
    }

    @Transactional
    public void alterarStatus(Long id, StatusEnum status) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        validarTransicao(pedido.getStatus(), status);

        pedido.setStatus(status);

        pedidoRepository.save(pedido);

        auditoriaService.registrarLog(
                pedido.getCliente().getId(),
                "ALTERAR_STATUS",
                "pedido",
                pedido.getId(),
                status
        );
    }

    private void validarTransicao(StatusEnum atual, StatusEnum novo) {

        switch (atual) {
            case CRIADO -> {
                if (novo != StatusEnum.PAGO && novo != StatusEnum.CANCELADO)
                    throw new RuntimeException("Transição inválida");
            }
            case PAGO -> {
                if (novo != StatusEnum.EM_PREPARACAO && novo != StatusEnum.CANCELADO)
                    throw new RuntimeException("Transição inválida");
            }
            case EM_PREPARACAO -> {
                if (novo != StatusEnum.PRONTO)
                    throw new RuntimeException("Transição inválida");
            }
            case PRONTO -> {
                if (novo != StatusEnum.ENTREGUE)
                    throw new RuntimeException("Transição inválida");
            }
            default -> throw new RuntimeException("Pedido não pode ser alterado");
        }
    }

    public PedidoResponseDTO toDTO(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .status(pedido.getStatus().name())
                .totalFinal(pedido.getTotalFinal())
                .dataHora(pedido.getDataHora())
                .itens(
                        pedido.getItens().stream().map(item ->
                                PedidoResponseDTO.ItemResponseDTO.builder()
                                        .produtoNome(item.getProduto().getNome())
                                        .quantidade(item.getQuantidade())
                                        .preco(item.getPrecoAplicado())
                                        .build()
                        ).toList()
                )
                .build();
    }
}