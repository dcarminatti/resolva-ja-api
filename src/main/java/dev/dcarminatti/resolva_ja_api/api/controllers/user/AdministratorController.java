package dev.dcarminatti.resolva_ja_api.api.controllers.user;

import dev.dcarminatti.resolva_ja_api.api.dtos.user.AdministratorDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Administrator;
import dev.dcarminatti.resolva_ja_api.services.AdministratorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/user/administrator")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAll() {
        return ResponseEntity.ok(administratorService.findAll().stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorDTO> getById(@PathVariable Long id) {
        Optional<Administrator> entity = administratorService.findById(id);
        if (entity.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(entity.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody AdministratorDTO newAdmin) {
        try {
            Administrator adminEntity = toEntity(newAdmin);
            Administrator savedAdmin = administratorService.save(adminEntity);
            return ResponseEntity.ok(toDTO(savedAdmin));
        } catch (ValidateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody AdministratorDTO updatedAdmin) {
        try {
            Optional<Administrator> existingUser = administratorService.findById(id);
            if(existingUser.isEmpty()) return ResponseEntity.notFound().build();
            Administrator adminEntity = toEntity(updatedAdmin);
            administratorService.update(id, adminEntity);
            return ResponseEntity.noContent().build();
        } catch (ValidateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Administrator> existingAdmin = administratorService.findById(id);
        if(existingAdmin.isEmpty()) return ResponseEntity.notFound().build();
        administratorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Administrator toEntity(AdministratorDTO dto) {
        Administrator adminEntity = new Administrator();
        adminEntity.setId(dto.id());
        adminEntity.setName(dto.name());
        adminEntity.setEmail(dto.email());
        adminEntity.setPassword(dto.password());
        adminEntity.setCreatedAt(dto.createdAt());
        adminEntity.setUpdatedAt(dto.updatedAt());
        return adminEntity;
    }

    private AdministratorDTO toDTO(Administrator entity) {
        return new AdministratorDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
