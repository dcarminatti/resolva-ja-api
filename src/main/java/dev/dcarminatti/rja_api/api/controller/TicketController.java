package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.InvalidTicketStatusException;
import dev.dcarminatti.rja_api.model.entity.Ticket;
import dev.dcarminatti.rja_api.model.enums.Status;
import dev.dcarminatti.rja_api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        Ticket savedTicket = ticketService.save(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.update(id, ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (!ticketService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Ticket", "id", id);
        }
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ticket>> getTicketsByStatus(@PathVariable Status status) {
        List<Ticket> tickets = ticketService.findByStatus(status);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/requester/{requesterId}")
    public ResponseEntity<List<Ticket>> getTicketsByRequester(@PathVariable Long requesterId) {
        List<Ticket> tickets = ticketService.findByRequester(requesterId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Ticket>> getTicketsByLocation(@PathVariable Long locationId) {
        List<Ticket> tickets = ticketService.findByLocation(locationId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<List<Ticket>> getTicketsBySubcategory(@PathVariable Long subcategoryId) {
        List<Ticket> tickets = ticketService.findBySubcategory(subcategoryId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<Ticket>> getTicketsByTechnician(@PathVariable Long technicianId) {
        List<Ticket> tickets = ticketService.findByTechnician(technicianId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Ticket>> searchTicketsByTitle(@RequestParam String title) {
        List<Ticket> tickets = ticketService.findByTitleContaining(title);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<Ticket>> searchTicketsByDescription(@RequestParam String description) {
        List<Ticket> tickets = ticketService.findByDescriptionContaining(description);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Ticket>> getTicketsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Ticket> tickets = ticketService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Ticket>> getOverdueTickets() {
        List<Ticket> tickets = ticketService.findOverdueTickets();
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Ticket> closeTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.closeTicket(id);
        return ResponseEntity.ok(ticket);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long id, @RequestParam Status status) {
        Ticket currentTicket = ticketService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
        
        // Business logic validation
        if (currentTicket.getStatus() == Status.RESOLVED && status != Status.RESOLVED) {
            throw new InvalidTicketStatusException(id, currentTicket.getStatus().name(), status.name());
        }
        
        Ticket ticket = ticketService.updateStatus(id, status);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/count/requester/{requesterId}")
    public ResponseEntity<Long> countTicketsByRequester(@PathVariable Long requesterId) {
        Long count = ticketService.countByRequester(requesterId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countTicketsByStatus(@PathVariable Status status) {
        Long count = ticketService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/open")
    public ResponseEntity<List<Ticket>> getOpenTickets() {
        List<Ticket> tickets = ticketService.findOpenTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/in-progress")
    public ResponseEntity<List<Ticket>> getInProgressTickets() {
        List<Ticket> tickets = ticketService.findInProgressTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/closed")
    public ResponseEntity<List<Ticket>> getClosedTickets() {
        List<Ticket> tickets = ticketService.findClosedTickets();
        return ResponseEntity.ok(tickets);
    }
}
