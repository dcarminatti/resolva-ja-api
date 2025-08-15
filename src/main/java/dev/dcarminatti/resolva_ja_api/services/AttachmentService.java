package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.api.dtos.AttachmentDTO;
import dev.dcarminatti.resolva_ja_api.models.entities.Attachment;
import dev.dcarminatti.resolva_ja_api.models.entities.Ticket;
import dev.dcarminatti.resolva_ja_api.models.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    private final Path fileStorageLocation = Paths.get("uploads/").toAbsolutePath().normalize();

//    public List<Attachment> download(Long id) {
//        Optional<Attachment> attachment = attachmentRepository.findById(id);
//        if (attachment.isEmpty()) return null;
//
//        try {
//            Path filePath = this.fileStorageLocation.resolve(attachment).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//        } catch (Exception e) {
//            return null;
//        }
//
//        return attachmentRepository.findAll();
//    }

    public Attachment upload(Ticket ticket, MultipartFile file) {
        Attachment attachment = new Attachment();

        try {
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            String uploadDir = "uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

            String filePath = uploadDir + uniqueFileName;
            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            attachment.setTicket(ticket);
            attachment.setFilePath(filePath);
            attachment.setFileType(fileType);
            attachment.setFileName(fileName);
        } catch (Exception e) {
            return null;
        }

        if (attachment.getFileName() == null || attachment.getFilePath() == null)
            return null;

        return attachmentRepository.save(attachment);
    }

    public Attachment findById(Long id) {
        return attachmentRepository.findById(id).orElse(null);
    }
}
