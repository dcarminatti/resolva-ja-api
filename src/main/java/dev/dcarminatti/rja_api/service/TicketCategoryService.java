package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.TicketCategory;
import dev.dcarminatti.rja_api.model.repository.TicketCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketCategoryService {

    @Autowired
    private TicketCategoryRepository ticketCategoryRepository;

    // Basic CRUD operations
    public List<TicketCategory> findAll() {
        return ticketCategoryRepository.findAll();
    }

    public Optional<TicketCategory> findById(Long id) {
        return ticketCategoryRepository.findById(id);
    }

    public TicketCategory save(TicketCategory ticketCategory) {
        return ticketCategoryRepository.save(ticketCategory);
    }

    public TicketCategory update(Long id, TicketCategory updatedTicketCategory) {
        return ticketCategoryRepository.findById(id)
                .map(ticketCategory -> {
                    ticketCategory.setName(updatedTicketCategory.getName());
                    ticketCategory.setDescription(updatedTicketCategory.getDescription());
                    return ticketCategoryRepository.save(ticketCategory);
                })
                .orElseThrow(() -> new RuntimeException("TicketCategory not found with id: " + id));
    }

    public void deleteById(Long id) {
        ticketCategoryRepository.deleteById(id);
    }

    // Business logic methods
    public Optional<TicketCategory> findByName(String name) {
        return ticketCategoryRepository.findByName(name);
    }

    public List<TicketCategory> findByNameContaining(String name) {
        return ticketCategoryRepository.findByNameContaining(name);
    }

    public List<TicketCategory> findByDescriptionContaining(String description) {
        return ticketCategoryRepository.findByDescriptionContaining(description);
    }

    public boolean existsByName(String name) {
        return ticketCategoryRepository.existsByName(name);
    }
}
