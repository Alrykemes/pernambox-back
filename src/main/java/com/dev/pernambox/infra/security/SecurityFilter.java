package com.dev.pernambox.infra.security;

import com.auth0.jwt.interfaces.Claim;
import com.dev.pernambox.utils.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Posteriormente verificação de origin e referer para bloquear de origem que não seja o nosso front.

        String path = request.getServletPath();

        // 🚫 Ignora as rotas públicas (não tenta validar JWT nelas)
        if (path.startsWith("/auth/password-reset") || path.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        if (token != null) {
            Map<String, Claim> payloadToken = tokenService.validateToken(
                    token,
                    RequestUtils.getRequestIp(request),
                    RequestUtils.getRequestUserAgent(request));

            UserDetails user = userDetailsService.loadUserByUsername(payloadToken.get("email").toString().replaceAll("\"", ""));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}