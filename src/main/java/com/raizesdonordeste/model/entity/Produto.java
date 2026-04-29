package com.raizesdonordeste.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Double precoBase;

    private Boolean sazonal;

    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}