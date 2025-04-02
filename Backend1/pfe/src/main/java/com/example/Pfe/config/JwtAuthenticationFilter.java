package com.example.Pfe.config;


import com.example.Pfe.service.UserService;
import com.example.Pfe.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Vérifier si la requête est pour un endpoint public
        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/auth/")) {
            System.out.println("Requête publique ignorée par JwtAuthenticationFilter : " + path);
            filterChain.doFilter(request, response);
            return;
        }
        // Existing implementation remains the same
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Header Authorization reçu : " + authHeader);

        final String userEmail;

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            System.out.println("Aucun token valide trouvé, passage au filtre suivant");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        System.out.println("Token JWT extrait : " + jwt);
        // Check if the token has exactly 2 period characters (three parts)
        if (jwt.chars().filter(ch -> ch == '.').count() != 2) {
            System.out.println("Token mal formé, passage au filtre suivant");
            filterChain.doFilter(request, response);
            return;
        }
        userEmail = jwtService.extractUserName(jwt);
        System.out.println("Email extrait du token : " + userEmail);

        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
            System.out.println("Utilisateur chargé : " + userDetails.getUsername() + ", Rôles : " + userDetails.getAuthorities());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("Token valide, authentification définie");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else {
                System.out.println("Token invalide pour " + userEmail);
            }
        }else {
            System.out.println("Email vide ou authentification déjà présente");
        }
        filterChain.doFilter(request, response);
    }
}
