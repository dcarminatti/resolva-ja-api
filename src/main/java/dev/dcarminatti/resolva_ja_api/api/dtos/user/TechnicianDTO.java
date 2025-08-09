package dev.dcarminatti.resolva_ja_api.api.dtos.user;

public record TechnicianDTO(
        Long id,
        String name,
        String email,
        String password,
        String createdAt,
        String updatedAt,
        String specialty
) {
}
