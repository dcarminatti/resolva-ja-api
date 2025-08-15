package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.SLA;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.models.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository repository;
    @Autowired
    private SLAService sLAService;

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Ticket findById(Long id) {
            Optional<Ticket> ticket = repository.findById(id);
            if (ticket.isEmpty()) {
                throw new RuntimeException("Ticket not found with id: " + id);
            }
            return ticket.get();
    }

    public Ticket save(Ticket entity) {
        entity.setId(null);
        entity.setCreationDate(new Date().toString());
        this.validateTicket(entity);
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void update(Long id, Ticket ticket) {
        Optional<Ticket> existingTicket = repository.findById(id);
        if (existingTicket.isEmpty()) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticket.setId(id);
        this.validateTicket(ticket);
        repository.save(ticket);
    }

    private void validateTicket(Ticket entity) {
        if (entity.getTitle() == null || entity.getTitle().isEmpty()) {
            throw new RuntimeException("Title must be provided for Ticket.");
        }
        if (entity.getDescription() == null || entity.getDescription().isEmpty()) {
            throw new RuntimeException("Description must be provided for Ticket.");
        }
        if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
            throw new RuntimeException("Status must be provided for Ticket.");
        }
    }
}

