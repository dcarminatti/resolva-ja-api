package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.InvalidFileException;
import dev.dcarminatti.rja_api.model.entity.Attachment;
import dev.dcarminatti.rja_api.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@CrossOrigin(origins = "*")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @GetMapping
    public ResponseEntity<List<Attachment>> getAllAttachments() {
        List<Attachment> attachments = attachmentService.findAll();
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable Long id) {
        Attachment attachment = attachmentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment", "id", id));
        return ResponseEntity.ok(attachment);
    }

    @PostMapping
    public ResponseEntity<Attachment> createAttachment(@Valid @RequestBody Attachment attachment) {
        if (attachmentService.existsByFileName(attachment.getFileName())) {
            throw new InvalidFileException(attachment.getFileName(), "File name already exists");
        }
        
        Attachment savedAttachment = attachmentService.save(attachment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAttachment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attachment> updateAttachment(@PathVariable Long id, @Valid @RequestBody Attachment attachment) {
        Attachment updatedAttachment = attachmentService.update(id, attachment);
        return ResponseEntity.ok(updatedAttachment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        if (!attachmentService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Attachment", "id", id);
        }
        attachmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByTicket(@PathVariable Long ticketId) {
        List<Attachment> attachments = attachmentService.findByTicket(ticketId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/uploader/{uploaderId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByUploader(@PathVariable Long uploaderId) {
        List<Attachment> attachments = attachmentService.findByUploader(uploaderId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/filename/{fileName}")
    public ResponseEntity<Attachment> getAttachmentByFileName(@PathVariable String fileName) {
        Attachment attachment = attachmentService.findByFileName(fileName)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment", "fileName", fileName));
        return ResponseEntity.ok(attachment);
    }

    @GetMapping("/search/filename")
    public ResponseEntity<List<Attachment>> searchAttachmentsByFileName(@RequestParam String fileName) {
        List<Attachment> attachments = attachmentService.findByFileNameContaining(fileName);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/search/url")
    public ResponseEntity<List<Attachment>> searchAttachmentsByFileUrl(@RequestParam String url) {
        List<Attachment> attachments = attachmentService.findByFileUrlContaining(url);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/count/ticket/{ticketId}")
    public ResponseEntity<Long> countAttachmentsByTicket(@PathVariable Long ticketId) {
        Long count = attachmentService.countByTicket(ticketId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/uploader/{uploaderId}")
    public ResponseEntity<Long> countAttachmentsByUploader(@PathVariable Long uploaderId) {
        Long count = attachmentService.countByUploader(uploaderId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/upload")
    public ResponseEntity<Attachment> uploadAttachmentToTicket(
            @RequestParam String fileName,
            @RequestParam String fileUrl,
            @RequestParam Long ticketId,
            @RequestParam Long uploaderId) {
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new InvalidFileException(fileName, "File name cannot be empty");
        }
        
        Attachment attachment = attachmentService.uploadAttachmentToTicket(fileName, fileUrl, ticketId, uploaderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
    }

    @DeleteMapping("/ticket/{ticketId}")
    public ResponseEntity<Void> deleteAttachmentsByTicket(@PathVariable Long ticketId) {
        attachmentService.deleteAttachmentsByTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}
