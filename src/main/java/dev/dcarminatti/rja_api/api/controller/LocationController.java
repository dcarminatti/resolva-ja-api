package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.Location;
import dev.dcarminatti.rja_api.model.enums.LocationType;
import dev.dcarminatti.rja_api.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.findAll();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id) {
        Location location = locationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));
        return ResponseEntity.ok(location);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@Valid @RequestBody Location location) {
        if (locationService.existsByName(location.getName())) {
            throw new ResourceAlreadyExistsException("Location", "name", location.getName());
        }
        
        Location savedLocation = locationService.save(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @Valid @RequestBody Location location) {
        Location updatedLocation = locationService.update(id, location);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        if (!locationService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Location", "id", id);
        }
        locationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/name/{name}")
    public ResponseEntity<Location> getLocationByName(@PathVariable String name) {
        Location location = locationService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Location", "name", name));
        return ResponseEntity.ok(location);
    }

    @GetMapping("/type/{locationType}")
    public ResponseEntity<List<Location>> getLocationsByType(@PathVariable LocationType locationType) {
        List<Location> locations = locationService.findByLocationType(locationType);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Location>> getLocationsByParent(@PathVariable Long parentId) {
        List<Location> locations = locationService.findByParentLocation(parentId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/root")
    public ResponseEntity<List<Location>> getRootLocations() {
        List<Location> locations = locationService.findRootLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Location>> searchLocationsByName(@RequestParam String name) {
        List<Location> locations = locationService.findByNameContaining(name);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<Location>> searchLocationsByDescription(@RequestParam String description) {
        List<Location> locations = locationService.findByDescriptionContaining(description);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<Location>> getChildLocations(@PathVariable Long id) {
        List<Location> children = locationService.findChildLocations(id);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/{id}/has-children")
    public ResponseEntity<Boolean> hasChildLocations(@PathVariable Long id) {
        boolean hasChildren = locationService.hasChildLocations(id);
        return ResponseEntity.ok(hasChildren);
    }

    @PatchMapping("/{id}/parent/{parentId}")
    public ResponseEntity<Location> setParentLocation(@PathVariable Long id, @PathVariable Long parentId) {
        Location location = locationService.setParentLocation(id, parentId);
        return ResponseEntity.ok(location);
    }

    @PatchMapping("/{id}/remove-parent")
    public ResponseEntity<Location> removeParentLocation(@PathVariable Long id) {
        Location location = locationService.setParentLocation(id, null);
        return ResponseEntity.ok(location);
    }
}
