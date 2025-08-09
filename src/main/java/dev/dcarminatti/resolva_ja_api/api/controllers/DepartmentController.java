package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Department;
import dev.dcarminatti.resolva_ja_api.api.dtos.DepartmentDTO;
import dev.dcarminatti.resolva_ja_api.services.DepartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public List<DepartmentDTO> getAll() {
        return departmentService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Long id) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(department.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody DepartmentDTO dto) {
        try {
            Department department = toEntity(dto);
            Department saved = departmentService.save(department);
            return ResponseEntity.ok(toDTO(saved));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
        try {
            Department department = toEntity(dto);
            department.setId(id);
            Department updated = departmentService.update(department);
            return ResponseEntity.ok(toDTO(updated));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private DepartmentDTO toDTO(Department department) {
        return new DepartmentDTO(department.getId(), department.getName(), department.getLocation());
    }

    private Department toEntity(DepartmentDTO dto) {
        Department department = new Department();
        department.setId(dto.id());
        department.setName(dto.name());
        department.setLocation(dto.location());
        return department;
    }
}

