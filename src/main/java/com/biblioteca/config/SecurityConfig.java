package com.biblioteca.config;

import com.biblioteca.model.Rol;
import com.biblioteca.repository.UsuarioRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UsuarioRepository usuarioRepository) {
        return email -> usuarioRepository.findByEmail(email)
                .map(usuario -> User.withUsername(usuario.getEmail())
                        .password(usuario.getPassword())
                        .authorities(usuario.getRol().getNombre())
                        .disabled(!usuario.isActivo())
                        .build())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado."));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/error", "/login", "/registro").permitAll()
                .requestMatchers("/admin/**").hasAuthority(Rol.ADMIN)
                .requestMatchers("/user/**").hasAuthority(Rol.USER)
                // Protege el CRUD mientras siga en sus rutas del Producto 2.
                .requestMatchers(HttpMethod.POST, "/libros/**")
                    .hasAuthority(Rol.ADMIN)
                .requestMatchers("/libros/nuevo", "/libros/*/editar")
                    .hasAuthority(Rol.ADMIN)
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/", false)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}