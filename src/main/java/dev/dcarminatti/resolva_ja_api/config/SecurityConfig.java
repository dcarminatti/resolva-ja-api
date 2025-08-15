package dev.dcarminatti.resolva_ja_api.config;

import dev.dcarminatti.resolva_ja_api.security.JwtAuthFilter;
import dev.dcarminatti.resolva_ja_api.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "/api/v1/auth/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/ticket/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/ticket/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/attachment/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/v1/attachment/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/sla/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/notification/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/department/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/v1/category/**").hasAnyRole("TECHNICIAN", "USER")
                                .requestMatchers("/api/v1/user/**").hasRole("ADMIN")
                )
                .userDetailsService(userService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:5173", "https://seu-front.vercel.app"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
