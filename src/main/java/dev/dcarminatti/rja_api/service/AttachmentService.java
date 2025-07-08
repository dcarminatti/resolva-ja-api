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
}
