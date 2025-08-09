package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
    List<TicketComment> findByTicketId(Long ticketId);
}
