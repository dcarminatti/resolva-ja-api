package dev.dcarminatti.resolva_ja_api.api.dtos.ticket;

public record TicketCommentDTO(
    Long id,
    String comment,
    Long userId,
    Long ticketId
) {
}
