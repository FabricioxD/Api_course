package br.com.alura.forum.controller;

import br.com.alura.forum.dto.DetalhesTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.entites.Curso;
import br.com.alura.forum.entites.Topico;
import br.com.alura.forum.form.AtualizacaoTopicoForm;
import br.com.alura.forum.form.TopicoForm;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    /**
     *
     * @param nomeCurso nome do curso que deseja listar os topicos
     * @param paginacao Objeto do spring data que faz a paginação, recebendo, no query parameter
     * o page (índice do registro inicial), size (quantidade de registros retornados), sort (critério de páginação dos registros)
     * desc ou asc, para descendente ou ascendente. Exemplo: http://localhost:8080/topicos?page=0&size=10&sort=id,desc
     * @return retorna um Page de TopicoDto com os tópicos já páginados
     */
    @GetMapping
    Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso,
                          @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable paginacao) {
        Page<Topico> topicos = null;

        // Uso de page para fazer paginacao do retorno do BD e controlar quantos itens serão retornados na requisição
        // necessário a medida que o BD vai tendo muitos registros

        if (nomeCurso == null) {
            topicos = topicoRepository.findAll(paginacao);
        } else {
            topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
        }


        return TopicoDto.converter(topicos);
    }

    @PostMapping
    @Transactional
    ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriComponentsBuilder) {
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    // colocar o id entre chaves para indicar que é dinâmico.
    @GetMapping("/{id}")
    // anotação @PathVariable pra indicar que o id virá na url e não como um query parameter
    // tbm é possível utilizar como @PathVariable("id") Long variableName
    public ResponseEntity<DetalhesTopicoDto> detalhar(@PathVariable Long id) {
        // findById trás o registro mas não lança exceção, retorna um optional que pode ter algo ou nao
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(new DetalhesTopicoDto(topico.get()));
        }

        return ResponseEntity.notFound().build();

    }
    // o Put altera todos os campos de uma vez, o patch altera apenas coisas específicas.
    @PutMapping("/{id}")
    // avisar o spring para commitar a transação ao final do método. todos os métodos que tiver operação de escrita deve utilizar o transactional
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(optional.get()));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Topico> optional = topicoRepository.findById(id);
        if (optional.isPresent()) {
          topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().build();
    }
}
