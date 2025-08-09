package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

