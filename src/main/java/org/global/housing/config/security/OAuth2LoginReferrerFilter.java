package org.global.housing.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Intercepta la petición a /oauth2/authorization/google y guarda el origen del frontend
 * en la sesión HTTP para que el SuccessHandler sepa a qué frontend redirigir.
 *
 * El frontend DEBE llamar así:
 *   GET /oauth2/authorization/google?redirect_uri=https://inmoscanner.com
 *
 * Si no envía el param, intenta el Referer como fallback.
 */
@Component
public class OAuth2LoginReferrerFilter extends OncePerRequestFilter {

    public static final String FRONTEND_ORIGIN_ATTR = "FRONTEND_ORIGIN";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/oauth2/authorization")) {
            // 1) Prioridad: query param explícito del frontend
            String redirectUri = request.getParameter("redirect_uri");
            if (redirectUri != null && !redirectUri.isBlank()) {
                request.getSession().setAttribute(FRONTEND_ORIGIN_ATTR, redirectUri.trim());
            } else {
                // 2) Fallback: Referer (funciona en local, no siempre en cross-origin)
                String referer = request.getHeader("Referer");
                if (referer != null) {
                    request.getSession().setAttribute(FRONTEND_ORIGIN_ATTR, referer);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}


