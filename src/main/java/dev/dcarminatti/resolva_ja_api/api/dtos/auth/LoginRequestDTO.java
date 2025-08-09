package dev.dcarminatti.resolva_ja_api.api.dtos.auth;

public record LoginRequestDTO(
        String email,
        String password
) {
}
