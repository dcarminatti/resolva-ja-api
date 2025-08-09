package dev.dcarminatti.resolva_ja_api.api.dtos.auth;

public record LoginResponseDTO(
        String email,
        String token
) {
}
