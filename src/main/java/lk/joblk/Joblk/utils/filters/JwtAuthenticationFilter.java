package lk.joblk.Joblk.utils.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.joblk.Joblk.service.UserService;
import lk.joblk.Joblk.service.auth.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader ("Authorization");
        if (authHeader == null || !authHeader.startsWith ("Bearer")) {
            filterChain.doFilter (request, response);
            return;
        }

        String jwt = authHeader.substring (7);
        String username = jwtService.extractUsername (jwt);

        if (username != null && SecurityContextHolder.getContext ().getAuthentication () == null) {
            UserDetails userDetails = userService.loadUserByUsername (username);
            if (userDetails != null && jwtService.isTokenValid (jwt)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken (username, userDetails.getPassword (), userDetails.getAuthorities ());
                authenticationToken.setDetails (new WebAuthenticationDetailsSource ().buildDetails (request));
                SecurityContextHolder.getContext ().setAuthentication (authenticationToken);
            }
        }
        filterChain.doFilter (request, response);
    }


}
