package dev.dcarminatti.resolva_ja_api.api.dtos.ticket;

import java.util.List;

public record TicketDTO(
    Long id,
    String title,
    String description,
    String creationDate,
    String status,
    String priority,
    String deadline,
    Long userId,
    Long technicianId,
    Long SLAId,
    Long categoryId,
    List<TicketCommentDTO> comments,
    List<TicketHistoryDTO> history,
    TicketFeedbackDTO feedback
) {
}
