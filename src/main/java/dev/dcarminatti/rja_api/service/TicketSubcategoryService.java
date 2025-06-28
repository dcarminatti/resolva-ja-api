package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.TicketSubcategory;
import dev.dcarminatti.rja_api.model.repository.TicketSubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketSubcategoryService {

    @Autowired
    private TicketSubcategoryRepository ticketSubcategoryRepository;

    // Basic CRUD operations
    public List<TicketSubcategory> findAll() {
        return ticketSubcategoryRepository.findAll();
    }

    public Optional<TicketSubcategory> findById(Long id) {
        return ticketSubcategoryRepository.findById(id);
    }

    public TicketSubcategory save(TicketSubcategory ticketSubcategory) {
        return ticketSubcategoryRepository.save(ticketSubcategory);
    }

    public TicketSubcategory update(Long id, TicketSubcategory updatedTicketSubcategory) {
        return ticketSubcategoryRepository.findById(id)
                .map(ticketSubcategory -> {
                    ticketSubcategory.setName(updatedTicketSubcategory.getName());
                    ticketSubcategory.setDescription(updatedTicketSubcategory.getDescription());
                    ticketSubcategory.setAssociatedServiceTypes(updatedTicketSubcategory.getAssociatedServiceTypes());
                    ticketSubcategory.setCategory(updatedTicketSubcategory.getCategory());
                    ticketSubcategory.setAssociatedSLA(updatedTicketSubcategory.getAssociatedSLA());
                    return ticketSubcategoryRepository.save(ticketSubcategory);
                })
                .orElseThrow(() -> new RuntimeException("TicketSubcategory not found with id: " + id));
    }

    public void deleteById(Long id) {
        ticketSubcategoryRepository.deleteById(id);
    }

    // Business logic methods
    public Optional<TicketSubcategory> findByName(String name) {
        return ticketSubcategoryRepository.findByName(name);
    }

    public List<TicketSubcategory> findByCategory(Long categoryId) {
        return ticketSubcategoryRepository.findByCategoryId(categoryId);
    }

    public List<TicketSubcategory> findBySLA(Long slaId) {
        return ticketSubcategoryRepository.findByAssociatedSLAId(slaId);
    }

    public List<TicketSubcategory> findByServiceType(Long serviceTypeId) {
        return ticketSubcategoryRepository.findByServiceTypeId(serviceTypeId);
    }

    public List<TicketSubcategory> findByNameContaining(String name) {
        return ticketSubcategoryRepository.findByNameContaining(name);
    }

    public List<TicketSubcategory> findByDescriptionContaining(String description) {
        return ticketSubcategoryRepository.findByDescriptionContaining(description);
    }

    public List<TicketSubcategory> findByCategoryName(String categoryName) {
        return ticketSubcategoryRepository.findByCategoryName(categoryName);
    }

    public boolean existsByName(String name) {
        return ticketSubcategoryRepository.existsByName(name);
    }
}
