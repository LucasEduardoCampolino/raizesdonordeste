package com.raizesdonordeste.repository;

import com.raizesdonordeste.model.entity.Pedido;
import com.raizesdonordeste.model.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatusIn(List<StatusEnum> status);
}