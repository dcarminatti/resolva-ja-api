package dev.dcarminatti.resolva_ja_api.api.dtos.ticket;

public record TicketFeedbackDTO(
    Long id,
    Long rating,
    String comment,
    String date,
    Long ticketId

) {
}
