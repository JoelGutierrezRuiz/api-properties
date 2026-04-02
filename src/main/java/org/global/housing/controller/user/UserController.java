package org.global.housing.controller.user;

import org.global.housing.dto.UserProfileDto;
import org.global.housing.dto.request.UserProfileUpdateRequest;
import org.global.housing.entity.login.UserEntity;
import org.global.housing.repository.UserRepository;
import org.global.housing.repository.MunicipalityRepository;
import org.global.housing.repository.UserFavoriteMunicipalityRepository;
import org.global.housing.service.UserFavoriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final UserFavoriteService userFavoriteService;

    public UserController(UserRepository userRepository,
                          MunicipalityRepository municipalityRepository,
                          UserFavoriteMunicipalityRepository userFavoriteMunicipalityRepository,
                          UserFavoriteService userFavoriteService) {
        this.userRepository = userRepository;
        this.userFavoriteService = userFavoriteService;
    }

    @GetMapping("/me")
    public UserProfileDto me(Authentication authentication) {
        // authentication may be null if request is unauthenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String email = extractEmailFromAuthentication(authentication);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo obtener email del principal");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getNombre(),
                user.getApellidos(),
                user.getTelefono(),
                user.getDireccion(),
                user.getDisplayName(),
                user.getProfilePhotoClob(),
                user.getRole()
        );
    }

    @PutMapping("/me")
    public UserProfileDto updateProfile(
            Authentication authentication,
            @RequestBody UserProfileUpdateRequest req) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String email = extractEmailFromAuthentication(authentication);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo obtener email del principal");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        user.setNombre(req.nombre());
        user.setApellidos(req.apellidos());
        user.setTelefono(req.telefono());
        user.setDireccion(req.direccion());
        user.setDisplayName(req.displayName());
        user.setProfilePhotoClob(req.profilePhotoClob());

        userRepository.save(user);

        return new UserProfileDto(
                user.getId(),
                user.getEmail(),
                user.getNombre(),
                user.getApellidos(),
                user.getTelefono(),
                user.getDireccion(),
                user.getDisplayName(),
                user.getProfilePhotoClob(),
                user.getRole()
        );
    }

    // New endpoint: add a liked municipality for the authenticated user
    @PostMapping("/me/likes")
    public List<String> addLike(Authentication authentication, @RequestBody Map<String, String> body) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String municipalityId = body.get("municipalityId");
        if (municipalityId == null || municipalityId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "municipalityId requerido");
        }

        String email = extractEmailFromAuthentication(authentication);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo obtener email del principal");
        }

        return userFavoriteService.addLikeByEmail(email, municipalityId);
    }

    @GetMapping("/me/likes")
    public List<String> getLikes(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String email = extractEmailFromAuthentication(authentication);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo obtener email del principal");
        }

        return userFavoriteService.listLikesByEmail(email);
    }

    @DeleteMapping("/me/likes/{municipalityId}")
    public List<String> deleteLike(Authentication authentication, @PathVariable String municipalityId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String email = extractEmailFromAuthentication(authentication);
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No se pudo obtener email del principal");
        }

        return userFavoriteService.removeLikeByEmail(email, municipalityId);
    }

    private String extractEmailFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            Object emailAttr = principal.getAttribute("email");
            return emailAttr != null ? emailAttr.toString() : null;
        }

        // If JwtAuthenticationFilter sets the principal as the subject string
        Object principalObj = authentication.getPrincipal();
        if (principalObj instanceof String) {
            return (String) principalObj;
        }

        return null;
    }
}
