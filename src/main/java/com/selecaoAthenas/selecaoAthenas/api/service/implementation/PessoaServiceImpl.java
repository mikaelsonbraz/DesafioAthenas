package com.selecaoAthenas.selecaoAthenas.api.service.implementation;

import com.selecaoAthenas.selecaoAthenas.api.model.entity.Pessoa;
import com.selecaoAthenas.selecaoAthenas.api.model.repository.PessoaRepository;
import com.selecaoAthenas.selecaoAthenas.api.service.PessoaService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class PessoaServiceImpl implements PessoaService {

    PessoaRepository repository;

    public PessoaServiceImpl(PessoaRepository repository){
        this.repository = repository;
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        return repository.save(pessoa);
    }

    @Override
    public Optional<Pessoa> getById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Pessoa update(Pessoa pessoa) {
        if (Objects.equals(pessoa, null) || Objects.equals(pessoa.getIdPessoa(), null)){
            throw new IllegalArgumentException("O id da pessoa não pode ser nulo");
        }
        return repository.save(pessoa);
    }

    @Override
    public void delete(Pessoa pessoa) {
        if (Objects.equals(pessoa, null) || Objects.equals(pessoa.getIdPessoa(), null)){
            throw new IllegalArgumentException("O id da pessoa não pode ser nulo");
        }
        repository.delete(pessoa);
    }
}
