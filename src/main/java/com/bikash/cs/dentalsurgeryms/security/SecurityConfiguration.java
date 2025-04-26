package com.bikash.cs.dentalsurgeryms.security;

import com.bikash.cs.dentalsurgeryms.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/v1/auth/*").permitAll()
                        .requestMatchers("/api/v1/surgeries").hasRole(Role.MANAGER.name())
                        .requestMatchers("/api/v1/surgeries/*").hasRole(Role.MANAGER.name())

                        .requestMatchers(HttpMethod.POST, "/api/v1/appointments").hasAnyRole( Role.PATIENT.name())
                        .requestMatchers("/api/v1/appointments/{id}/cancel").hasAnyRole(Role.PATIENT.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/appointments/patient/{id}").hasAnyRole(Role.PATIENT.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/appointments/dentist/{id}").hasAnyRole(Role.DENTIST.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/appointments").hasRole(Role.MANAGER.name())
                        .requestMatchers("/api/v1/appointments/*").hasRole(Role.MANAGER.name())

                        .requestMatchers(HttpMethod.GET,"/api/v1/patients").hasRole(Role.MANAGER.name())
                        .requestMatchers("/api/v1/patients/{id}/appointments").hasAnyRole(Role.PATIENT.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/patients/{id}").hasAnyRole(Role.PATIENT.name(), Role.MANAGER.name())

                        .requestMatchers(HttpMethod.POST,"/api/v1/dentists").hasRole(Role.PATIENT.name())
                        .requestMatchers(HttpMethod.GET,"/api/v1/dentists").hasRole(Role.MANAGER.name())
                        .requestMatchers( "/api/v1/dentists/{id}/appointments").hasAnyRole(Role.DENTIST.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/dentists/{id}").hasAnyRole(Role.DENTIST.name(), Role.MANAGER.name())

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();


    }

}
