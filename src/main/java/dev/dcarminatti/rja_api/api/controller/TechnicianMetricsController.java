package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.model.entity.TechnicianMetrics;
import dev.dcarminatti.rja_api.service.TechnicianMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/technician-metrics")
@CrossOrigin(origins = "*")
public class TechnicianMetricsController {

    @Autowired
    private TechnicianMetricsService technicianMetricsService;

    @GetMapping
    public ResponseEntity<List<TechnicianMetrics>> getAllTechnicianMetrics() {
        List<TechnicianMetrics> metrics = technicianMetricsService.findAll();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianMetrics> getTechnicianMetricsById(@PathVariable Long id) {
        TechnicianMetrics metrics = technicianMetricsService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TechnicianMetrics", "id", id));
        return ResponseEntity.ok(metrics);
    }

    @PostMapping
    public ResponseEntity<TechnicianMetrics> createTechnicianMetrics(@Valid @RequestBody TechnicianMetrics metrics) {
        TechnicianMetrics savedMetrics = technicianMetricsService.save(metrics);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMetrics);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicianMetrics> updateTechnicianMetrics(@PathVariable Long id, @Valid @RequestBody TechnicianMetrics metrics) {
        TechnicianMetrics updatedMetrics = technicianMetricsService.update(id, metrics);
        return ResponseEntity.ok(updatedMetrics);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnicianMetrics(@PathVariable Long id) {
        if (!technicianMetricsService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("TechnicianMetrics", "id", id);
        }
        technicianMetricsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
