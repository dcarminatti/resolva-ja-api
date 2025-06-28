package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.TicketCategory;
import dev.dcarminatti.rja_api.service.TicketCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ticket-categories")
@CrossOrigin(origins = "*")
public class TicketCategoryController {

    @Autowired
    private TicketCategoryService ticketCategoryService;

    @GetMapping
    public ResponseEntity<List<TicketCategory>> getAllTicketCategories() {
        List<TicketCategory> categories = ticketCategoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketCategory> getTicketCategoryById(@PathVariable Long id) {
        TicketCategory category = ticketCategoryService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TicketCategory", "id", id));
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<TicketCategory> createTicketCategory(@Valid @RequestBody TicketCategory category) {
        if (ticketCategoryService.existsByName(category.getName())) {
            throw new ResourceAlreadyExistsException("TicketCategory", "name", category.getName());
        }
        
        TicketCategory savedCategory = ticketCategoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketCategory> updateTicketCategory(@PathVariable Long id, @Valid @RequestBody TicketCategory category) {
        TicketCategory updatedCategory = ticketCategoryService.update(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketCategory(@PathVariable Long id) {
        if (!ticketCategoryService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("TicketCategory", "id", id);
        }
        ticketCategoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/name/{name}")
    public ResponseEntity<TicketCategory> getTicketCategoryByName(@PathVariable String name) {
        TicketCategory category = ticketCategoryService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("TicketCategory", "name", name));
        return ResponseEntity.ok(category);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<TicketCategory>> searchTicketCategoriesByName(@RequestParam String name) {
        List<TicketCategory> categories = ticketCategoryService.findByNameContaining(name);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<TicketCategory>> searchTicketCategoriesByDescription(@RequestParam String description) {
        List<TicketCategory> categories = ticketCategoryService.findByDescriptionContaining(description);
        return ResponseEntity.ok(categories);
    }
}
