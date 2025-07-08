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
        if (attachmentService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Attachment", "id", id);
        }
        attachmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
