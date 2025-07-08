package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Location;
import dev.dcarminatti.rja_api.model.enums.LocationType;
import dev.dcarminatti.rja_api.model.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Basic CRUD operations
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }

    public Location update(Long id, Location updatedLocation) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.setName(updatedLocation.getName());
                    location.setLocationType(updatedLocation.getLocationType());
                    location.setDescription(updatedLocation.getDescription());
                    location.setParentLocation(updatedLocation.getParentLocation());
                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }
}
