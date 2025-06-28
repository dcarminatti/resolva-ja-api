package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Attachment;
import dev.dcarminatti.rja_api.model.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    // Basic CRUD operations
    public List<Attachment> findAll() {
        return attachmentRepository.findAll();
    }

    public Optional<Attachment> findById(Long id) {
        return attachmentRepository.findById(id);
    }

    public Attachment save(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    public Attachment update(Long id, Attachment updatedAttachment) {
        return attachmentRepository.findById(id)
                .map(attachment -> {
                    attachment.setFileName(updatedAttachment.getFileName());
                    attachment.setFileUrl(updatedAttachment.getFileUrl());
                    attachment.setUploader(updatedAttachment.getUploader());
                    attachment.setTicket(updatedAttachment.getTicket());
                    return attachmentRepository.save(attachment);
                })
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
    }

    public void deleteById(Long id) {
        attachmentRepository.deleteById(id);
    }

    // Business logic methods
    public List<Attachment> findByTicket(Long ticketId) {
        return attachmentRepository.findByTicketId(ticketId);
    }

    public List<Attachment> findByUploader(Long uploaderId) {
        return attachmentRepository.findByUploaderId(uploaderId);
    }

    public Optional<Attachment> findByFileName(String fileName) {
        return attachmentRepository.findByFileName(fileName);
    }

    public List<Attachment> findByFileNameContaining(String fileName) {
        return attachmentRepository.findByFileNameContaining(fileName);
    }

    public List<Attachment> findByFileUrlContaining(String url) {
        return attachmentRepository.findByFileUrlContaining(url);
    }

    public Long countByTicket(Long ticketId) {
        return attachmentRepository.countByTicketId(ticketId);
    }

    public Long countByUploader(Long uploaderId) {
        return attachmentRepository.countByUploaderId(uploaderId);
    }

    public boolean existsByFileName(String fileName) {
        return attachmentRepository.existsByFileName(fileName);
    }

    public Attachment uploadAttachmentToTicket(String fileName, String fileUrl, Long ticketId, Long uploaderId) {
        Attachment attachment = new Attachment();
        attachment.setFileName(fileName);
        attachment.setFileUrl(fileUrl);
        // Here you would typically fetch the User and Ticket entities
        // For now, we'll assume they are set elsewhere
        return attachmentRepository.save(attachment);
    }

    public void deleteAttachmentsByTicket(Long ticketId) {
        List<Attachment> attachments = attachmentRepository.findByTicketId(ticketId);
        attachmentRepository.deleteAll(attachments);
    }
}
