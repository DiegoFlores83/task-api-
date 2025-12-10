package com.diegoflores.jwtapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                // Habilitar CORS globalmente
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //Desactivar CSRF porque usaremos JWT(no sesiones)
                .csrf(csrf -> csrf.disable())
                //Decirle a Spring que rutas son publicas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/users/register", "/api/test/public").permitAll() //Login,registro
                        // 🔒 Rutas protegidas
                        .requestMatchers("/api/test/private", "/api/tasks/**").authenticated()
                        // (opcional) rutas con roles
                        .requestMatchers("/api/test/admin").hasRole("ADMIN")
                        // todo lo demás también protegido
                        .anyRequest().authenticated() // el resto requiere token
                )
                // Politica sin sesiones
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Agregar el filtro JWT antes del de autenticacion por username/password
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir origen del frontend
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir envío de cookies/autorización (si es necesario)
        configuration.setAllowCredentials(true);

        // Registrar configuración para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    //Bean para encriptar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    //Bean para AuthenticationManager (necesario para login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return  configuration.getAuthenticationManager();
    }

}
