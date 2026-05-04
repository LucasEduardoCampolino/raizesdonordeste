package com.raizesdonordeste.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Promocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeEvento;

    private Double valorPromocional;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Boolean ativo;

    @ManyToOne
    private Unidade unidade;
}