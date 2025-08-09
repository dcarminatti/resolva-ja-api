package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.TicketFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketFeedbackRepository extends JpaRepository<TicketFeedback, Long> {
    TicketFeedback findByTicketId(Long ticketId);
}

