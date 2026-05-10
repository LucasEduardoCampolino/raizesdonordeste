package com.raizesdonordeste.repository;

import com.raizesdonordeste.model.entity.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    List<Promocao> findByUnidadeIdAndAtivoTrueAndDataInicioBeforeAndDataFimAfter(
            Long unidadeId,
            LocalDate dataInicio,
            LocalDate dataFim
    );
}