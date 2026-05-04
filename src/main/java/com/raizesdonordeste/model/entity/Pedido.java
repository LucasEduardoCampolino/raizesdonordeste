package com.raizesdonordeste.model.entity;

import com.raizesdonordeste.model.enums.CanalEnum;
import com.raizesdonordeste.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    private LocalDateTime dataHora;

    private Double totalBruto;
    private Double descontoFidelidade;
    private Double totalFinal;
    private String transacaoGatewayId;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Enumerated(EnumType.STRING)
    private CanalEnum canalOrigem;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;
}