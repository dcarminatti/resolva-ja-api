package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.SLA;
import dev.dcarminatti.rja_api.service.SLAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/slas")
@CrossOrigin(origins = "*")
public class SLAController {

    @Autowired
    private SLAService slaService;

    @GetMapping
    public ResponseEntity<List<SLA>> getAllSLAs() {
        List<SLA> slas = slaService.findAll();
        return ResponseEntity.ok(slas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SLA> getSLAById(@PathVariable Long id) {
        SLA sla = slaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SLA", "id", id));
        return ResponseEntity.ok(sla);
    }

    @PostMapping
    public ResponseEntity<SLA> createSLA(@Valid @RequestBody SLA sla) {
        if (slaService.existsByName(sla.getName())) {
            throw new ResourceAlreadyExistsException("SLA", "name", sla.getName());
        }
        
        SLA savedSLA = slaService.save(sla);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSLA);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SLA> updateSLA(@PathVariable Long id, @Valid @RequestBody SLA sla) {
        SLA updatedSLA = slaService.update(id, sla);
        return ResponseEntity.ok(updatedSLA);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSLA(@PathVariable Long id) {
        if (!slaService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("SLA", "id", id);
        }
        slaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
