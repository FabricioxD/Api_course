package br.com.alura.forum.config.security;

import br.com.alura.forum.entites.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
//Implementa UserDetailsService pra indicar que é uma classe de autenticação do usuário.
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Usuario> usuario = repository.findByEmail(s);
        if (usuario.isPresent()) {
            return usuario.get();
        }

        throw new UsernameNotFoundException("dados inválidos");
    }
}
