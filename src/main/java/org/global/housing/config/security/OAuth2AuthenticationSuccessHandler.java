package org.global.housing.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final String defaultFrontendUrl;

    // Orígenes de frontend permitidos
    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:4200",
            "https://inmoscanner.com",
            "https://www.inmoscanner.com"
    );

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, Environment env) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.defaultFrontendUrl = env.getProperty("app.frontend.url", "https://inmoscanner.com/auth/success");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = null;
        String role = "USER";

        Object principal = authentication.getPrincipal();
        if (principal instanceof DefaultOAuth2User user) {
            email = Optional.ofNullable(user.getAttribute("email")).orElse(user.getName()).toString();
            role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst().orElse("ROLE_USER").replace("ROLE_", "");
        }

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No email in OAuth2 user");
            return;
        }

        String token = jwtTokenProvider.createToken(email, role);

        String frontendBase = resolveFrontendOrigin(request);
        // Usamos query param ?token= que funciona con HTTP redirects 302
        String redirectUrl = frontendBase + "/auth/success?token=" + token;

        log.info("OAuth2 login exitoso para: {} | role: {} | redirigiendo a: {}", email, role, redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    /**
     * Detecta desde qué frontend se inició el login.
     * 1) Primero mira la sesión (guardada por OAuth2LoginReferrerFilter con el query param redirect_uri)
     * 2) Luego intenta Referer/Origin de la petición actual
     * 3) Fallback: producción
     */
    private String resolveFrontendOrigin(HttpServletRequest request) {
        // 1) Leer el origen guardado en sesión al inicio del flujo OAuth
        String savedOrigin = null;
        try {
            Object attr = request.getSession(false) != null
                    ? request.getSession(false).getAttribute(OAuth2LoginReferrerFilter.FRONTEND_ORIGIN_ATTR)
                    : null;
            if (attr != null) {
                savedOrigin = attr.toString();
                request.getSession().removeAttribute(OAuth2LoginReferrerFilter.FRONTEND_ORIGIN_ATTR);
            }
        } catch (Exception ignored) {}

        log.info("OAuth2 redirect resolución — sesión={}, Referer={}, Origin={}",
                savedOrigin, request.getHeader("Referer"), request.getHeader("Origin"));

        // 2) Probar sesión, luego Referer, luego Origin
        String referer = request.getHeader("Referer");
        String origin = request.getHeader("Origin");

        for (String header : new String[]{savedOrigin, referer, origin}) {
            if (header != null) {
                for (String allowed : ALLOWED_ORIGINS) {
                    if (header.startsWith(allowed)) {
                        return allowed;
                    }
                }
                if (header.contains(".vercel.app")) {
                    try {
                        java.net.URL url = java.net.URI.create(header).toURL();
                        return url.getProtocol() + "://" + url.getHost();
                    } catch (Exception ignored) {}
                }
            }
        }

        // 3) Fallback
        String fallback = defaultFrontendUrl.replace("/auth/success", "");
        log.info("OAuth2 redirect — no se detectó origen, usando fallback: {}", fallback);
        return fallback;
    }
}

