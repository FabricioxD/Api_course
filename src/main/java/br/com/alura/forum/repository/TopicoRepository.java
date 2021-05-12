package br.com.alura.forum.repository;

import br.com.alura.forum.entites.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * o uso do JpaRepository elimina necessidade de criação do DAO com o jpa, deve passa a entidade e o id.
 */
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    /**
     *
     * @param nomeCurso - nome do filtro no query parametrers, o jpa identifica a entidade curso dentro de topico e o atributo nome dentro da entidade curso
     * @return retorna a lista de tópico filtrado pelo curso
     */
    Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);

    @Query("SELECT t FROM Topico t where t.curso.nome = :nomeCurso")
    List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);

}
