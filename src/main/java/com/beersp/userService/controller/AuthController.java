package com.beersp.userService.controller;
import com.beersp.userService.service.AuthService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import jakarta.validation.Valid;
import java.util.Map;

import com.beersp.userService.assembler.UsuarioModelAssembler;
import com.beersp.userService.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    private final UsuarioModelAssembler usuarioModelAssembler;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody Usuario usuarioNuevo) {
        Usuario usuario = authService.register(usuarioNuevo);
        return ResponseEntity.created(linkTo(UsuarioController.class).slash(usuario.getIdUsuario()).toUri()).build();
    }
    
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String correo) {
        boolean verificado = authService.verifyUser(correo);
        if (verificado) {
            return ResponseEntity.ok("Usuario verificado exitosamente.");
        } else {
            return ResponseEntity.badRequest().body("No se pudo verificar el usuario.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String contraseña = credentials.get("contraseña");
        Usuario usuario = authService.login(username, contraseña);
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
    }



    /*** PRUEBAS PARA LA API GATEWAY (Borrar si todo funciona) ***/
    /*@PostMapping("/register")
    public String registerUser() {
        return "OK -> userService: Ruta de REGISTRO/LOGIN (POST /api/usuarios) recibida.";
    }

    @PostMapping("/login")
    public String loginUser() {
        return "OK -> userService: Ruta de LOGIN (POST /api/usuarios/login) recibida.";
    }*/
    
}
