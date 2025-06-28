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

    // Business logic endpoints
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<TechnicianMetrics> getTechnicianMetricsByTechnician(@PathVariable Long technicianId) {
        TechnicianMetrics metrics = technicianMetricsService.findByTechnician(technicianId)
                .orElseThrow(() -> new ResourceNotFoundException("TechnicianMetrics", "technicianId", technicianId));
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/min-requests-handled/{minRequests}")
    public ResponseEntity<List<TechnicianMetrics>> getTechnicianMetricsByMinRequestsHandled(@PathVariable int minRequests) {
        List<TechnicianMetrics> metrics = technicianMetricsService.findByMinRequestsHandled(minRequests);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/max-response-time/{maxResponseTime}")
    public ResponseEntity<List<TechnicianMetrics>> getTechnicianMetricsByMaxResponseTime(@PathVariable int maxResponseTime) {
        List<TechnicianMetrics> metrics = technicianMetricsService.findByMaxResponseTime(maxResponseTime);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/max-resolution-time/{maxResolutionTime}")
    public ResponseEntity<List<TechnicianMetrics>> getTechnicianMetricsByMaxResolutionTime(@PathVariable int maxResolutionTime) {
        List<TechnicianMetrics> metrics = technicianMetricsService.findByMaxResolutionTime(maxResolutionTime);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/ordered/resolved-requests")
    public ResponseEntity<List<TechnicianMetrics>> getAllTechnicianMetricsOrderedByResolvedRequests() {
        List<TechnicianMetrics> metrics = technicianMetricsService.findAllOrderByResolvedRequests();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/ordered/response-time")
    public ResponseEntity<List<TechnicianMetrics>> getAllTechnicianMetricsOrderedByResponseTime() {
        List<TechnicianMetrics> metrics = technicianMetricsService.findAllOrderByResponseTime();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/ordered/resolution-time")
    public ResponseEntity<List<TechnicianMetrics>> getAllTechnicianMetricsOrderedByResolutionTime() {
        List<TechnicianMetrics> metrics = technicianMetricsService.findAllOrderByResolutionTime();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/with-pending-requests")
    public ResponseEntity<List<TechnicianMetrics>> getTechniciansWithPendingRequests() {
        List<TechnicianMetrics> metrics = technicianMetricsService.findTechniciansWithPendingRequests();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/overall-average-response-time")
    public ResponseEntity<Double> getOverallAverageResponseTime() {
        Double averageResponseTime = technicianMetricsService.getOverallAverageResponseTime();
        return ResponseEntity.ok(averageResponseTime != null ? averageResponseTime : 0.0);
    }

    @GetMapping("/overall-average-resolution-time")
    public ResponseEntity<Double> getOverallAverageResolutionTime() {
        Double averageResolutionTime = technicianMetricsService.getOverallAverageResolutionTime();
        return ResponseEntity.ok(averageResolutionTime != null ? averageResolutionTime : 0.0);
    }

    @GetMapping("/total-requests-handled")
    public ResponseEntity<Long> getTotalRequestsHandledByAll() {
        Long totalRequests = technicianMetricsService.getTotalRequestsHandledByAll();
        return ResponseEntity.ok(totalRequests != null ? totalRequests : 0L);
    }

    @GetMapping("/exists/technician/{technicianId}")
    public ResponseEntity<Boolean> checkIfMetricsExistByTechnician(@PathVariable Long technicianId) {
        boolean exists = technicianMetricsService.existsByTechnician(technicianId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/technician/{technicianId}")
    public ResponseEntity<TechnicianMetrics> createOrUpdateMetricsForTechnician(@PathVariable Long technicianId) {
        TechnicianMetrics metrics = technicianMetricsService.createOrUpdateMetrics(technicianId);
        return ResponseEntity.ok(metrics);
    }

    @PatchMapping("/technician/{technicianId}/increment-requests")
    public ResponseEntity<TechnicianMetrics> incrementRequestsHandled(@PathVariable Long technicianId) {
        TechnicianMetrics metrics = technicianMetricsService.incrementRequestsHandled(technicianId);
        return ResponseEntity.ok(metrics);
    }

    @PatchMapping("/technician/{technicianId}/increment-resolved")
    public ResponseEntity<TechnicianMetrics> incrementResolvedRequests(@PathVariable Long technicianId) {
        TechnicianMetrics metrics = technicianMetricsService.incrementResolvedRequests(technicianId);
        return ResponseEntity.ok(metrics);
    }
}
