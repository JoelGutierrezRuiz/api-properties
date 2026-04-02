package org.global.housing.dto;

public record UserProfileDto(
        Long id,
        String email,
        String nombre,
        String apellidos,
        String telefono,
        String direccion,
        String displayName,
        String profilePhotoClob,
        String role
) {}
