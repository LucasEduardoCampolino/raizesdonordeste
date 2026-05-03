package com.raizesdonordeste.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizesdonordeste.model.entity.AuditoriaLog;
import com.raizesdonordeste.repository.AuditoriaLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaLogRepository repository;
    private final ObjectMapper objectMapper;

    public void registrarLog(
            Long usuarioId,
            String acao,
            String tabela,
            Long registroId,
            Object detalhes
    ) {

        String json = null;

        try {
            if (detalhes != null) {
                json = objectMapper.writeValueAsString(detalhes);
            }
        } catch (Exception e) {
            json = "ERRO_AO_SERIALIZAR";
        }

        AuditoriaLog log = AuditoriaLog.builder()
                .usuarioId(usuarioId)
                .acao(acao)
                .tabelaAfetada(tabela)
                .registroId(registroId)
                .dataHora(LocalDateTime.now())
                .detalhesJson(json)
                .build();

        repository.save(log);
    }
}