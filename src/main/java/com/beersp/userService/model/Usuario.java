package com.beersp.userService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends RepresentationModel<Usuario>{

    @Id
    @Column(name = "idUsuario", nullable = false, unique = true, columnDefinition = "CHAR(36)")
    @Schema(hidden = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String idUsuario;

    @Schema(description = "Nombre de usuario", required = true, example = "menganito123")
    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    private String username;

    @Schema(description = "Nombre del usuario", required = true, example = "Menganito") 
    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre del usuario no puede ser nulo")
    private String nombre;

    @Schema(description = "Apellidos del usuario", required = true, example = "Pérez Gómez") 
    @Column(name = "apellidos", nullable = true)
    private String apellidos;

    @Schema(description = "Fecha de nacimiento del usuario", example = "2003-07-01")
    @Column(name = "fecha_nacimiento", nullable = false)
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fechaNacimiento;

    @Schema(description = "Contraseña del usuario", required = true, example = "P@ssw0rd!")
    @Column(name = "contraseña", nullable = false)
    @NotBlank(message = "La contraseña no puede ser nula")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contraseña;

    @Schema(description = "Fecha de registro del usuario", example = "2025-07-01")
    @Column(name = "fecha_registro", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate fechaRegistro;

    @Schema(description = "Género del usuario", required = true, example = "Masculino")
    @Column(name = "genero", nullable = true)
    private String genero;

    @Schema(description = "Biografía del usuario", required = true, example = "Apasionado por las cervezas artesanales y los videojuegos.")
    @Column(name = "biografia", nullable = true)
    private String biografia;

    @Schema(description = "Ubicación del usuario", required = true, example = "Apasionado por las cervezas artesanales y los videojuegos.")
    @Column(name = "ubicación", nullable = true)
    private String ubicacion;

    @Schema(description = "Correo electrónico del usuario", required = true, example = "menganito123@gmail.com")
    @Column(name = "correo", nullable = false, unique = true)
    @NotBlank(message = "El correo del usuario no puede ser nulo")
    @Email(message = "El correo debe ser válido")
    private String correo;

    @Schema(description = "URL de la imagen del usuario", required = true, example = "https://example.com/imagen.jpg")
    @Column(name = "fotoPerfil_url", nullable = true)
    private String fotoPerfilUrl;


    @Schema(description = "Indica si el usuario está validado", example = "true")
    @Column(name = "validado", nullable = false)
    private boolean validado = false;

    @Transient
    @Schema(hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<EntityModel<Local>> localesVisitados;

    @Transient
    @Schema(hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<EntityModel<Cerveza>> cervezasFavoritas;

    
}
