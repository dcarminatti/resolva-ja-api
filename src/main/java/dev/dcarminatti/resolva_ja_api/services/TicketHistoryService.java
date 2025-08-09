package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.TicketHistory;
import dev.dcarminatti.resolva_ja_api.models.repositories.TicketHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketHistoryService {
    @Autowired
    private TicketHistoryRepository repository;

    public List<TicketHistory> findAll() {
        return repository.findAll();
    }

    public Optional<TicketHistory> findById(Long id) {
        return repository.findById(id);
    }

    public TicketHistory save(TicketHistory entity) throws ValidateException {
        entity.setId(null);
        entity.setDate(LocalDateTime.now().toString());
        return repository.save(entity);
    }

    public TicketHistory update(Long id, TicketHistory entity) throws ValidateException {
        entity.setId(id);
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<TicketHistory> findByTicketId(Long id) {
        return repository.findByTicketId(id);
    }

    private void validateTicketHistory(TicketHistory entity) {
        if (entity.getTicket() == null || entity.getTicket().getId() == null) {
            throw new ValidateException("Ticket must be provided for TicketHistory.");
        }
        if (entity.getDate() == null || entity.getDate().isEmpty()) {
            throw new ValidateException("Date must be provided for TicketHistory.");
        }
        if (entity.getPreviousStatus() == null || entity.getPreviousStatus().isEmpty()) {
            throw new ValidateException("Previous status must be provided for TicketHistory.");
        }
        if (entity.getCurrentStatus() == null || entity.getCurrentStatus().isEmpty()) {
            throw new ValidateException("Current status must be provided for TicketHistory.");
        }
    }
}

