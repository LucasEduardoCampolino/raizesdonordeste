package com.raizesdonordeste.service;

import com.raizesdonordeste.dto.ItemDTO;
import com.raizesdonordeste.dto.PedidoDTO;
import com.raizesdonordeste.gateway.PagamentoGateway;
import com.raizesdonordeste.model.entity.*;
import com.raizesdonordeste.model.enums.StatusEnum;
import com.raizesdonordeste.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueService estoqueService;
    private final PagamentoGateway pagamentoGateway;
    private final FidelidadeService fidelidadeService;

    public PedidoService(
            PedidoRepository repository,
            UsuarioRepository usuarioRepository,
            UnidadeRepository unidadeRepository,
            ProdutoRepository produtoRepository,
            EstoqueService estoqueService,
            PagamentoGateway pagamentoGateway,
            FidelidadeService fidelidadeService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.unidadeRepository = unidadeRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
        this.pagamentoGateway = pagamentoGateway;
        this.fidelidadeService = fidelidadeService;
    }

    @Transactional
    public Pedido processarCheckout(PedidoDTO dto) {

        Usuario cliente = usuarioRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Unidade unidade = unidadeRepository.findById(dto.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        List<ItemPedido> itens = new ArrayList<>();
        double totalBruto = 0;

        for (ItemDTO itemDTO : dto.getItens()) {

            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            double preco = produto.getPrecoBase();

            ItemPedido item = ItemPedido.builder()
                    .produto(produto)
                    .quantidade(itemDTO.getQuantidade())
                    .precoAplicado(preco)
                    .build();

            totalBruto += preco * itemDTO.getQuantidade();
            itens.add(item);
        }

        double desconto = 0;

        if (dto.isUsarPontos()) {
            fidelidadeService.validarResgate(cliente, dto.getPontosUtilizados());
            desconto = fidelidadeService.calcularDesconto(dto.getPontosUtilizados());
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .unidade(unidade)
                .dataHora(LocalDateTime.now())
                .totalBruto(totalBruto)
                .descontoFidelidade(desconto)
                .totalFinal(totalBruto - desconto)
                .status(StatusEnum.CRIADO)
                .canalOrigem(dto.getCanalOrigem())
                .build();

        itens.forEach(i -> i.setPedido(pedido));
        pedido.setItens(itens);

        return repository.save(pedido);
    }

    @Transactional
    public Pedido processarPagamento(Long pedidoId) {

        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusEnum.CRIADO) {
            throw new RuntimeException("Pedido não pode ser pago");
        }

        try {
            for (ItemPedido item : pedido.getItens()) {
                estoqueService.validarEBaixarEstoque(
                        pedido.getUnidade().getId(),
                        item.getProduto().getId(),
                        item.getQuantidade()
                );
            }

            String transacaoId = pagamentoGateway.processarPagamento(pedido);

            Usuario cliente = pedido.getCliente();

            if (pedido.getDescontoFidelidade() > 0) {
                int pontosUsados = (int) (pedido.getDescontoFidelidade() / 0.1);
                fidelidadeService.debitarPontos(cliente, pontosUsados);
            }

            int pontosGanhos = fidelidadeService.calcularPontos(pedido.getTotalFinal());
            fidelidadeService.adicionarPontos(cliente, pontosGanhos);

            pedido.setStatus(StatusEnum.PAGO);
            pedido.setTransacaoGatewayId(transacaoId);

            return repository.save(pedido);

        } catch (Exception e) {
            pedido.setStatus(StatusEnum.CANCELADO);
            repository.save(pedido);
            throw new RuntimeException("Pagamento falhou");
        }
    }

    public void alterarStatus(Long id, StatusEnum novoStatus) {

        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        StatusEnum atual = pedido.getStatus();

        switch (atual) {

            case PAGO -> {
                if (novoStatus != StatusEnum.EM_PREPARACAO) {
                    throw new RuntimeException("Pedido pago só pode ir para EM_PREPARACAO");
                }
            }

            case EM_PREPARACAO -> {
                if (novoStatus != StatusEnum.PRONTO) {
                    throw new RuntimeException("Pedido em preparação só pode ir para PRONTO");
                }
            }

            case PRONTO -> {
                if (novoStatus != StatusEnum.ENTREGUE) {
                    throw new RuntimeException("Pedido pronto só pode ser ENTREGUE");
                }
            }

            default -> throw new RuntimeException("Transição de status inválida");
        }

        pedido.setStatus(novoStatus);
        repository.save(pedido);
    }

    public List<Pedido> filaCozinha() {
        return repository.findAll().stream()
                .filter(p -> p.getStatus() == StatusEnum.PAGO
                        || p.getStatus() == StatusEnum.EM_PREPARACAO)
                .toList();
    }
}