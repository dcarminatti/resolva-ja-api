package dev.dcarminatti.resolva_ja_api.api.controllers.ticket;

import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketFeedbackDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.models.entities.TicketFeedback;
import dev.dcarminatti.resolva_ja_api.services.TicketFeedbackService;
import dev.dcarminatti.resolva_ja_api.services.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/ticket/ticket-feedback")
public class TicketFeedbackController {
    @Autowired
    private TicketFeedbackService ticketFeedbackService;
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketFeedbackDTO>> getAll() {
        return ResponseEntity.ok(ticketFeedbackService.findAll().stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketFeedbackDTO> getById(@PathVariable Long id) {
        Optional<TicketFeedback> feedback = ticketFeedbackService.findById(id);
        if (feedback.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(toDTO(feedback.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TicketFeedbackDTO dto) {
        try {
            TicketFeedback feedback = toEntity(dto);
            return ResponseEntity.ok(toDTO(ticketFeedbackService.save(feedback)));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TicketFeedbackDTO dto) {
        try {
            TicketFeedback existingFeedback = ticketFeedbackService.findById(id)
                .orElseThrow(() -> new ValidateException("Ticket feedback not found"));

            TicketFeedback updatedFeedback = toEntity(dto);
            updatedFeedback.setId(existingFeedback.getId());
            ticketFeedbackService.update(updatedFeedback);
            return ResponseEntity.noContent().build();
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketFeedbackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TicketFeedback toEntity(TicketFeedbackDTO dto) {
        Ticket ticket = ticketService.findById(dto.ticketId());
        TicketFeedback feedback = new TicketFeedback();
        feedback.setRating(dto.rating());
        feedback.setComment(dto.comment());
        feedback.setDate(dto.date());
        feedback.setTicket(ticket);
        return feedback;
    }

    private TicketFeedbackDTO toDTO(TicketFeedback feedback) {
        return new TicketFeedbackDTO(
            feedback.getId(),
            feedback.getRating(),
            feedback.getComment(),
            feedback.getDate(),
            feedback.getTicket() != null ? feedback.getTicket().getId() : null
        );
    }
}
