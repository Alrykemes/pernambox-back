package com.dev.pernambox.infra.security;

import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.exceptions.PasswordResetException;
import com.dev.pernambox.exceptions.dtos.ErrorResponseDto;
import com.dev.pernambox.utils.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenService tokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Posteriormente verificação de origin e referer para bloquear de origem que não seja o nosso front.

        var token = this.recoverToken(request);

        if (token != null) {
            try {
                String userAgent = RequestUtils.getRequestUserAgent(request);
                String ip = RequestUtils.getRequestIp(request);

                Map<String, Claim> payloadToken = tokenService.validateToken(
                        token,
                        ip,
                        userAgent);

                if ((request.getRequestURI().equals("/auth/password-reset") || request.getServletPath().equals("/auth/password-reset"))
                        && request.getMethod().equals("PATCH")) {
                    if (!payloadToken.get("iss").asString().equals("password-reset")) {
                        log.warn("IP: {} \n User agent: {}, \n Tentando utilizar Jwt inválida para alterar senha", ip, userAgent);
                        handleJwtError(response, request, HttpStatus.UNAUTHORIZED, new PasswordResetException("Invalid token for reset password"));
                        return;
                    }
                }

                // if token is for password reset
                if (payloadToken.get("iss").asString().equals("password-reset")) {
                    // and route is'not for password reset
                    if (!((request.getRequestURI().equals("/auth/password-reset") || request.getServletPath().equals("/auth/password-reset")))
                            && !(request.getMethod().equals("PATCH"))) {
                        log.warn("IP: {} \n User agent: {}, \n Tentando utilizar Jwt inválida (de alterar senha) para acessar outros recursos", ip, userAgent);
                        handleJwtError(response, request, HttpStatus.UNAUTHORIZED, new PasswordResetException("Invalid token for acess resources"));
                        return;
                    }
                }

                UserDetails user = userDetailsService.loadUserByUsername(payloadToken.get("email").toString().replaceAll("\"", ""));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (SecurityException ex) {
                log.error("Authentication Error: {}", ex.getMessage());
                handleJwtError(response, request, HttpStatus.UNAUTHORIZED, ex);
                return;
            } catch (Exception e) {
                StackTraceElement origin = e.getStackTrace()[0];
                log.error("Erro no processo de Autenticação: {} \n Classe: {} \n Método: {} \n Linha: {}",
                        e.getMessage(), origin.getClass(), origin.getMethodName(), origin.getLineNumber());
                handleJwtError(response, request, HttpStatus.INTERNAL_SERVER_ERROR, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private void handleJwtError(HttpServletResponse response,
                                HttpServletRequest request,
                                HttpStatus status,
                                Exception ex) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        ErrorResponseDto errorDto = new ErrorResponseDto(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        response.getWriter().write(mapper.writeValueAsString(errorDto));
    }
}