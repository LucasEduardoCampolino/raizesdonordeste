package com.raizesdonordeste.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promocoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeEvento;

    private Double valorPromocional;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
}