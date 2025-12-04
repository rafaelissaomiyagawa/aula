package com.example.aula;

import com.example.aula.service.PessoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// jpa + jdbc template + docker + postgres + flyway
@RestController // @Component com extras
@RequestMapping("/qqrcoisa") //endpoint
@RequiredArgsConstructor
public class HomeController {

    private final PessoaService pessoaService;

    @GetMapping
    public String index(@RequestParam("nome") String nome) {
        pessoaService.save(Pessoa.comNome(nome));
        return "oi";
    }

    @GetMapping("/oi")
    public String index2() {
        return "oi2";
    }
}
