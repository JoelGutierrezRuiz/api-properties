package org.global.housing.config.security;

import org.global.housing.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationSuccessHandler successHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginReferrerFilter oAuth2LoginReferrerFilter;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          AuthenticationSuccessHandler successHandler,
                          JwtAuthenticationFilter jwtAuthenticationFilter,
                          OAuth2LoginReferrerFilter oAuth2LoginReferrerFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.successHandler = successHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2LoginReferrerFilter = oAuth2LoginReferrerFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/user/**").authenticated()
                        // scrapers externos (pisos.com, idealista...): solo usuarios logueados con Google
                        .requestMatchers("/api/pisos/**", "/api/idealista/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(oAuth2LoginReferrerFilter, OAuth2AuthorizationRequestRedirectFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(successHandler)
                );

        return http.build();
    }
}
