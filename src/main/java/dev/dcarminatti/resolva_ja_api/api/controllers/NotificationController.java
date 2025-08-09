package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Notification;
import dev.dcarminatti.resolva_ja_api.api.dtos.NotificationDTO;
import dev.dcarminatti.resolva_ja_api.services.NotificationService;
import dev.dcarminatti.resolva_ja_api.services.UserService;
import dev.dcarminatti.resolva_ja_api.services.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<NotificationDTO> getAll() {
        return notificationService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.findById(id);
        if (notification.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(notification.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody NotificationDTO dto) {
        try {
            Notification notification = toEntity(dto);
            Notification saved = notificationService.save(notification);
            return ResponseEntity.ok(toDTO(saved));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody NotificationDTO dto) {
        try {
            Notification notification = toEntity(dto);
            notification.setId(id);
            Notification updated = notificationService.update(notification);
            return ResponseEntity.ok(toDTO(updated));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private NotificationDTO toDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getSendDate(),
                notification.getType(),
                notification.getUser() != null ? notification.getUser().getId() : null,
                notification.getTicket() != null ? notification.getTicket().getId() : null
        );
    }

    private Notification toEntity(NotificationDTO dto) {
        Notification notification = new Notification();
        notification.setId(dto.id());
        notification.setMessage(dto.message());
        notification.setSendDate(dto.sendDate());
        notification.setType(dto.type());
        if (dto.userId() != null) notification.setUser(userService.findById(dto.userId()));
        if (dto.ticketId() != null) notification.setTicket(ticketService.findById(dto.ticketId()));
        return notification;
    }
}

