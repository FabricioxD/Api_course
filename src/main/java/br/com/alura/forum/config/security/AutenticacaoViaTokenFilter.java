package br.com.alura.forum.config.security;

import br.com.alura.forum.entites.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

// Classe para autenticação do token enviado pelo usuário. Essa classe vai interceptar a requisição, para fazer a autenticação do token e depois liberar o fluxo da requisição.
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {
    // Não é possível fazer o uso de autowired e injetar dependencia em classes de filter.
    // passamos o token pelo construtor.
    private TokenService tokenService;
    private UsuarioRepository repository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    // Toda requisição será autenticada via token, porque a comunicação é stateless, não se aguarda nenhum estado de sessão.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperarToken(request);

        boolean valido = tokenService.isTokenValido(token);
        if (valido) {
            // se o token for válido, será feita a autenticação do usuário, caso não seja valido
            // será chamado o fluxo sem autenticar e retornar um forbiden
            autenticarCliente(token);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarCliente(String token) {
        // pega o id do usuario pelo token e recupera o usuario no BD, passando o id pro repository.
        Long idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario = repository.findById(idUsuario).get();

        // faz a autenticacao desse usuário usando o UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        // pega o cabeçalho authorization que é utilizado em validação via token
        String token = request.getHeader("Authorization");

        // valida se o token está vindo no header, não é vazio e começa com Bearer
        if (token == null || token.isEmpty() || !token.startsWith("Bearer")) {
            return null;
        }

        // retorna só o token, sem o Bearer
        return token.substring(7, token.length());
    }
}
