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
}
