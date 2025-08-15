package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.api.dtos.AttachmentDTO;
import dev.dcarminatti.resolva_ja_api.models.entities.Attachment;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.services.AttachmentService;
import dev.dcarminatti.resolva_ja_api.services.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/attachment")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        Attachment attachment = attachmentService.findById(id);
        if (attachment == null) return ResponseEntity.notFound().build();

        try {
            Path filePath = Paths.get(attachment.getFilePath());
            byte[] fileBytes = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getFileType()))
                    .header("Content-Disposition", "attachment; filename=\"" + filePath.getFileName() + "\"")
                    .body(fileBytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/{ticketId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachmentDTO> upload(@PathVariable Long ticketId, @RequestParam("file")MultipartFile file) {
        if (file.isEmpty()) return ResponseEntity.badRequest().build();
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) return ResponseEntity.notFound().build();

        Attachment saved = attachmentService.upload(ticket, file);
        if (saved == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(toDTO(saved));
    }

    private AttachmentDTO toDTO(Attachment attachment) {
        return new AttachmentDTO(
            attachment.getId(),
            attachment.getFileType(),
            attachment.getFilePath(),
            attachment.getTicket() != null ? attachment.getTicket().getId() : null
        );
    }

    private Attachment toEntity(AttachmentDTO dto) {
        Attachment attachment = new Attachment();
        attachment.setId(dto.id());
        attachment.setFileType(dto.fileType());
        attachment.setFilePath(dto.filePath());
        if (dto.ticketId() != null) {
            Ticket ticket = ticketService.findById(dto.ticketId());
            attachment.setTicket(ticket);
        }
        return attachment;
    }
}

