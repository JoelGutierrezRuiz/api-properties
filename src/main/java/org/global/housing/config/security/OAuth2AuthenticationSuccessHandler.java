package org.global.housing.config.security;

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
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final String frontendUrl;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, Environment env) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.frontendUrl = env.getProperty("app.frontend.url", "http://localhost:4200/auth/success");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String email = null;
        String role = "USER";

        Object principal = authentication.getPrincipal();
        if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User user = (DefaultOAuth2User) principal;
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

        String redirectUrl = frontendUrl + "?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}

