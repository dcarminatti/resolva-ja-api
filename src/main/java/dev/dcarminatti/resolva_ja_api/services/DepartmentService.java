package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Department;
import dev.dcarminatti.resolva_ja_api.models.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository repository;

    public List<Department> findAll() {
        return repository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return repository.findById(id);
    }

    public Department save(Department entity) throws ValidateException {
        this.validateDepartment(entity);
        return repository.save(entity);
    }

    public Department update(Department department) throws ValidateException {
        this.validateDepartment(department);
        Optional<Department> existing = repository.findById(department.getId());
        if (existing.isEmpty()) {
            throw new ValidateException("Department with ID " + department.getId() + " does not exist");
        }
        return repository.save(department);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateDepartment(Department department) {
        if (department.getName() == null || department.getName().isEmpty()) {
            throw new IllegalArgumentException("Department name cannot be empty");
        }
        if (department.getLocation() == null || department.getLocation().isEmpty()) {
            throw new IllegalArgumentException("Department description cannot be empty");
        }
    }
}

