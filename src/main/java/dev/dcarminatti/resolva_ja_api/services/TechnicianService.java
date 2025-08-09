package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Technician;
import dev.dcarminatti.resolva_ja_api.models.repositories.TechnicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TechnicianService {
    @Autowired
    private TechnicianRepository repository;

    public List<Technician> findAll() {
        return repository.findAll();
    }

    public Optional<Technician> findById(Long id) {
        return repository.findById(id);
    }

    public Technician save(Technician entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setId(null);
        entity.setCreatedAt(now.toString());
        entity.setUpdatedAt(now.toString());
        this.validateTechnician(entity);
        return repository.save(entity);
    }

    public void update(Long id, Technician technicianEntity) {
        LocalDateTime now = LocalDateTime.now();
        technicianEntity.setId(id);
        technicianEntity.setUpdatedAt(now.toString());
        this.validateTechnician(technicianEntity);
        repository.save(technicianEntity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateTechnician(Technician technicianEntity) {
        if (technicianEntity.getName() == null || technicianEntity.getName().isEmpty()) {
            throw new ValidateException("Technician name cannot be null or empty");
        }
        if (technicianEntity.getEmail() == null || technicianEntity.getEmail().isEmpty()) {
            throw new ValidateException("Technician email cannot be null or empty");
        }
        if (technicianEntity.getPassword() == null || technicianEntity.getPassword().isEmpty()) {
            throw new ValidateException("Technician password cannot be null or empty");
        }
        if (technicianEntity.getPassword().length() < 6) {
            throw new ValidateException("Technician password must be at least 6 characters long");
        }
        if (technicianEntity.getCreatedAt() == null || technicianEntity.getUpdatedAt() == null) {
            throw new ValidateException("Technician createdAt and updatedAt cannot be null");
        }

        try {
            LocalDateTime createdAt = LocalDateTime.parse(technicianEntity.getCreatedAt());
            LocalDateTime updatedAt = LocalDateTime.parse(technicianEntity.getUpdatedAt());

            if (updatedAt.isBefore(createdAt)) {
                throw new ValidateException("Technician updatedAt cannot be before createdAt");
            }
        } catch (Exception e) {
            throw new ValidateException("Invalid date format for createdAt or updatedAt: " + e.getMessage());
        }
    }
}

