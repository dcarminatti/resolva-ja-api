package dev.dcarminatti.resolva_ja_api.api.dtos.auth;

public record RegisterRequestDTO(
        String name,
        String email,
        String password
) {
}
