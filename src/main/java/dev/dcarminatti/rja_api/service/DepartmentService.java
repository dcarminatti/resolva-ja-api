package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Department;
import dev.dcarminatti.rja_api.model.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Basic CRUD operations
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public Department update(Long id, Department updatedDepartment) {
        return departmentRepository.findById(id)
                .map(department -> {
                    department.setName(updatedDepartment.getName());
                    department.setLocation(updatedDepartment.getLocation());
                    department.setManager(updatedDepartment.getManager());
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

    // Business logic methods
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    public List<Department> findByLocation(Long locationId) {
        return departmentRepository.findByLocationId(locationId);
    }

    public List<Department> findByManager(Long managerId) {
        return departmentRepository.findByManagerId(managerId);
    }

    public List<Department> findByNameContaining(String name) {
        return departmentRepository.findByNameContaining(name);
    }

    public List<Department> findByLocationName(String locationName) {
        return departmentRepository.findByLocationName(locationName);
    }

    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }

    public boolean existsByManager(Long managerId) {
        return departmentRepository.existsByManagerId(managerId);
    }

    public Department assignManager(Long departmentId, Long managerId) {
        return departmentRepository.findById(departmentId)
                .map(department -> {
                    // Here you would typically fetch the User entity
                    // For now, we'll assume the User object is created elsewhere
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
    }
}
