package dev.dcarminatti.resolva_ja_api.api.controllers.ticket;

import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketCommentDTO;
import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketDTO;
import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketFeedbackDTO;
import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketHistoryDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.*;
import dev.dcarminatti.resolva_ja_api.services.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketCommentService ticketCommentService;
    @Autowired
    private TicketHistoryService ticketHistoryService;
    @Autowired
    private TicketFeedbackService ticketFeedbackService;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private SLAService sLAService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAll() {
        return ResponseEntity.ok(ticketService.findAll().stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getById(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id);
        return ResponseEntity.ok(toDTO(ticket));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TicketDTO dto) {
        try {
            Ticket ticket = toEntity(dto);
            Ticket savedTicket = ticketService.save(ticket);
            return ResponseEntity.ok(toDTO(savedTicket));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TicketDTO dto) {
        try {
            Ticket ticket = toEntity(dto);
            ticketService.update(id, ticket);
            return ResponseEntity.noContent().build();
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Ticket toEntity(TicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.id());
        ticket.setTitle(dto.title());
        ticket.setDescription(dto.description());
        ticket.setStatus(dto.status());
        ticket.setPriority(dto.priority());
        ticket.setDeadline(dto.deadline());
        if (dto.userId() != null) {
            User user = userService.findById(dto.userId());
            ticket.setRequester(user);
        }

        Optional<Technician> technician = technicianService.findById(dto.technicianId());
        Optional<Category> category = categoryService.findById(dto.categoryId());

        if (technician.isPresent()) {
            ticket.setTechnician(technician.get());
        } else {
            ticket.setTechnician(null);
        }

        if (category.isPresent()) {
            ticket.setCategory(category.get());
        } else {
            ticket.setCategory(null);
        }

        return ticket;
    }

    private TicketDTO toDTO(Ticket ticket) {
        List<TicketCommentDTO> comments = ticketCommentService.findByTicketId(ticket.getId()).stream().map(
                entity -> new TicketCommentDTO(
                        entity.getId(),
                        entity.getComment(),
                        entity.getUser().getId(),
                        entity.getTicket().getId()
                )
        ).toList();
        List<TicketHistoryDTO> history = ticketHistoryService.findByTicketId(ticket.getId()).stream().map(
                entity -> new TicketHistoryDTO(
                        entity.getId(),
                        entity.getDate(),
                        entity.getPreviousStatus(),
                        entity.getCurrentStatus(),
                        entity.getNote(),
                        entity.getTicket().getId()
                )
        ).toList();
        TicketFeedback feedback = ticketFeedbackService.findByTicketId(ticket.getId());
        TicketFeedbackDTO feedbackDTO = new TicketFeedbackDTO(
                feedback != null ? feedback.getId() : null,
                feedback != null ? feedback.getRating() : null,
                feedback != null ? feedback.getComment() : null,
                feedback != null ? feedback.getDate() : null,
                feedback != null ? feedback.getTicket().getId() : null
        );

        Optional<SLA> sla = sLAService.findById(ticket.getCategory().getSla().getId());

        return new TicketDTO(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCreationDate(),
                ticket.getStatus(),ticket.getPriority(),
                ticket.getDeadline(),
                ticket.getRequester() != null ? ticket.getRequester().getId() : null,
                ticket.getTechnician() != null ? ticket.getTechnician().getId() : null,
                sla.isPresent() ? sla.get().getId() : null,
                ticket.getCategory() != null ? ticket.getCategory().getId() : null,
                comments,
                history,
                feedbackDTO
        );
    }
}
