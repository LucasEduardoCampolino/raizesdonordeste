package com.raizesdonordeste.dto;

import com.raizesdonordeste.model.enums.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Integer saldoPontos;
    private PerfilEnum perfil;
}