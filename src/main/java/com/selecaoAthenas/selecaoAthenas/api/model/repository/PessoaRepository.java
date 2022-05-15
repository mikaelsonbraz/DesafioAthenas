package com.selecaoAthenas.selecaoAthenas.api.model.repository;

import com.selecaoAthenas.selecaoAthenas.api.model.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
}
