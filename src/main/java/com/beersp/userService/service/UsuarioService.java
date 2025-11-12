package com.beersp.userService.service;

import org.springframework.stereotype.Service;
import com.beersp.userService.model.Usuario;
import com.beersp.userService.repository.UsuarioRepository;
import com.beersp.userService.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getUsuarioByUsername (String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("Usuario no encontrado con username: "+username));
    }

    public Usuario getUsuarioById (String idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new NotFoundUserException("Usuario no encontrado con id: "+idUsuario));
    }

    public Page<Usuario> buscarUsuarios(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if(username == null || username.isBlank()) {
            return usuarioRepository.findAll(pageable);
        }
        return usuarioRepository.findByUsernameContainingIgnoreCase(username, pageable);
    }

    /*public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }*/

}
