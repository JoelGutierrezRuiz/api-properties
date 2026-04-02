package org.global.housing.service;

import org.global.housing.entity.login.UserEntity;
import org.global.housing.repository.MunicipalityRepository;
import org.global.housing.repository.UserFavoriteMunicipalityRepository;
import org.global.housing.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final MunicipalityRepository municipalityRepository;

    private final UserFavoriteMunicipalityRepository userFavoriteMunicipalityRepository;

    public CustomOAuth2UserService(UserRepository userRepository,
                                   MunicipalityRepository municipalityRepository,
                                   UserFavoriteMunicipalityRepository userFavoriteMunicipalityRepository) {
        this.userRepository = userRepository;
        this.municipalityRepository = municipalityRepository;
        this.userFavoriteMunicipalityRepository = userFavoriteMunicipalityRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String nombre = oAuth2User.getAttribute("given_name");
        String apellidos = oAuth2User.getAttribute("family_name");
        String name = oAuth2User.getAttribute("name");

        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity u = new UserEntity();
                    u.setEmail(email);
                    u.setNombre(nombre);
                    u.setApellidos(apellidos);
                    u.setDisplayName(name);
                    u.setGoogleId(googleId);
                    u.setProvider(provider.toUpperCase());
                    u.setRole("USER");
                    return userRepository.save(u);
                });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole())),
                oAuth2User.getAttributes(),
                "sub"
        );
    }

    public boolean saveLikedMunicipality(OAuth2UserRequest userRequest){
        // simple placeholder: not used currently, return true to indicate success
        return true;
    }

}
