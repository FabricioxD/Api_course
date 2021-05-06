package br.com.alura.forum.controller;

import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.entites.Curso;
import br.com.alura.forum.entites.Topico;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class TopicosController {

    @RequestMapping("/topicos")
    public List<TopicoDto> lista() {
        Topico topico = new Topico("xablaus", "Duvida com Spring", new Curso("Spring", "Programação"));

        return TopicoDto.converter(Arrays.asList(topico, topico, topico));
    }
}
