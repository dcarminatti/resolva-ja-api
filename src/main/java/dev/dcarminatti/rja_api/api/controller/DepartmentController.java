package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.Department;
import dev.dcarminatti.rja_api.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return ResponseEntity.ok(department);
    }

    @PostMapping
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        if (departmentService.existsByName(department.getName())) {
            throw new ResourceAlreadyExistsException("Department", "name", department.getName());
        }
        
        Department savedDepartment = departmentService.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department department) {
        Department updatedDepartment = departmentService.update(id, department);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/name/{name}")
    public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
        Department department = departmentService.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "name", name));
        return ResponseEntity.ok(department);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<Department>> getDepartmentsByLocation(@PathVariable Long locationId) {
        List<Department> departments = departmentService.findByLocation(locationId);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Department>> getDepartmentsByManager(@PathVariable Long managerId) {
        List<Department> departments = departmentService.findByManager(managerId);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Department>> searchDepartmentsByName(@RequestParam String name) {
        List<Department> departments = departmentService.findByNameContaining(name);
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/search/location")
    public ResponseEntity<List<Department>> searchDepartmentsByLocationName(@RequestParam String locationName) {
        List<Department> departments = departmentService.findByLocationName(locationName);
        return ResponseEntity.ok(departments);
    }

    @PatchMapping("/{id}/manager/{managerId}")
    public ResponseEntity<Department> assignManager(@PathVariable Long id, @PathVariable Long managerId) {
        Department department = departmentService.assignManager(id, managerId);
        return ResponseEntity.ok(department);
    }
}
