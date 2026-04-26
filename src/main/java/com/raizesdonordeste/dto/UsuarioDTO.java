package com.raizesdonordeste.dto;

import com.raizesdonordeste.model.enums.PerfilEnum;
import lombok.Data;

@Data
public class UsuarioDTO {

    private String nome;
    private String email;
    private String senha;
    private PerfilEnum perfil;
}
