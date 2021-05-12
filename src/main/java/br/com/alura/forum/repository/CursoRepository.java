package br.com.alura.forum.repository;

import br.com.alura.forum.entites.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNome(String nomeCurso);
}
