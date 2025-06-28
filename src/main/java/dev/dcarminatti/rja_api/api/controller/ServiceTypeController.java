package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.ServiceType;
import dev.dcarminatti.rja_api.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/service-types")
@CrossOrigin(origins = "*")
public class ServiceTypeController {

    @Autowired
    private ServiceTypeService serviceTypeService;

    @GetMapping
    public ResponseEntity<List<ServiceType>> getAllServiceTypes() {
        List<ServiceType> serviceTypes = serviceTypeService.findAll();
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceType> getServiceTypeById(@PathVariable Long id) {
        ServiceType serviceType = serviceTypeService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceType", "id", id));
        return ResponseEntity.ok(serviceType);
    }

    @PostMapping
    public ResponseEntity<ServiceType> createServiceType(@Valid @RequestBody ServiceType serviceType) {
        if (serviceTypeService.existsByName(serviceType.getName())) {
            throw new ResourceAlreadyExistsException("ServiceType", "name", serviceType.getName());
        }
        
        ServiceType savedServiceType = serviceTypeService.save(serviceType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedServiceType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceType> updateServiceType(@PathVariable Long id, @Valid @RequestBody ServiceType serviceType) {
        ServiceType updatedServiceType = serviceTypeService.update(id, serviceType);
        return ResponseEntity.ok(updatedServiceType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        if (!serviceTypeService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("ServiceType", "id", id);
        }
        serviceTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/name/{name}")
    public ResponseEntity<ServiceType> getServiceTypeByName(@PathVariable String name) {
        ServiceType serviceType = serviceTypeService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceType", "name", name));
        return ResponseEntity.ok(serviceType);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ServiceType>> searchServiceTypesByName(@RequestParam String name) {
        List<ServiceType> serviceTypes = serviceTypeService.findByNameContaining(name);
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<ServiceType>> searchServiceTypesByDescription(@RequestParam String description) {
        List<ServiceType> serviceTypes = serviceTypeService.findByDescriptionContaining(description);
        return ResponseEntity.ok(serviceTypes);
    }
}
