package dev.dcarminatti.resolva_ja_api.api.controllers.ticket;

import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketHistoryDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.models.entities.TicketHistory;
import dev.dcarminatti.resolva_ja_api.services.TicketHistoryService;
import dev.dcarminatti.resolva_ja_api.services.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/ticket/ticket-history")
public class TicketHistoryController {
    @Autowired
    TicketHistoryService ticketHistoryService;
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketHistoryDTO>> getAll() {
        List<TicketHistoryDTO> dtos = ticketHistoryService.findAll().stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketHistoryDTO> getById(@PathVariable Long id) {
        Optional<TicketHistory> entity = ticketHistoryService.findById(id);
        return entity.map(e -> ResponseEntity.ok(toDTO(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TicketHistoryDTO dto) {
        try {
            TicketHistory entity = toEntity(dto);
            TicketHistory saved = ticketHistoryService.save(entity);
            return ResponseEntity.ok(toDTO(saved));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TicketHistoryDTO dto) {
        try {
            Optional<TicketHistory> existing = ticketHistoryService.findById(id);
            if (existing.isEmpty())
                return ResponseEntity.notFound().build();

            ticketHistoryService.update(id, existing.get());
            return ResponseEntity.noContent().build();
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketHistoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TicketHistoryDTO toDTO(TicketHistory entity) {
        return new TicketHistoryDTO(
                entity.getId(),
                entity.getDate(),
                entity.getPreviousStatus(),
                entity.getCurrentStatus(),
                entity.getNote(),
                entity.getTicket().getId()
        );
    }

    private TicketHistory toEntity(TicketHistoryDTO dto) {
        Ticket ticket = ticketService.findById(dto.ticketId());
        TicketHistory entity = new TicketHistory();
        entity.setDate(dto.date());
        entity.setPreviousStatus(dto.previousStatus());
        entity.setCurrentStatus(dto.currentStatus());
        entity.setNote(dto.note());
        entity.setTicket(ticket);
        return entity;
    }
}
