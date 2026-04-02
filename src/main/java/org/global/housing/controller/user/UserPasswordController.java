package org.global.housing.controller.user;

import org.global.housing.dto.request.PasswordUpdateRequest;
import org.global.housing.entity.login.UserEntity;
import org.global.housing.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserPasswordController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPasswordController(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/password")
    public String updatePassword(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody PasswordUpdateRequest req) {

        String email = principal.getAttribute("email");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!req.newPassword().equals(req.confirmPassword())) {
            return "Las contraseñas no coinciden";
        }

        // Si no tiene password (usuario solo Google) → no exigimos currentPassword
        if (user.getPasswordHash() == null) {
            user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
            userRepository.save(user);
            return"Contraseña creada correctamente";
        }

        // Si tiene password → validamos la actual
        if (!passwordEncoder.matches(req.currentPassword(), user.getPasswordHash())) {
            return "La contraseña actual es incorrecta";
        }

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);

        return "Contraseña actualizada correctamente";
    }
}
