package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.TechnicianMetrics;
import dev.dcarminatti.rja_api.model.repository.TechnicianMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TechnicianMetricsService {

    @Autowired
    private TechnicianMetricsRepository technicianMetricsRepository;

    // Basic CRUD operations
    public List<TechnicianMetrics> findAll() {
        return technicianMetricsRepository.findAll();
    }

    public Optional<TechnicianMetrics> findById(Long id) {
        return technicianMetricsRepository.findById(id);
    }

    public TechnicianMetrics save(TechnicianMetrics technicianMetrics) {
        return technicianMetricsRepository.save(technicianMetrics);
    }

    public TechnicianMetrics update(Long id, TechnicianMetrics updatedMetrics) {
        return technicianMetricsRepository.findById(id)
                .map(metrics -> {
                    metrics.setTotalRequestsHandled(updatedMetrics.getTotalRequestsHandled());
                    metrics.setAverageResponseTime(updatedMetrics.getAverageResponseTime());
                    metrics.setAverageResolutionTime(updatedMetrics.getAverageResolutionTime());
                    metrics.setTotalResolvedRequests(updatedMetrics.getTotalResolvedRequests());
                    metrics.setTotalPendingRequests(updatedMetrics.getTotalPendingRequests());
                    metrics.setTotalEscalatedRequests(updatedMetrics.getTotalEscalatedRequests());
                    metrics.setTechnician(updatedMetrics.getTechnician());
                    return technicianMetricsRepository.save(metrics);
                })
                .orElseThrow(() -> new RuntimeException("TechnicianMetrics not found with id: " + id));
    }

    public void deleteById(Long id) {
        technicianMetricsRepository.deleteById(id);
    }

    // Business logic methods
    public Optional<TechnicianMetrics> findByTechnician(Long technicianId) {
        return technicianMetricsRepository.findByTechnicianId(technicianId);
    }

    public List<TechnicianMetrics> findByMinRequestsHandled(int minRequests) {
        return technicianMetricsRepository.findByTotalRequestsHandledGreaterThanEqual(minRequests);
    }

    public List<TechnicianMetrics> findByMaxResponseTime(int maxResponseTime) {
        return technicianMetricsRepository.findByAverageResponseTimeLessThanEqual(maxResponseTime);
    }

    public List<TechnicianMetrics> findByMaxResolutionTime(int maxResolutionTime) {
        return technicianMetricsRepository.findByAverageResolutionTimeLessThanEqual(maxResolutionTime);
    }

    public List<TechnicianMetrics> findAllOrderByResolvedRequests() {
        return technicianMetricsRepository.findAllOrderByResolvedRequestsDesc();
    }

    public List<TechnicianMetrics> findAllOrderByResponseTime() {
        return technicianMetricsRepository.findAllOrderByResponseTimeAsc();
    }

    public List<TechnicianMetrics> findAllOrderByResolutionTime() {
        return technicianMetricsRepository.findAllOrderByResolutionTimeAsc();
    }

    public List<TechnicianMetrics> findTechniciansWithPendingRequests() {
        return technicianMetricsRepository.findTechniciansWithPendingRequests();
    }

    public Double getOverallAverageResponseTime() {
        return technicianMetricsRepository.getOverallAverageResponseTime();
    }

    public Double getOverallAverageResolutionTime() {
        return technicianMetricsRepository.getOverallAverageResolutionTime();
    }

    public Long getTotalRequestsHandledByAll() {
        return technicianMetricsRepository.getTotalRequestsHandledByAllTechnicians();
    }

    public boolean existsByTechnician(Long technicianId) {
        return technicianMetricsRepository.existsByTechnicianId(technicianId);
    }

    public TechnicianMetrics createOrUpdateMetrics(Long technicianId) {
        Optional<TechnicianMetrics> existingMetrics = technicianMetricsRepository.findByTechnicianId(technicianId);
        
        if (existingMetrics.isPresent()) {
            return existingMetrics.get();
        } else {
            TechnicianMetrics newMetrics = new TechnicianMetrics();
            // Here you would typically fetch the User entity and set initial values
            // For now, we'll assume they are set elsewhere
            return technicianMetricsRepository.save(newMetrics);
        }
    }

    public TechnicianMetrics incrementRequestsHandled(Long technicianId) {
        return technicianMetricsRepository.findByTechnicianId(technicianId)
                .map(metrics -> {
                    metrics.setTotalRequestsHandled(metrics.getTotalRequestsHandled() + 1);
                    return technicianMetricsRepository.save(metrics);
                })
                .orElseThrow(() -> new RuntimeException("TechnicianMetrics not found for technician: " + technicianId));
    }

    public TechnicianMetrics incrementResolvedRequests(Long technicianId) {
        return technicianMetricsRepository.findByTechnicianId(technicianId)
                .map(metrics -> {
                    metrics.setTotalResolvedRequests(metrics.getTotalResolvedRequests() + 1);
                    if (metrics.getTotalPendingRequests() > 0) {
                        metrics.setTotalPendingRequests(metrics.getTotalPendingRequests() - 1);
                    }
                    return technicianMetricsRepository.save(metrics);
                })
                .orElseThrow(() -> new RuntimeException("TechnicianMetrics not found for technician: " + technicianId));
    }
}
