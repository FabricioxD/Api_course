package br.com.alura.forum.config.security;

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// anotações para habilitar impl de segurança no spring
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    // uso de @Bean para indicar para o autowired que é injetável
    @Bean
    // necessário implementar para poder injetar a classe AuthenticationManager
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Configuracoes de autenticacao
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // faz a autenticação do usuário definido na classe AutenticacaoService, pelo email, e encripta o password pra guardar no BD
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Configuracoes de autorizacao
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //permite acesso público ao endpoint /topicos, quando o verbo é GET e restringe pra outros verbos http
                .antMatchers(HttpMethod.GET, "/topicos").permitAll()
                //permite acesso público ao endpoint /topicos/qualquerCoisa, qndo o verbo é GET.
                .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                // indica que todas as URL'S que não são /topicos precisam estar autenticadas.
                .anyRequest().authenticated()
                // desabilita csrf, pois o token ja nos protege contra isso
                .and().csrf().disable()
                //define política de gerenciamento de sessão como stateless, indicando que não é pra criar sessão nem guardar estado.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // registra o filtro do token, criado na classe AutenticacaoViaTokenFilter, utilizando como classe de autenticacao padrão UsernamePasswordAuthenticationFilter
                .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
    }

    //configuracoes de recursos estaticos(js, css, imagens, etc.)
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}
