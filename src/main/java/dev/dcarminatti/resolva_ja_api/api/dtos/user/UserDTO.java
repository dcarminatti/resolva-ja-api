package dev.dcarminatti.resolva_ja_api.api.dtos.user;

public record UserDTO(
        Long id,
        String name,
        String email,
        String password,
        String createdAt,
        String updatedAt
) {
}
