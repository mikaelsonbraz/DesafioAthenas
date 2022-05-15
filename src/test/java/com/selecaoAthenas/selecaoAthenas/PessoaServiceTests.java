package com.selecaoAthenas.selecaoAthenas;

import com.selecaoAthenas.selecaoAthenas.api.model.entity.Pessoa;
import com.selecaoAthenas.selecaoAthenas.api.model.repository.PessoaRepository;
import com.selecaoAthenas.selecaoAthenas.api.service.PessoaService;
import com.selecaoAthenas.selecaoAthenas.api.service.implementation.PessoaServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PessoaServiceTests {

    PessoaService service;

    @MockBean
    PessoaRepository repository;

    @MockBean
    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    public void setUp(){
        this.service = new PessoaServiceImpl(repository);
    }

    public Pessoa getPessoa(){
        return Pessoa.builder().idPessoa(1)
                .nome("João Almeida")
                .dataNasc(Date.valueOf("1990-05-15"))
                .cpf("111.222.333-44")
                .sexo('M')
                .altura(1.80)
                .peso(80)
                .build();
    }

    @Test
    @DisplayName("Deve salvar uma pessoa no BD")
    public void savePessoaTest(){
        //cenário
        Pessoa pessoa = getPessoa();
        Mockito.when(repository.save(Mockito.any(Pessoa.class))).thenReturn(pessoa);

        //execução
        Pessoa savedPessoa = service.save(pessoa);

        //verificações
        Assertions.assertThat(savedPessoa.getIdPessoa()).isNotNull();
        Assertions.assertThat(savedPessoa.getNome()).isEqualTo(pessoa.getNome());
        Assertions.assertThat(savedPessoa.getSexo()).isEqualTo(pessoa.getSexo());
        Assertions.assertThat(savedPessoa.getCpf()).isEqualTo(pessoa.getCpf());
        Assertions.assertThat(savedPessoa.getDataNasc()).isEqualTo(pessoa.getDataNasc());
        Assertions.assertThat(savedPessoa.getAltura()).isEqualTo(pessoa.getAltura());
        Assertions.assertThat(savedPessoa.getPeso()).isEqualTo(pessoa.getPeso());
    }

    @Test
    @DisplayName("Deve retornar um Optional<Pessoa> pelo id passado")
    public void getPessoaPorIdTest(){
        //cenário
        Pessoa pessoa = getPessoa();
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.of(pessoa));

        //execução
        Optional<Pessoa> foundPessoa = service.getById(1);

        //verificações
        Assertions.assertThat(foundPessoa.isPresent()).isTrue();
        Assertions.assertThat(foundPessoa.get().getIdPessoa()).isNotNull();
        Assertions.assertThat(foundPessoa.get().getNome()).isEqualTo(pessoa.getNome());
    }

    @Test
    @DisplayName("Deve retornar um Optional.empty() quando não encontrar uma pessoa pelo id")
    public void pessoaNaoEncontradaPorIdTest(){
        //cenário
        Mockito.when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        //execução
        Optional<Pessoa> foundPessoa = service.getById(1);

        //verificação
        Assertions.assertThat(foundPessoa.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma pessoa")
    public void atualizaPessoaTest(){
        //cenário
        Pessoa pessoa = getPessoa();
        Mockito.when(repository.save(Mockito.any(Pessoa.class))).thenReturn(pessoa);

        //execução
        Pessoa savedPessoa = service.update(pessoa);

        //verificações
        Assertions.assertThat(savedPessoa.getIdPessoa()).isNotNull();
        Assertions.assertThat(savedPessoa.getNome()).isEqualTo(pessoa.getNome());
        Assertions.assertThat(savedPessoa.getSexo()).isEqualTo(pessoa.getSexo());
        Assertions.assertThat(savedPessoa.getCpf()).isEqualTo(pessoa.getCpf());
        Assertions.assertThat(savedPessoa.getDataNasc()).isEqualTo(pessoa.getDataNasc());
        Assertions.assertThat(savedPessoa.getAltura()).isEqualTo(pessoa.getAltura());
        Assertions.assertThat(savedPessoa.getPeso()).isEqualTo(pessoa.getPeso());
    }

    @Test
    @DisplayName("Deve retornar IllegalArgumentException quando tenta atualizar os dados de uma pessoa inexistente no BD")
    public void atualizaPessoaNaoEncontradaTest(){
        //cenário
        Pessoa pessoa = new Pessoa();

        //execução

        //verificações
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(pessoa));
        Mockito.verify(repository, Mockito.never()).save(pessoa);
    }

    @Test
    @DisplayName("Deve deletar os dados de uma pessoa do BD")
    public void deletarPessoaTest(){
        //cenário
        Pessoa pessoa = getPessoa();
        entityManager.persist(pessoa);

        //execução
        service.delete(pessoa);
        Pessoa deletedPessoa = entityManager.find(Pessoa.class, pessoa);

        //verificações
        Mockito.verify(repository, Mockito.times(1)).delete(pessoa);
        Assertions.assertThat(deletedPessoa).isNull();
    }

    @Test
    @DisplayName("Deve retornar IllegalArgumentException quando tenta deletar os dados de uma pessoa inexistente no BD")
    public void deletarPessoaNaoEncontradaTest(){
        //cenário
        Pessoa pessoa = new Pessoa();

        //execução

        //verificações
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(pessoa));
        Mockito.verify(repository, Mockito.never()).delete(pessoa);
    }
}
