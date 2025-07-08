package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Ticket;
import dev.dcarminatti.rja_api.model.enums.Status;
import dev.dcarminatti.rja_api.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Basic CRUD operations
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket save(Ticket ticket) {
        if (ticket.getOpeningDateTime() == null) {
            ticket.setOpeningDateTime(LocalDateTime.now());
        }
        if (ticket.getStatus() == null) {
            ticket.setStatus(Status.OPEN);
        }
        return ticketRepository.save(ticket);
    }

    public Ticket update(Long id, Ticket updatedTicket) {
        return ticketRepository.findById(id)
                .map(ticket -> {
                    ticket.setTitle(updatedTicket.getTitle());
                    ticket.setDescription(updatedTicket.getDescription());
                    ticket.setLocation(updatedTicket.getLocation());
                    ticket.setAssociatedSubcategory(updatedTicket.getAssociatedSubcategory());
                    ticket.setStatus(updatedTicket.getStatus());
                    ticket.setRequester(updatedTicket.getRequester());
                    ticket.setAssociatedTechnicians(updatedTicket.getAssociatedTechnicians());
                    ticket.setClosingDateTime(updatedTicket.getClosingDateTime());
                    return ticketRepository.save(ticket);
                })
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }
}
