package com.beersp.userService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // Deshabilita la Protección CSRF (necesario para APIs REST sin estado)
            .csrf(csrf -> csrf.disable())
            
            // Configuración de la Autorización
            .authorizeHttpRequests(auth -> auth
                // Permite el acceso público a todas las rutas bajo /v1/auth/
                .requestMatchers("/v1/auth/**").permitAll() 
                // Permite el acceso público a todas las rutas que empiecen con /v1/usuarios/ (para pruebas de API Gateway)
                .requestMatchers("/v1/usuarios/**").permitAll()
                .requestMatchers("/login.html", "/css/**", "/js/**").permitAll() // Permitir recursos estáticos
                
                // Requiere autenticación (login o token) para todas las demás rutas
                .anyRequest().authenticated() 
            );

        return http.build();
    }
    
    // Necesario para encriptar contraseñas. Esto es crucial para un registro seguro.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
