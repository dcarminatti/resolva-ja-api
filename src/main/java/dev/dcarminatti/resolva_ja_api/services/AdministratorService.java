package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Administrator;
import dev.dcarminatti.resolva_ja_api.models.repositories.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {
    @Autowired
    private AdministratorRepository repository;

    public List<Administrator> findAll() {
        return repository.findAll();
    }

    public Optional<Administrator> findById(Long id) {
        return repository.findById(id);
    }

    public Administrator save(Administrator newAdmin) {
        try {
            LocalDateTime now = LocalDateTime.now();
            newAdmin.setId(null);
            newAdmin.setCreatedAt(now.toString());
            newAdmin.setUpdatedAt(now.toString());
            this.validateAdministrator(newAdmin);
            return repository.save(newAdmin);
        } catch (ValidateException ex) {
            throw new ValidateException("Error saving administrator: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while saving the administrator", ex);
        }
    }

    public void update(Long id, Administrator updatedAdmin) {
        try {
            LocalDateTime now = LocalDateTime.now();
            updatedAdmin.setId(id);
            updatedAdmin.setUpdatedAt(now.toString());
            this.validateAdministrator(updatedAdmin);
            repository.save(updatedAdmin);
        } catch (ValidateException ex) {
            throw new ValidateException("Error updating administrator: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while updating the administrator", ex);
        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateAdministrator(Administrator administrator) {
        if (administrator.getName() == null || administrator.getName().isEmpty()) {
            throw new ValidateException("Administrator name cannot be null or empty");
        }
        if (administrator.getEmail() == null || administrator.getEmail().isEmpty()) {
            throw new ValidateException("Administrator email cannot be null or empty");
        }
        if (administrator.getPassword() == null || administrator.getPassword().isEmpty()) {
            throw new ValidateException("Administrator password cannot be null or empty");
        }
        if (administrator.getPassword().length() < 6) {
            throw new ValidateException("Administrator password must be at least 6 characters long");
        }
        if (administrator.getCreatedAt() == null || administrator.getUpdatedAt() == null) {
            throw new ValidateException("Administrator createdAt and updatedAt cannot be null");
        }

        try {
            LocalDateTime createdAt = LocalDateTime.parse(administrator.getCreatedAt());
            LocalDateTime updatedAt = LocalDateTime.parse(administrator.getUpdatedAt());

            if (createdAt.isAfter(LocalDateTime.now()) || updatedAt.isAfter(LocalDateTime.now())) {
                throw new ValidateException("User createdAt and updatedAt cannot be in the future");
            }
            if (createdAt.isAfter(updatedAt)) {
                throw new ValidateException("User updatedAt cannot be before createdAt");
            }
        } catch (Exception e) {
            throw new ValidateException("Invalid date format for createdAt or updatedAt: " + e.getMessage());
        }
    }
}

