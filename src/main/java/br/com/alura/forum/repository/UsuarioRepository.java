package br.com.alura.forum.repository;

import br.com.alura.forum.entites.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //Uso de Optional pra indicar que pode retornar um email existente ou não.
    Optional<Usuario> findByEmail(String email);
}
