package dev.dcarminatti.resolva_ja_api.api.dtos.ticket;

public record TicketHistoryDTO(
    Long id,
    String date,
    String previousStatus,
    String currentStatus,
    String note,
    Long ticketId
) {
}
