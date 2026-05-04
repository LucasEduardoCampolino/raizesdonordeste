package com.raizesdonordeste.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;

    private String acao;

    private String tabelaAfetada;

    private Long registroId;

    private LocalDateTime dataHora;

    @Column(columnDefinition = "TEXT")
    private String detalhesJson;
}