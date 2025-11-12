package com.beersp.userService.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.beersp.userService.controller.UsuarioController;
import com.beersp.userService.model.Usuario;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, Usuario>{
    
    public UsuarioModelAssembler() {
        super(UsuarioController.class, Usuario.class);
    }

    @Override
    public Usuario toModel(Usuario entity) {
        entity.add(linkTo(methodOn(UsuarioController.class).getUsuario(entity.getIdUsuario())).withSelfRel());
        return entity;
    }
}
