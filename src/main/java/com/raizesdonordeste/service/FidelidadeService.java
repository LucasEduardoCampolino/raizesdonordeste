package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Usuario;
import com.raizesdonordeste.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import com.raizesdonordeste.exception.BusinessException;

@Service
public class FidelidadeService {

    private final UsuarioRepository usuarioRepository;

    public FidelidadeService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public int calcularPontos(Double valor) {

        if (valor == null || valor <= 0) {
            return 0;
        }

        return valor.intValue();
    }

    public double calcularDesconto(int pontos) {

        if (pontos < 0) {
            throw new BusinessException("Pontos inválidos");
        }

        return pontos * 0.1;
    }

    public void validarResgate(Usuario usuario, int pontos) {

        if (pontos <= 0) {
            throw new BusinessException("Quantidade de pontos inválida");
        }

        if (usuario.getSaldoPontos() == null || usuario.getSaldoPontos() < pontos) {
            throw new BusinessException("Pontos insuficientes");
        }
    }

    public void debitarPontos(Usuario usuario, int pontos) {

        if (pontos <= 0) {
            throw new RuntimeException("Pontos inválidos");
        }

        usuario.setSaldoPontos(usuario.getSaldoPontos() - pontos);
        usuarioRepository.save(usuario);
    }

    public void adicionarPontos(Usuario usuario, int pontos) {
        int saldo = usuario.getSaldoPontos() == null ? 0 : usuario.getSaldoPontos();
        usuario.setSaldoPontos(saldo + pontos);
        usuarioRepository.save(usuario);
    }
}