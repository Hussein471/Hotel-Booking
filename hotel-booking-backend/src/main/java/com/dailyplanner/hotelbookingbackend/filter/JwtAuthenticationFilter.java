package com.dailyplanner.hotelbookingbackend.filter;


import com.dailyplanner.hotelbookingbackend.repository.AppUserRepository;
import com.dailyplanner.hotelbookingbackend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AppUserRepository appUserRepository
    ) {
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Tunachukua Authorization header.
        String authorizationHeader = request.getHeader("Authorization");

        // Header isipokuwa Bearer token, tunaendelea bila authentication.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }


        String token = authorizationHeader.substring(7);




        try {
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token); // mpya

            if (SecurityContextHolder.getContext().getAuthentication() == null
                    && appUserRepository.existsByEmail(email)) {

                // "ROLE_" prefix ni lazima kwa Spring Security kutambua hasRole("ADMIN")
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JwtException | IllegalArgumentException exception) {
            // token mbovu, tunapitisha bila auth (kama ilivyokuwa)
        }


        try {
            String email = jwtService.extractEmail(token);

            // DEBUG - ondoa baadaye
            System.out.println("DEBUG: email kutoka token = " + email);
            System.out.println("DEBUG: existsByEmail = " + appUserRepository.existsByEmail(email));

            if (SecurityContextHolder.getContext().getAuthentication() == null
                    && appUserRepository.existsByEmail(email)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                // DEBUG
                System.out.println("DEBUG: authentication imewekwa kikamilifu");
            }

        } catch (JwtException | IllegalArgumentException exception) {
            // DEBUG - hii ndiyo tunayotaka kuona kama inapigwa
            System.out.println("DEBUG: JWT EXCEPTION = " + exception.getMessage());
        }


        filterChain.doFilter(request, response);
    }
    }

