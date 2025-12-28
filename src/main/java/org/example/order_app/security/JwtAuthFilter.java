package org.example.order_app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsImplService userDetailsImplService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt); //извлекаем jwt токен
                UserDetails userDetails = userDetailsImplService.loadUserByUsername(username); //загружаем пользователя из бд по имени
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()); //создаем объект для аутентификации
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)); //добавляем детали запроса
                SecurityContextHolder.getContext().setAuthentication(authentication); //установка аутентификации в контекст безопасности
            }
        }catch (Exception e){
           logger.error("Failed to establish authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
//Извлекаем jwt токен из запроса, проверяем его и возвращаем токен если он удовлетворяет условиям и null если нет
    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");
        if(headerAuth.startsWith("Bearer ") && StringUtils.hasText(headerAuth)){
            return headerAuth.substring(7);
        }
        return null;
    }
}
