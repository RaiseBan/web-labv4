package com.example.backend.config;

import com.example.backend.Utils.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    public SecurityConfig(JwtRequestFilter jwtRequestFilter){
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .authorizeHttpRequests(authz -> authz
                        // Разрешить все запросы к эндпоинтам аутентификации
                        .requestMatchers("/api/users/login", "/api/users/register").permitAll()
                        // Разрешить доступ к статическим ресурсам
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/img/**").permitAll()
                        // Разрешить GET запросы к эндпоинту получения данных о выстрелах без аутентификации
                        .requestMatchers(HttpMethod.GET, "/api/shots").permitAll()
                        // Требовать аутентификацию для всех остальных запросов
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // Отключить CSRF
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Добавить JWT фильтр перед базовой аутентификацией

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
