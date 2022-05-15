package com.selecaoAthenas.selecaoAthenas.api.service;

import com.selecaoAthenas.selecaoAthenas.api.model.entity.Pessoa;

import java.util.Optional;

public interface PessoaService {

    Pessoa save(Pessoa pessoa);

    Optional<Pessoa> getById(Integer id);

    Pessoa update(Pessoa pessoa);

    void delete(Pessoa pessoa);
}
