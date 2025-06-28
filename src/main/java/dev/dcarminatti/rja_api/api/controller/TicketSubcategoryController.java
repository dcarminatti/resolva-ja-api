package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.TicketSubcategory;
import dev.dcarminatti.rja_api.service.TicketSubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ticket-subcategories")
@CrossOrigin(origins = "*")
public class TicketSubcategoryController {

    @Autowired
    private TicketSubcategoryService ticketSubcategoryService;

    @GetMapping
    public ResponseEntity<List<TicketSubcategory>> getAllTicketSubcategories() {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findAll();
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketSubcategory> getTicketSubcategoryById(@PathVariable Long id) {
        TicketSubcategory subcategory = ticketSubcategoryService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TicketSubcategory", "id", id));
        return ResponseEntity.ok(subcategory);
    }

    @PostMapping
    public ResponseEntity<TicketSubcategory> createTicketSubcategory(@Valid @RequestBody TicketSubcategory subcategory) {
        if (ticketSubcategoryService.existsByName(subcategory.getName())) {
            throw new ResourceAlreadyExistsException("TicketSubcategory", "name", subcategory.getName());
        }
        
        TicketSubcategory savedSubcategory = ticketSubcategoryService.save(subcategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubcategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketSubcategory> updateTicketSubcategory(@PathVariable Long id, @Valid @RequestBody TicketSubcategory subcategory) {
        TicketSubcategory updatedSubcategory = ticketSubcategoryService.update(id, subcategory);
        return ResponseEntity.ok(updatedSubcategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketSubcategory(@PathVariable Long id) {
        if (!ticketSubcategoryService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("TicketSubcategory", "id", id);
        }
        ticketSubcategoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/name/{name}")
    public ResponseEntity<TicketSubcategory> getTicketSubcategoryByName(@PathVariable String name) {
        TicketSubcategory subcategory = ticketSubcategoryService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("TicketSubcategory", "name", name));
        return ResponseEntity.ok(subcategory);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TicketSubcategory>> getTicketSubcategoriesByCategory(@PathVariable Long categoryId) {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findByCategory(categoryId);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/sla/{slaId}")
    public ResponseEntity<List<TicketSubcategory>> getTicketSubcategoriesBySLA(@PathVariable Long slaId) {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findBySLA(slaId);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/service-type/{serviceTypeId}")
    public ResponseEntity<List<TicketSubcategory>> getTicketSubcategoriesByServiceType(@PathVariable Long serviceTypeId) {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findByServiceType(serviceTypeId);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<TicketSubcategory>> searchTicketSubcategoriesByName(@RequestParam String name) {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findByNameContaining(name);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<TicketSubcategory>> searchTicketSubcategoriesByDescription(@RequestParam String description) {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findByDescriptionContaining(description);
        return ResponseEntity.ok(subcategories);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<TicketSubcategory>> searchTicketSubcategoriesByCategoryName(@RequestParam String categoryName) {
        List<TicketSubcategory> subcategories = ticketSubcategoryService.findByCategoryName(categoryName);
        return ResponseEntity.ok(subcategories);
    }
}
