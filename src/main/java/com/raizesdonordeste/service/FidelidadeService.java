package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Usuario;
import com.raizesdonordeste.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class FidelidadeService {

    private final UsuarioRepository usuarioRepository;

    public FidelidadeService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public int calcularPontos(Double valor) {
        return valor.intValue();
    }

    public double calcularDesconto(int pontos) {
        return pontos * 0.1;
    }

    public void validarResgate(Usuario usuario, int pontos) {

        if (pontos <= 0) {
            throw new RuntimeException("Quantidade de pontos inválida");
        }

        if (usuario.getSaldoPontos() == null || usuario.getSaldoPontos() < pontos) {
            throw new RuntimeException("Pontos insuficientes");
        }
    }

    public void debitarPontos(Usuario usuario, int pontos) {
        usuario.setSaldoPontos(usuario.getSaldoPontos() - pontos);
        usuarioRepository.save(usuario);
    }

    public void adicionarPontos(Usuario usuario, int pontos) {
        int saldo = usuario.getSaldoPontos() == null ? 0 : usuario.getSaldoPontos();
        usuario.setSaldoPontos(saldo + pontos);
        usuarioRepository.save(usuario);
    }
}