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
}
