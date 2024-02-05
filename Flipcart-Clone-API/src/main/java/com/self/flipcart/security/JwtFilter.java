package com.self.flipcart.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.self.flipcart.exceptions.UserNotLoggedInException;
import com.self.flipcart.repository.AccessTokenRepo;
import com.self.flipcart.util.SimpleResponseStructure;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private CustomUserDetailsService userDetailsService;
    private AccessTokenRepo accessTokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("at")) accessToken = cookie.getValue();
            }

        try {
            log.info("Authenticating Token with JWT Filter...");
            final String finalToken = accessToken;
            String username = null;

            if (accessToken != null) {
                username = accessTokenRepo.findByToken(accessToken).map(at -> {
                    log.info("Extracting username...");
                    // exception doesn't get handled here,
                    if (at.isBlocked()) {
                        try {
                            handleException(new UserNotLoggedInException("The access is blocked, Please login again"), response, "Failed to Authenticate User");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return jwtService.extractUsername(finalToken);
                }).orElseThrow(() -> new UserNotLoggedInException("Failed to authenticate the user"));
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;
                userDetails = userDetailsService.loadUserByUsername(username);
                log.info("Creating authentication token...");
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                        null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                log.info("JWT Authentication Successful");
            }
        } catch (ExpiredJwtException ex) {
            handleException(ex, response, "Your AccessToken is expired, refresh your login");
        } catch (JwtException ex) {
            handleException(ex, response, "Authentication Failed");
        }
        filterChain.doFilter(request, response);
    }

    private void handleException(RuntimeException ex, HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("Application/json");
        response.setHeader("error", ex.getMessage());
        SimpleResponseStructure structure = new SimpleResponseStructure()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setMessage(message + " | " + ex.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), structure);
    }
}