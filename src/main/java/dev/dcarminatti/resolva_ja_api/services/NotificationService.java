package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Notification;
import dev.dcarminatti.resolva_ja_api.models.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    public List<Notification> findAll() {
        return repository.findAll();
    }

    public Optional<Notification> findById(Long id) {
        return repository.findById(id);
    }

    public Notification save(Notification entity) throws ValidateException {

        this.validateNotification(entity);
        return repository.save(entity);
    }

    public Notification update(Notification notification) {
        this.validateNotification(notification);
        Optional<Notification> optional = repository.findById(notification.getId());
        if (optional.isEmpty()) {
            throw new ValidateException("Notification not found");
        }
        return repository.save(notification);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateNotification(Notification notification) {
        if (notification.getUser() == null) {
            throw new ValidateException("User cannot be null");
        }
        if (notification.getTicket() == null) {
            throw new ValidateException("Ticket cannot be null");
        }
        if (notification.getMessage() == null || notification.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
    }
}

