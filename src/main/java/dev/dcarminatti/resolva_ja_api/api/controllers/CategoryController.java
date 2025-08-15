package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Category;
import dev.dcarminatti.resolva_ja_api.api.dtos.CategoryDTO;
import dev.dcarminatti.resolva_ja_api.models.entities.SLA;
import dev.dcarminatti.resolva_ja_api.services.CategoryService;
import dev.dcarminatti.resolva_ja_api.services.SLAService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SLAService sLAService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(category.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody CategoryDTO dto) {
        try {
            Category category = toEntity(dto);
            Category saved = categoryService.save(category);
            return ResponseEntity.ok(toDTO(saved));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        try {
            Category category = toEntity(dto);
            category.setId(id);
            Category updated = categoryService.update(category);
            return ResponseEntity.ok(toDTO(updated));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription(), category.getSla() != null ? category.getSla().getId() : null);
    }

    private Category toEntity(CategoryDTO dto) {
        Optional<SLA> sla = sLAService.findById(dto.slaId());
        if (sla.isEmpty()) {
            throw new ValidateException("SLA not found with id: " + dto.slaId());
        }

        Category category = new Category();
        category.setId(dto.id());
        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setSla(sla.get());
        return category;
    }
}

