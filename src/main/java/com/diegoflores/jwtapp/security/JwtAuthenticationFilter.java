package com.diegoflores.jwtapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        //Obtener token desde el header Authorization
        String token = getTokenFromRequest(request);

        // Si no hay token, continuamos solo si la ruta es pública
        if(token == null){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //Validar token
            if (jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);

                //Cargar usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Crear el objeto de autenticacion
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                // Guardar autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
                return;
            }
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Invalid or expired token\"}");
            return;
        }
        //Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    //Metodo para extraer token del header Authorization
    private String getTokenFromRequest(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if(bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
    }
        return null;
    }

    // Evitar que el filtro se ejecute en rutas públicas
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") ||
                path.startsWith("/api/users/register") ||
                path.startsWith("/api/test/public");
    }
}
