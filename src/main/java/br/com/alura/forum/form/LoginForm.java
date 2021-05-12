package br.com.alura.forum.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//criação de form para Loginde autenticação - Form é um DTO para objetos de post.
public class LoginForm {

    private String email;
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(email, senha);
    }
}
