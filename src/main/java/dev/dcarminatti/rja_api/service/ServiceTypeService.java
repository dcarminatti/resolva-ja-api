package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.ServiceType;
import dev.dcarminatti.rja_api.model.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceTypeService {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    // Basic CRUD operations
    public List<ServiceType> findAll() {
        return serviceTypeRepository.findAll();
    }

    public Optional<ServiceType> findById(Long id) {
        return serviceTypeRepository.findById(id);
    }

    public ServiceType save(ServiceType serviceType) {
        return serviceTypeRepository.save(serviceType);
    }

    public ServiceType update(Long id, ServiceType updatedServiceType) {
        return serviceTypeRepository.findById(id)
                .map(serviceType -> {
                    serviceType.setName(updatedServiceType.getName());
                    serviceType.setDescription(updatedServiceType.getDescription());
                    return serviceTypeRepository.save(serviceType);
                })
                .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + id));
    }

    public void deleteById(Long id) {
        serviceTypeRepository.deleteById(id);
    }
}
