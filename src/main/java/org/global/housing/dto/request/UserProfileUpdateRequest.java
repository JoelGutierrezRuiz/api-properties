package org.global.housing.dto.request;

public record UserProfileUpdateRequest(
        String nombre,
        String apellidos,
        String telefono,
        String direccion,
        String displayName,
        String profilePhotoClob
) {}
