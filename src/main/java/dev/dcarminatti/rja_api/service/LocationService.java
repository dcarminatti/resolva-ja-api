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

    // Business logic methods
    public Optional<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    public List<Location> findByLocationType(LocationType locationType) {
        return locationRepository.findByLocationType(locationType);
    }

    public List<Location> findByParentLocation(Long parentLocationId) {
        return locationRepository.findByParentLocationId(parentLocationId);
    }

    public List<Location> findRootLocations() {
        return locationRepository.findRootLocations();
    }

    public List<Location> findByNameContaining(String name) {
        return locationRepository.findByNameContaining(name);
    }

    public List<Location> findByDescriptionContaining(String description) {
        return locationRepository.findByDescriptionContaining(description);
    }

    public boolean existsByName(String name) {
        return locationRepository.existsByName(name);
    }

    public List<Location> findChildLocations(Long parentId) {
        return locationRepository.findByParentLocationId(parentId);
    }

    public boolean hasChildLocations(Long locationId) {
        return !locationRepository.findByParentLocationId(locationId).isEmpty();
    }

    public Location setParentLocation(Long locationId, Long parentLocationId) {
        return locationRepository.findById(locationId)
                .map(location -> {
                    if (parentLocationId != null) {
                        Location parentLocation = locationRepository.findById(parentLocationId)
                                .orElseThrow(() -> new RuntimeException("Parent location not found with id: " + parentLocationId));
                        location.setParentLocation(parentLocation);
                    } else {
                        location.setParentLocation(null);
                    }
                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
    }
}
