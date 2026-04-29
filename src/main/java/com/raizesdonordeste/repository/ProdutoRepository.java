package com.raizesdonordeste.repository;

import com.raizesdonordeste.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}