package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.api.dtos.AttachmentDTO;
import dev.dcarminatti.resolva_ja_api.models.entities.Attachment;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.services.AttachmentService;
import dev.dcarminatti.resolva_ja_api.services.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/attachment")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private TicketService ticketService;

//    @GetMapping("/{id}")
//    public ResponseEntity<AttachmentDTO> download(@PathVariable Long id) {
//        Attachment attachment = attachmentService.download(id);
//        if (attachment == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(toDTO(attachment));
//    }

    @PostMapping("/{ticketId}")
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

