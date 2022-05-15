package com.selecaoAthenas.selecaoAthenas.api.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ModelAndView home(){
        return new ModelAndView("Home.html");
    }

    @GetMapping("/CadastrarPessoa.html")
    public ModelAndView cadastro() {
        return new ModelAndView("CadastrarPessoa.html");
    }

    @GetMapping("/PesquisarPessoa.html")
    public ModelAndView pesquisa(){
        return new ModelAndView("PesquisarPessoa.html");
    }
}
