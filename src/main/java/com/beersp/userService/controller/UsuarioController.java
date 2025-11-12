package com.beersp.userService.controller;

import java.time.LocalDate;
import java.util.List;
//import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.beersp.userService.assembler.UsuarioModelAssembler;
import com.beersp.userService.service.UsuarioService;

import com.beersp.userService.service.BeerClient;
import com.beersp.userService.service.LocalClient;
import com.beersp.userService.service.TastingClient;
import com.beersp.userService.model.Cerveza;
import com.beersp.userService.model.Degustacion;
import com.beersp.userService.model.Local;
import com.beersp.userService.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.Link;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioModelAssembler;
    private final PagedResourcesAssembler<Usuario> pagedResourcesAssembler;

    private final TastingClient tastingClient;
    private final LocalClient localClient;
    private final BeerClient beerClient;

    @GetMapping(value = "/{id}", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<Usuario> getUsuario(@PathVariable String id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
    }
    

    @GetMapping(produces = {"application/json", "application/hal+json"})
    public ResponseEntity<PagedModel<Usuario>> getUsuarios(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size){

        Page<Usuario> usuarios = usuarioService.buscarUsuarios(username, page, size);
            
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(usuarios, usuarioModelAssembler));
    }
    

    @GetMapping(value = "/{id}/inicio", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<Usuario> inicio(@PathVariable String id) {
        Usuario usuario = usuarioService.getUsuarioById(id);

        List<Degustacion> degustaciones = tastingClient.getDegustaciones(id);

        LocalDate sieteDiasAtras = LocalDate.now().minusDays(7);

        Set<EntityModel<Local>> localesVisitados = degustaciones.stream()
            .filter(d -> d.getFechaDegustacion() != null &&
                         !d.getFechaDegustacion().isBefore(sieteDiasAtras))
            .map(Degustacion::getIdLocal)
            .distinct()
            .map(localClient::getLocalById)
            .filter(local -> local != null)
            .map(EntityModel::of)
            .collect(Collectors.toSet());

        
        usuario.setLocalesVisitados(localesVisitados);

        Set<EntityModel<Cerveza>> cervezasFavoritas = degustaciones.stream()
            .sorted((d1, d2) -> Integer.compare(d2.getPuntuacion(), d1.getPuntuacion())) // orden descendente por puntuación
            .map(Degustacion::getIdCerveza)
            .distinct()
            .limit(3)
            .map(beerClient::getCervezaById)
            .filter(cerveza -> cerveza != null)
            .map(EntityModel::of)
            .collect(Collectors.toSet());

        usuario.setCervezasFavoritas(cervezasFavoritas);

        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
    }

    @GetMapping(value = "/{id}/cervezas", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<PagedModel<EntityModel<Cerveza>>> getCervezasUsuario(
            @PathVariable String id,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size){
        List<Degustacion> degustaciones = tastingClient.getDegustaciones(id);
        if (degustaciones == null || degustaciones.isEmpty()) {
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, 0);
            PagedModel<EntityModel<Cerveza>> emptyPagedModel = PagedModel.empty(metadata);
            return ResponseEntity.ok(emptyPagedModel);
        }
        List<Cerveza> cervezas = degustaciones.stream()
            .map(Degustacion::getIdCerveza)
            .distinct()
            .map(beerClient::getCervezaById)
            .filter(cerveza -> cerveza != null)
            .collect(Collectors.toList());

        int totalElements = cervezas.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        int start = Math.min(page * size, cervezas.size());
        int end = Math.min(start + size, cervezas.size());
        List<Cerveza> cervezasPage = cervezas.subList(start, end);

        List<EntityModel<Cerveza>> cervezasResources = cervezasPage.stream()
            .map(EntityModel::of)
            .collect(Collectors.toList());
        
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, cervezas.size());
        PagedModel<EntityModel<Cerveza>> pagedModel = PagedModel.of(cervezasResources, metadata);

        String baseUrl = String.format("http://localhost:8081/v1/usuarios/%s/cervezas", id);

        if (totalPages == 1) {
            // solo una página
            pagedModel.add(Link.of(baseUrl + "?page=0&size=" + size).withSelfRel());
        } else {
            // first
            pagedModel.add(Link.of(baseUrl + "?page=0&size=" + size).withRel("first"));

            // prev solo si no estamos en la primera página
            if (page > 0) {
                pagedModel.add(Link.of(baseUrl + "?page=" + (page - 1) + "&size=" + size).withRel("prev"));
            }

            // self
            pagedModel.add(Link.of(baseUrl + "?page=" + page + "&size=" + size).withSelfRel());

            // next solo si no estamos en la última página
            if (page < totalPages - 1) {
                pagedModel.add(Link.of(baseUrl + "?page=" + (page + 1) + "&size=" + size).withRel("next"));
            }

            // last
            pagedModel.add(Link.of(baseUrl + "?page=" + (totalPages - 1) + "&size=" + size).withRel("last"));
        }
        return ResponseEntity.ok(pagedModel);
    }

    /*@GetMapping(value = "/{id}/resumen", produces = {"application/json", "application/hal+json"}) 
    public ResponseEntity<UsuarioResumen> getUsuarioResumen(@PathVariable String id) {
        
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
    }
    */

    /*@GetMapping(value = "/{id}/locales", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<PagedModel<Local>> getLocalesVisitados(
            @PathVariable String id,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size){
            
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(locales, localModelAssembler));
    }*/

    /*@GetMapping(value = "/{id}/cervezas", produces = {"application/json", "application/hal+json"})
    public ResponseEntity<PagedModel<Cerveza>> getCervezasFavoritas(
            @PathVariable String id,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "5", required = false) int size){
            
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(cervezas, cervezaModelAssembler));
     */


    /*** PRUEBAS PARA LA API GATEWAY (Borrar si todo funciona) ***/

    /*
    @GetMapping(value = "/{id}")
    public String getUsuario(@PathVariable String id) {
        return "OK -> userService: Ruta de /v1/usuarios/{id} (METODO GET) recibida.";
    }

    @GetMapping()
    public String getUsuarios() {
        return "OK -> userService: Ruta de /v1/usuarios (METODO GET) recibida.";
    }

    @GetMapping(value = "/{id}/resumen")
    public String getUsuarioResumen(@PathVariable String id) {
        return "OK -> userService: Ruta de /v1/usuarios/{id}/resumen (METODO GET) recibida.";
    }
    
    @GetMapping(value = "/{id}/locales")
    public String getLocalesVisitados(@PathVariable String id) {
        return "OK -> userService: Ruta de /v1/usuarios/{id}/locales (METODO GET) recibida.";
    }

    @GetMapping(value = "/{id}/cervezas")
    public String getCervezasFavoritas(@PathVariable String id) {
        return "OK -> userService: Ruta de /v1/usuarios/{id}/cervezas (METODO GET) recibida.";
    }*/
}
