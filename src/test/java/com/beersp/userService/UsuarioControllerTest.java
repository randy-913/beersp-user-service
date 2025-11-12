package com.beersp.userService;

import com.beersp.userService.assembler.UsuarioModelAssembler;
import com.beersp.userService.controller.UsuarioController;
import com.beersp.userService.model.*;
import com.beersp.userService.service.BeerClient;
import com.beersp.userService.service.LocalClient;
import com.beersp.userService.service.TastingClient;
import com.beersp.userService.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioControllerTest {
    
    @Test
    public void testInicio() {
        // Creamos mocks de los servicios
        UsuarioService usuarioService = mock(UsuarioService.class);
        TastingClient tastingClient = mock(TastingClient.class);
        LocalClient localClient = mock(LocalClient.class);
        BeerClient beerClient = mock(BeerClient.class);
        UsuarioModelAssembler usuarioModelAssembler = mock(UsuarioModelAssembler.class);
        @SuppressWarnings("unchecked")
        PagedResourcesAssembler<Usuario> pagedResourcesAssembler = mock(PagedResourcesAssembler.class);

        when(usuarioModelAssembler.toModel(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Usuario simulado
        Usuario usuarioMock = new Usuario();
        usuarioMock.setIdUsuario("user123");
        usuarioMock.setUsername("testUser");
        usuarioMock.setContraseña("contraseñaSegura");
        usuarioMock.setCorreo("correo@gmail.com");
        usuarioMock.setNombre("Test");
        usuarioMock.setApellidos("User");
        usuarioMock.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        usuarioMock.setGenero("M");
        when(usuarioService.getUsuarioById("user123")).thenReturn(usuarioMock);

        // Degustaciones simuladas

        List<Degustacion> degustacionesMock = List.of(
            new Degustacion("deg1", "user123", "cerveza1", "local1", LocalDate.now().minusDays(2), 5, null),
            new Degustacion("deg2", "user123", "cerveza2", "local2", LocalDate.now().minusDays(10), 4, null),
            new Degustacion("deg3", "user123", "cerveza3", "local3", LocalDate.now().minusDays(5), 3, null),
            new Degustacion("deg4", "user123", "cerveza1", "local1", LocalDate.now().minusDays(1), 5, null),
            new Degustacion("deg5", "user123", "cerveza2", "local2", LocalDate.now().minusDays(20), 2, null),
            new Degustacion("deg6", "user123", "cerveza4", "local4", LocalDate.now().minusDays(3), 4, null),
            new Degustacion("deg7", "user123", "cerveza5", "local5", LocalDate.now().minusDays(15), 5, null),
            new Degustacion("deg8", "user123", "cerveza6", "local1", LocalDate.now().minusDays(4), 3, null),
            new Degustacion("deg9", "user123", "cerveza3", "local3", LocalDate.now().minusDays(6), 4, null),
            new Degustacion("deg10", "user123", "cerveza7", "local6", LocalDate.now().minusDays(8), 2, null),
            new Degustacion("deg11", "user123", "cerveza8", "local2", LocalDate.now().minusDays(7), 5, null),
            new Degustacion("deg12", "user123", "cerveza5", "local5", LocalDate.now().minusDays(12), 4, null)
        );
        when(tastingClient.getDegustaciones("user123")).thenReturn(degustacionesMock);

        // Locales simulados
        when(localClient.getLocalById("local1")).thenReturn(new Local("local1", "Local 1", "Dirección 1", null));
        when(localClient.getLocalById("local2")).thenReturn(new Local("local2", "Local 2", "Dirección 2", null));
        when(localClient.getLocalById("local3")).thenReturn(new Local("local3", "Local 3", "Dirección 3", null));
        when(localClient.getLocalById("local4")).thenReturn(new Local("local4", "Local 4", "Dirección 4", null));
        when(localClient.getLocalById("local5")).thenReturn(new Local("local5", "Local 5", "Dirección 5", null));
        when(localClient.getLocalById("local6")).thenReturn(new Local("local6", "Local 6", "Dirección 6", null));
    

        // Cervezas simuladas
        when(beerClient.getCervezaById("cerveza1")).thenReturn(new Cerveza("cerveza1", "Cerveza 1", "IPA", "España", "500ml", "Botella", "5.5%", "Medio", "Ámbar", "foto1.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza2")).thenReturn(new Cerveza("cerveza2", "Cerveza 2", "Lager", "Alemania", "330ml", "Lata", "4.8%", "Suave", "Rubia", "foto2.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza3")).thenReturn(new Cerveza("cerveza3", "Cerveza 3", "Stout", "Irlanda", "440ml", "Botella", "6.2%", "Fuerte", "Negra", "foto3.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza4")).thenReturn(new Cerveza("cerveza4", "Cerveza 4", "Pale Ale", "Bélgica", "500ml", "Botella", "5.0%", "Medio", "Rubia", "foto4.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza5")).thenReturn(new Cerveza("cerveza5", "Cerveza 5", "Wheat Beer", "EEUU", "355ml", "Lata", "4.5%", "Suave", "Rubia", "foto5.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza6")).thenReturn(new Cerveza("cerveza6", "Cerveza 6", "Weiss", "Alemania", "500ml", "Botella", "5.3%", "Suave", "Rubia", "foto6.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza7")).thenReturn(new Cerveza("cerveza7", "Cerveza 7", "Saison", "Francia", "330ml", "Botella", "4.9%", "Medio", "Rubia", "foto7.jpg", LocalDate.now(), null));
        when(beerClient.getCervezaById("cerveza8")).thenReturn(new Cerveza("cerveza8", "Cerveza 8", "IPA", "España", "500ml", "Botella", "5.5%", "Medio", "Ámbar", "foto8.jpg", LocalDate.now(), null));

        // Creamos el controlador con los mocks
        UsuarioController usuarioController = new UsuarioController(
            usuarioService, usuarioModelAssembler, pagedResourcesAssembler, tastingClient, localClient, beerClient
        );

        // Ejecutamos el método a probar
        ResponseEntity<Usuario> response = usuarioController.inicio("user123");

        // Verificaciones
        Usuario usuarioResponse = response.getBody();
        assertNotNull(usuarioResponse);

        // Mostrar datos reales
        // Imprimir status HTTP
        System.out.println("Status code: " + response.getStatusCode());

        // Imprimir headers
        System.out.println("Headers: " + response.getHeaders());

        // Imprimir el cuerpo completo
        System.out.println("Cuerpo:");
        System.out.println(usuarioResponse);
        System.out.println("Usuario: " + usuarioResponse.getUsername());
        System.out.println("Locales visitados:");
        usuarioResponse.getLocalesVisitados().forEach(l -> System.out.println("  " + l.getContent().getNombre()));
        System.out.println("Cervezas favoritas:");
        usuarioResponse.getCervezasFavoritas().forEach(c -> System.out.println("  " + c.getContent().getNombre()));
        
        // Solo locales de los ultimos 7 dias (deg1 y deg3)
        Set<EntityModel<Local>> localesVisitados = usuarioResponse.getLocalesVisitados();
        assertEquals(4, localesVisitados.size());

        // Cervezas favoritas (top 3 por puntuacion: deg1, deg2, deg3)
        Set<EntityModel<Cerveza>> cervezasFavoritas = usuarioResponse.getCervezasFavoritas();
        assertEquals(3, cervezasFavoritas.size());

        // Comprobar que los nombres coinciden
        assertTrue(cervezasFavoritas.stream().anyMatch(c -> c.getContent().getNombre().equals("Cerveza 1")));
        assertTrue(cervezasFavoritas.stream().anyMatch(c -> c.getContent().getNombre().equals("Cerveza 8")));
        assertTrue(cervezasFavoritas.stream().anyMatch(c -> c.getContent().getNombre().equals("Cerveza 5")));
    }
}
