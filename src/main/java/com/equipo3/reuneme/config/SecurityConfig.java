package com.equipo3.reuneme.config;

import com.equipo3.reuneme.security.JwtAuthenticationFilter;
import com.equipo3.reuneme.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Replaces @EnableGlobalMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors() // Habilita CORS sin usar Customizer
            .and()
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para APIs REST
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No mantener estado
            .authorizeHttpRequests((requests) -> requests
            		
            		.requestMatchers(new AntPathRequestMatcher("/empleados/reunion/{idReunion}/asistente/{idUsuario}")).hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/empleados/reunion/organizador")).hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/empleados/reunion/asiste")).hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/empleados/reunion/{id}/cancelar")).hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/admins/modificarEmpleado")).hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/empleados/")).hasRole("EMPLOYEE") // Solo accesible para empleados
                    .requestMatchers(new AntPathRequestMatcher("/reunion/")).hasAnyRole("EMPLOYEE", "ADMIN")
                    .requestMatchers(new AntPathRequestMatcher("/reset-password")).permitAll()
                       
            		
            		
            	.requestMatchers(new AntPathRequestMatcher("/users/activar-2fa")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/users/verify-2fa")).permitAll()
            	.requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/users/desactivar-2fa")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/tokens/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/pwd/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/admins/getUserRoleByEmail")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/admins/modificarEmpleado")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/admins/todasAusencias")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/admins/**")).hasRole("ADMIN") 
                .requestMatchers(new AntPathRequestMatcher("/empleados/verDatos")).hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/empleados/**")).hasRole("EMPLOYEE") // Solo accesible para empleados
                .requestMatchers(new AntPathRequestMatcher("/reunion/**")).hasAnyRole("EMPLOYEE", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/reset-password")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/public/**"), 
                                 new AntPathRequestMatcher("/pwd/**"), 
                                 new AntPathRequestMatcher("/tokens/**")).permitAll() // Rutas públicas
                .anyRequest().authenticated() // Otras rutas requieren autenticación
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // Añadir filtro de JWT

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://web.swey.net")); // Especifica tus orígenes permitidos
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Permitir credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
            .password(passwordEncoder().encode("adminPass"))
            .roles("ADMIN")
            .build());
        manager.createUser(User.withUsername("employee")
            .password(passwordEncoder().encode("employeePass"))
            .roles("EMPLOYEE")
            .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

