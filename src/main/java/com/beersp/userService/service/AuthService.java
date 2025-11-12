package com.beersp.userService.service;

import com.beersp.userService.repository.UsuarioRepository;
import com.beersp.userService.exception.*;
import java.time.Period;
import com.beersp.userService.model.Usuario;

import java.util.UUID;
import java.time.LocalDate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario register(Usuario usuario){
        if(usuarioRepository.existsByUsername(usuario.getUsername())){
            throw new DuplicateUserException("El nombre de usuario ya está en uso.");
        }
        if(usuarioRepository.existsByCorreo(usuario.getCorreo())){
            throw new DuplicateUserException("El correo electrónico ya está en uso.");
        }
        if(usuario.getFechaNacimiento() == null ||
            Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears() < 18){
            throw new IllegalAgeException("El usuario debe ser mayor de 18 años.");
        }
        usuario.setIdUsuario(UUID.randomUUID().toString());
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuario.setValidado(false);
        usuarioRepository.save(usuario);
        emailService.sendVerificationEmail(usuario.getCorreo(), usuario.getUsername());
        return usuario;
    }

    public boolean verifyUser(String correo){
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new NotFoundUserException("Usuario no encontrado con correo: "+correo));
        if (usuario != null && !usuario.isValidado()){
            usuario.setValidado(true);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    public Usuario login(String username, String contraseña){
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("El usuario no existe. "));
        if(!passwordEncoder.matches(contraseña, usuario.getContraseña())) {
            throw new InvalidCredentialsException("Contraseña incorrecta.");
        }
        if(!usuario.isValidado()) {
            throw new UserNotVerifiedException("Debes verificar tu cuenta antes de iniciar sesión.");
        }
        return usuario;
        /*if (usuario != null && passwordEncoder.matches(contraseña, usuario.getContraseña()) && usuario.isValidado()){
            return true;
        }
        return false;*/
    }
}
