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
        return valor.intValue(); // 1 real = 1 ponto
    }

    public double calcularDesconto(int pontos) {
        return pontos * 0.1; // 1 ponto = 0.10
    }

    public void validarResgate(Usuario usuario, int pontos) {
        if (usuario.getSaldoPontos() < pontos) {
            throw new RuntimeException("Pontos insuficientes");
        }
    }

    public void debitarPontos(Usuario usuario, int pontos) {
        usuario.setSaldoPontos(usuario.getSaldoPontos() - pontos);
        usuarioRepository.save(usuario);
    }

    public void adicionarPontos(Usuario usuario, int pontos) {
        usuario.setSaldoPontos(usuario.getSaldoPontos() + pontos);
        usuarioRepository.save(usuario);
    }
}