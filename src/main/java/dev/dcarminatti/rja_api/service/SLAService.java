package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.SLA;
import dev.dcarminatti.rja_api.model.enums.Priority;
import dev.dcarminatti.rja_api.model.repository.SLARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SLAService {

    @Autowired
    private SLARepository slaRepository;

    // Basic CRUD operations
    public List<SLA> findAll() {
        return slaRepository.findAll();
    }

    public Optional<SLA> findById(Long id) {
        return slaRepository.findById(id);
    }

    public SLA save(SLA sla) {
        return slaRepository.save(sla);
    }

    public SLA update(Long id, SLA updatedSLA) {
        return slaRepository.findById(id)
                .map(sla -> {
                    sla.setName(updatedSLA.getName());
                    sla.setDescription(updatedSLA.getDescription());
                    sla.setExpectedResponseTimeHours(updatedSLA.getExpectedResponseTimeHours());
                    sla.setPriority(updatedSLA.getPriority());
                    return slaRepository.save(sla);
                })
                .orElseThrow(() -> new RuntimeException("SLA not found with id: " + id));
    }

    public void deleteById(Long id) {
        slaRepository.deleteById(id);
    }

    // Business logic methods
    public Optional<SLA> findByName(String name) {
        return slaRepository.findByName(name);
    }

    public List<SLA> findByPriority(Priority priority) {
        return slaRepository.findByPriority(priority);
    }

    public List<SLA> findByMaxResponseTime(Integer maxHours) {
        return slaRepository.findByExpectedResponseTimeHoursLessThanEqual(maxHours);
    }

    public List<SLA> findByMinResponseTime(Integer minHours) {
        return slaRepository.findByExpectedResponseTimeHoursGreaterThanEqual(minHours);
    }

    public List<SLA> findByNameContaining(String name) {
        return slaRepository.findByNameContaining(name);
    }

    public List<SLA> findByDescriptionContaining(String description) {
        return slaRepository.findByDescriptionContaining(description);
    }

    public List<SLA> findAllOrderByResponseTime() {
        return slaRepository.findAllOrderByResponseTimeAsc();
    }

    public boolean existsByName(String name) {
        return slaRepository.existsByName(name);
    }

    public List<SLA> findHighPrioritySLAs() {
        return slaRepository.findByPriority(Priority.HIGH);
    }

    public List<SLA> findCriticalSLAs() {
        return slaRepository.findByPriority(Priority.CRITICAL);
    }
}
