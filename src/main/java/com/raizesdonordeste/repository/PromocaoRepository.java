package com.raizesdonordeste.repository;

import com.raizesdonordeste.model.entity.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    List<Promocao> findByUnidadeIdAndAtivoTrueAndDataInicioBeforeAndDataFimAfter(
            Long unidadeId,
            LocalDateTime agora1,
            LocalDateTime agora2
    );
}