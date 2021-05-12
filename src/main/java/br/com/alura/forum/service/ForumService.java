package br.com.alura.forum.service;

import br.com.alura.forum.dto.TopicoDto;
import org.springframework.stereotype.Service;

@Service
public class ForumService {

    /**
     *
     * @param test recebe uma string
     * @param id recebe um id
     * @return
     */
    public TopicoDto teste(String test, Integer id) {
        //TODO
        return null;
    }

    public TopicoDto teste2() {
        return teste("x", 2);
    }
}
