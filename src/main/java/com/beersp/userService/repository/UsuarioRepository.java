package com.beersp.userService.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.beersp.userService.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByUsername(String username);
    Page<Usuario> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    boolean existsByUsername(String username);
    boolean existsByCorreo(String correo);
}

