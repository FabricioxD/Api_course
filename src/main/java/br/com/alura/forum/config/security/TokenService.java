package br.com.alura.forum.config.security;

import br.com.alura.forum.entites.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    // Injeção de valor definido no application properties, passa-se o nome do atributo na anotação value
    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        Usuario principal = (Usuario) authentication.getPrincipal();
        Date hoje = new Date();
        // obtém o tempo de expiração, somando o tempo de hoje em milisegundos, com os milisegundos definidos no properties.
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));

        // builder para gerar o token, utilizando o Jwts
        return Jwts.builder()
                // quem está gerando o token
                .setIssuer("Api do fórum da Alura")
                // Usuário do token
                .setSubject(principal.getId().toString())
                // data de geração do token
                .setIssuedAt(hoje)
                // tempo de expiração do token
                .setExpiration(dataExpiracao)
                // criptografia do token. Recebe como parametros o algoritmo de criptografia e a senha da aplicação
                .signWith(SignatureAlgorithm.HS256, secret)
                // compacta tudo e transforma numa string
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            // faz o parse do token (descriptografar) usando o jwts
            // o parseClaimsJws, devolve as informações setadas dentro do token.
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    // pega o id do usuario usando o token e jwt, para poder recuperar o usuário do BD pelo id
    public Long getIdUsuario(String token) {
        // recupera o id do usuário dentro do token
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        // pega o id que veio no claims e retorna como long
        return Long.parseLong(claims.getSubject());
    }
}
