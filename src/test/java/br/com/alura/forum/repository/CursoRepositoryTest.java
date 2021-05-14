package br.com.alura.forum.repository;

import br.com.alura.forum.entites.Curso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class CursoRepositoryTest {

    @InjectMocks
    CursoRepository cursoRepository;

    @Mock
    Curso curso;

    @Test
    @DisplayName("retorna o campo nome dentro de Curso")
    void shouldReturnCursoName() {
        Curso mockCurso = Mockito.mock(Curso.class);
        mockCurso.setNome("xablau");

        Curso curso = cursoRepository.findByNome("xablau");

        assertEquals(mockCurso.getNome(), curso.getNome());

    }
}