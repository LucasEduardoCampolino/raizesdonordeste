package com.raizesdonordeste.model.entity;

import com.raizesdonordeste.model.enums.PerfilEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private String senha;

    private Integer saldoPontos;

    private LocalDateTime dataConsentimentoLGPD;

    @Enumerated(EnumType.STRING)
    private PerfilEnum perfil;
}
