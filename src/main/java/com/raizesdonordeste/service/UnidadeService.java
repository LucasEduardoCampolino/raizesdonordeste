package com.raizesdonordeste.service;

import com.raizesdonordeste.model.entity.Unidade;
import com.raizesdonordeste.repository.UnidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService {

    private final UnidadeRepository repository;

    public UnidadeService(UnidadeRepository repository) {
        this.repository = repository;
    }

    public Unidade salvar(Unidade unidade) {
        return repository.save(unidade);
    }

    public List<Unidade> listar() {
        return repository.findAll();
    }
}