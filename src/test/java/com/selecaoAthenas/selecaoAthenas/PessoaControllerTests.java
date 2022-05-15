package com.selecaoAthenas.selecaoAthenas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.selecaoAthenas.selecaoAthenas.api.model.dto.PessoaDto;
import com.selecaoAthenas.selecaoAthenas.api.model.entity.Pessoa;
import com.selecaoAthenas.selecaoAthenas.api.resource.PessoaController;
import com.selecaoAthenas.selecaoAthenas.api.service.PessoaService;
import org.apiguardian.api.API;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = PessoaController.class)
@AutoConfigureMockMvc
public class PessoaControllerTests {

    static String API_PESSOAS = "/api/pessoas";

    @Autowired
    MockMvc mvc;

    @MockBean
    PessoaService service;

    public PessoaDto getPessoaDto(){
        return PessoaDto.builder()
                .nome("João Almeida")
                .dataNasc(Date.valueOf("1990-05-14"))
                .cpf("111.222.333-44")
                .sexo('M')
                .altura(1.80)
                .peso(80)
                .build();
    }

    public Pessoa getPessoa(){
        return Pessoa.builder()
                .idPessoa(1)
                .nome("João Almeida")
                .dataNasc(Date.valueOf("1990-05-14"))
                .cpf("111.222.333-44")
                .sexo('M')
                .altura(1.80)
                .peso(80)
                .build();
    }

    @Test
    @DisplayName("Deve criar uma pessoa no BD")
    public void criarPessoaTest() throws Exception{
        //cenário
        Pessoa pessoa = getPessoa();
        PessoaDto pessoaDto = getPessoaDto();
        BDDMockito.given(service.save(Mockito.any(Pessoa.class))).willReturn(pessoa);
        String json = new ObjectMapper().writeValueAsString(pessoaDto);

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API_PESSOAS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("idPessoa").value(pessoa.getIdPessoa()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(pessoa.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(pessoa.getCpf()));
    }

    @Test
    @DisplayName("Deve retornar uma Pessoa por id")
    public void getPessoaPorIdTest() throws Exception{
        //cenário
        Pessoa pessoa = getPessoa();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(pessoa));

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API_PESSOAS.concat("/1"))
                .accept(MediaType.APPLICATION_JSON);

        //verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idPessoa").value(pessoa.getIdPessoa()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(pessoa.getNome()));
    }

    @Test
    @DisplayName("Deve retornar ResponseStatusException quando não encontrar uma pessoa por id")
    public void pessoaNaoEncontradaPorIdTest() throws Exception{
        //cenário
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API_PESSOAS.concat("/1"))
                .accept(MediaType.APPLICATION_JSON);

        //verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma pessoa por id")
    public void atualizaPessoaTest() throws Exception{
        //cenário
        Pessoa pessoa = getPessoa();
        PessoaDto pessoaDto = getPessoaDto();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(pessoa));
        BDDMockito.given(service.update(Mockito.any(Pessoa.class))).willReturn(pessoa);
        String json = new ObjectMapper().writeValueAsString(pessoaDto);

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API_PESSOAS.concat("/update/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificações
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("idPessoa").value(pessoa.getIdPessoa()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(pessoa.getNome()));
    }

    @Test
    @DisplayName("Deve retornar ResponseStatusException quando não encontrar uma pessoa por id pra atualizar")
    public void atualizaPessoaNaoEncontradaTest() throws Exception{
        //cenário
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());
        String json = new ObjectMapper().writeValueAsString(new PessoaDto());

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API_PESSOAS.concat("/update/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar dos dados de uma pessoa do banco de dados")
    public void deletaPessoaTest() throws Exception{
        //cenário
        Pessoa pessoa = getPessoa();
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.of(pessoa));

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API_PESSOAS.concat("/delete/1"));

        //verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar ReponseeStatusException quando não encontrar os dados de uma pessoa para deletar")
    public void deletaPessoaNaoEncotradaTest() throws Exception{
        //cenário
        BDDMockito.given(service.getById(Mockito.anyInt())).willReturn(Optional.empty());

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API_PESSOAS.concat("/delete/1"));

        //verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
