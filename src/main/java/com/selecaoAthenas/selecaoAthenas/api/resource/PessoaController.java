package com.selecaoAthenas.selecaoAthenas.api.resource;

import com.selecaoAthenas.selecaoAthenas.api.model.dto.PessoaDto;
import com.selecaoAthenas.selecaoAthenas.api.model.entity.Pessoa;
import com.selecaoAthenas.selecaoAthenas.api.service.PessoaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService service;
    private final ModelMapper modelMapper;

    public PessoaController(PessoaService service, ModelMapper modelMapper){
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PessoaDto create(PessoaDto pessoaDto){
        Pessoa entity = modelMapper.map(pessoaDto, Pessoa.class);
        entity = service.save(entity);
        return modelMapper.map(entity, PessoaDto.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PessoaDto read(@PathVariable  Integer id){
        return service.getById(id)
                .map(pessoa -> modelMapper.map(pessoa, PessoaDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PessoaDto update(@PathVariable Integer id, PessoaDto pessoaDto){
        return service.getById(id)
                .map(pessoa -> {
                    pessoa.setNome(pessoaDto.getNome());
                    pessoa.setDataNasc(pessoaDto.getDataNasc());
                    pessoa.setCpf(pessoaDto.getCpf());
                    pessoa.setSexo(pessoaDto.getSexo());
                    pessoa.setAltura(pessoaDto.getAltura());
                    pessoa.setPeso(pessoaDto.getPeso());
                    pessoa = service.update(pessoa);
                    return modelMapper.map(pessoa, PessoaDto.class);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        Pessoa pessoa = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(pessoa);
    }
}
