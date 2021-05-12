package br.com.alura.forum.form;

import br.com.alura.forum.entites.Curso;
import br.com.alura.forum.entites.Topico;
import br.com.alura.forum.repository.CursoRepository;
import com.sun.istack.NotNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

public class TopicoForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String titulo;
    @NotNull @NotEmpty @Length(min = 5)
    private String mensagem;
    @NotNull @NotEmpty @Length(min = 1)
    private String nomeCurso;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Topico converter(CursoRepository repository) {
        Curso curso = repository.findByNome("nomeCurso");
        return new Topico(titulo, mensagem, curso);
    }
}
