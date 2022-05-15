package com.selecaoAthenas.selecaoAthenas.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idPessoa;

    private String nome;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNasc;

    private String cpf;

    private char sexo;

    private double altura;

    private double peso;
}
