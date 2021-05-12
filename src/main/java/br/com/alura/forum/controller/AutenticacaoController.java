package br.com.alura.forum.controller;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.dto.TokenDto;
import br.com.alura.forum.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form) {
        // cria token de autenticação com os dados de login do usuário
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            // autentica o usuário, devolvendo a autenticação
            Authentication authentication = authManager.authenticate(dadosLogin);
            // gera o token de autenticação do usuário utilizando o jwts
            String token = tokenService.gerarToken(authentication);

            // retornar o status code 200 e um token dto com o tipo de autenticação (Bearer) e o código do token
            return ResponseEntity.ok(new TokenDto(token, "Bearer"));
        } catch (AuthenticationException e) {
            // retorna badrequest se não conseguir fazer a autenticação
            return ResponseEntity.badRequest().build();
        }



    }
}
