package dev.dcarminatti.resolva_ja_api.api.controllers.ticket;

import dev.dcarminatti.resolva_ja_api.api.dtos.ticket.TicketCommentDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.models.entities.TicketComment;
import dev.dcarminatti.resolva_ja_api.models.entities.User;
import dev.dcarminatti.resolva_ja_api.services.TicketCommentService;
import dev.dcarminatti.resolva_ja_api.services.TicketService;
import dev.dcarminatti.resolva_ja_api.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/ticket/ticket-comment")
public class TicketCommentController {
    @Autowired
    private TicketCommentService ticketCommentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketCommentDTO>> getAll() {
        return ResponseEntity.ok(ticketCommentService.findAll().stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCommentDTO> getById(@PathVariable Long id) {
        TicketComment comment = ticketCommentService.findById(id);
        return ResponseEntity.ok(toDTO(comment));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TicketCommentDTO dto) {
        try {
            TicketComment comment = toEntity(dto);
            TicketComment savedComment = ticketCommentService.save(comment);
            return ResponseEntity.ok(toDTO(savedComment));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TicketCommentDTO dto) {
        try {
            TicketComment comment = toEntity(dto);
            ticketCommentService.update(id, comment);
            return ResponseEntity.noContent().build();
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketCommentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TicketComment toEntity(TicketCommentDTO dto) {
        User user = userService.findById(dto.userId());
        Ticket ticket = ticketService.findById(dto.ticketId());
        TicketComment comment = new TicketComment();
        comment.setId(dto.id());
        comment.setComment(dto.comment());
        comment.setUser(user);
        comment.setTicket(ticket);
        return comment;
    }

    private TicketCommentDTO toDTO(TicketComment comment) {
        return new TicketCommentDTO(
                comment.getId(),
                comment.getComment(),
                comment.getUser().getId(),
                comment.getTicket().getId()
        );
    }
}
