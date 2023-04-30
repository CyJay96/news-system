package ru.clevertec.ecl.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.ecl.authservice.security.jwt.JwtConfigurer;
import ru.clevertec.ecl.authservice.security.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ROLE_ADMIN = "ADMIN";

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/v0/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v0/news/**", "/v0/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/v0/news/**").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.PUT, "/v0/news/**").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.PATCH, "/v0/news/**").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/v0/news/**").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.PATCH, "/v0/users/block/**", "/v0/users/unblock/**").hasRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/v0/users/**").hasRole(ROLE_ADMIN)
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .build();
    }

}
