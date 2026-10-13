package com.dailyplanner.hotelbookingbackend.config;

import com.dailyplanner.hotelbookingbackend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                // Tumia CORS config iliyo chini (corsConfigurationSource)
                .cors(cors -> {})

                // Hakuna session; kila request inatambuliwa kwa JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth
                        // MPANGILIO NI MUHIMU: sheria maalum kwanza, anyRequest() mwisho kabisa

                        // Preflight requests za browser (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Spring inatumia /error kurudisha makosa halisi (400, 409, n.k.).
                        // Bila hii, makosa yote yanaonekana kama 401 kwenye browser.
                        .requestMatchers("/error").permitAll()

                        // Login na register ziko wazi kwa kila mtu
                        .requestMatchers("/api/auth/**").permitAll()

                        // Admin pekee ndiye anaweza kuongeza/kubadilisha/kufuta rooms
                        .requestMatchers(HttpMethod.POST, "/api/rooms").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/rooms/**").hasRole("ADMIN")

                        // Endpoints za admin (mfano bookings zote)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Kila kitu kingine kinahitaji kuwa umelogin. HII LAZIMA IWE YA MWISHO.
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                        ))

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    // Inaeleza ni origin zipi zinaruhusiwa kuwasiliana na backend
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // React dev server (Vite)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // ikiwemo Authorization na Content-Type
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}