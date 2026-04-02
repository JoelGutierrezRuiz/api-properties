package org.global.housing.dto.request;

public record PasswordUpdateRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {}
