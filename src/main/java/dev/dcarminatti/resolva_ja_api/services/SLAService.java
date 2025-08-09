package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.SLA;
import dev.dcarminatti.resolva_ja_api.models.repositories.SLARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SLAService {
    @Autowired
    private SLARepository repository;

    public List<SLA> findAll() {
        return repository.findAll();
    }

    public Optional<SLA> findById(Long id) {
        return repository.findById(id);
    }

    public SLA save(SLA entity) throws ValidateException {
        this.validateSLA(entity);
        return repository.save(entity);
    }

    public SLA update(SLA sla) throws ValidateException {
        this.validateSLA(sla);
        Optional<SLA> existingSLA = repository.findById(sla.getId());
        if (existingSLA.isEmpty()) {
            throw new ValidateException("SLA with ID " + sla.getId() + " does not exist.");
        }
        return repository.save(sla);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateSLA(SLA sla) {
        if (sla.getDescription() == null || sla.getDescription().isEmpty()) {
            throw new IllegalArgumentException("SLA description cannot be empty");
        }
        if (sla.getResponseTimeHours() <= 0) {
            throw new IllegalArgumentException("Response time must be greater than zero");
        }
        if (sla.getResolutionTimeHours() <= 0) {
            throw new IllegalArgumentException("Resolution time must be greater than zero");
        }
    }
}

