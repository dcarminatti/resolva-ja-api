package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Category;
import dev.dcarminatti.resolva_ja_api.models.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    public Category save(Category entity) throws ValidateException {
        this.validateCategory(entity);
        return repository.save(entity);
    }

    public Category update(Category category) throws ValidateException {
        this.validateCategory(category);
        Optional<Category> optional = repository.findById(category.getId());
        if (optional.isEmpty()) {
            throw new ValidateException("Category not found with id: " + category.getId());
        }
        return repository.save(category);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private void validateCategory(Category category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new ValidateException("Category name cannot be empty");
        }
    }
}

