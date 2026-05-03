package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.ItemDTO;
import com.raizesdonordeste.dto.PedidoDTO;
import com.raizesdonordeste.model.entity.*;
import com.raizesdonordeste.model.enums.CanalEnum;
import com.raizesdonordeste.model.enums.StatusEnum;
import com.raizesdonordeste.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PagamentoService pagamentoService;
    private final AuditoriaService auditoriaService;

    private CanalEnum parseCanal(String canal) {
        try {
            return CanalEnum.valueOf(canal.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Canal de origem inválido");
        }
    }

    @Transactional
    public Pedido processarCheckout(PedidoDTO dto, String emailLogado) {

        Usuario cliente = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUnidade(unidade);
        pedido.setStatus(StatusEnum.CRIADO);
        pedido.setCanalOrigem(parseCanal(dto.getCanalOrigem()));
        pedido.setDataHora(LocalDateTime.now());

        List<ItemPedido> itens = new ArrayList<>();

        for (ItemDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            Estoque estoque = estoqueService.buscar(unidade.getId(), produto.getId());

            if (estoque.getQuantidade() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para produto: " + produto.getNome());
            }

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoAplicado(produto.getPrecoBase());

            itens.add(item);
        }

        pedido.setItens(itens);

        promocaoService.aplicarDescontoSazonal(pedido);

        double descontoFidelidade = 0;

        if (dto.isUsarPontos()) {

            int pontosUsar = dto.getPontosUtilizados() != null
                    ? dto.getPontosUtilizados()
                    : 0;

            if (pontosUsar > 0) {
                fidelidadeService.validarResgate(cliente, pontosUsar);
                descontoFidelidade = fidelidadeService.calcularDesconto(pontosUsar);
                fidelidadeService.debitarPontos(cliente, pontosUsar);
            }
        }

        double totalFinal = Math.max(pedido.getTotalFinal() - descontoFidelidade, 0);

        pedido.setDescontoFidelidade(descontoFidelidade);
        pedido.setTotalFinal(totalFinal);

        Pedido salvo = pedidoRepository.save(pedido);

        auditoriaService.registrarLog(
                cliente.getId(),
                "CRIAR_PEDIDO",
                "pedido",
                salvo.getId(),
                salvo
        );

        return salvo;
    }

    @Transactional
    public Pedido processarPagamento(Long id) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusEnum.CRIADO) {
            throw new RuntimeException("Pedido não pode ser pago");
        }

        String transacao = pagamentoService.processarPagamento(pedido);

        pedido.setTransacaoGatewayId(transacao);
        pedido.setStatus(StatusEnum.PAGO);

        for (ItemPedido item : pedido.getItens()) {
            estoqueService.baixar(
                    pedido.getUnidade().getId(),
                    item.getProduto().getId(),
                    item.getQuantidade()
            );
        }

        Usuario cliente = pedido.getCliente();
        int pontos = fidelidadeService.calcularPontos(pedido.getTotalFinal());
        fidelidadeService.adicionarPontos(cliente, pontos);

        Pedido atualizado = pedidoRepository.save(pedido);

        auditoriaService.registrarLog(
                cliente.getId(),
                "PAGAR_PEDIDO",
                "pedido",
                atualizado.getId(),
                atualizado
        );

        return atualizado;
    }

    public List<Pedido> filaCozinha() {
        return pedidoRepository.findByStatusIn(
                List.of(StatusEnum.PAGO, StatusEnum.EM_PREPARACAO)
        );
    }

    public void alterarStatus(Long id, StatusEnum novoStatus) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        StatusEnum atual = pedido.getStatus();

        if (atual == StatusEnum.CANCELADO) {
            throw new RuntimeException("Pedido já cancelado");
        }

        boolean transicaoValida =
                (atual == StatusEnum.PAGO && novoStatus == StatusEnum.EM_PREPARACAO) ||
                (atual == StatusEnum.EM_PREPARACAO && novoStatus == StatusEnum.PRONTO) ||
                (atual == StatusEnum.PRONTO && novoStatus == StatusEnum.ENTREGUE);

        if (transicaoValida || novoStatus == StatusEnum.CANCELADO) {

            pedido.setStatus(novoStatus);
            pedidoRepository.save(pedido);

            auditoriaService.registrarLog(
                    pedido.getCliente().getId(),
                    "ALTERAR_STATUS_" + novoStatus,
                    "pedido",
                    pedido.getId(),
                    null
            );

            return;
        }

        throw new RuntimeException("Transição de status inválida");
    }
}