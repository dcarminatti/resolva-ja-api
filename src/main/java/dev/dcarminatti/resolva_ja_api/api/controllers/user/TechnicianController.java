package dev.dcarminatti.resolva_ja_api.api.controllers.user;

import dev.dcarminatti.resolva_ja_api.api.dtos.user.TechnicianDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Technician;
import dev.dcarminatti.resolva_ja_api.services.TechnicianService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/user/technician")
public class TechnicianController {
    @Autowired
    private TechnicianService technicianService;

    @GetMapping
    public ResponseEntity<List<TechnicianDTO>> getAll() {
        return ResponseEntity.ok(technicianService.findAll().stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianDTO> getById(@PathVariable Long id) {
        Optional<Technician> technician = technicianService.findById(id);
        if (technician.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(technician.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TechnicianDTO newTechnician) {
        try {
            Technician technicianEntity = toEntity(newTechnician);
            Technician savedTechnician = technicianService.save(technicianEntity);
            return ResponseEntity.ok(toDTO(savedTechnician));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TechnicianDTO updatedTechnician) {
        try {
            Optional<Technician> existingTechnician = technicianService.findById(id);
            if(existingTechnician.isEmpty()) return ResponseEntity.notFound().build();
            Technician technicianEntity = toEntity(updatedTechnician);
            technicianService.update(id, technicianEntity);
            return ResponseEntity.noContent().build();
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Technician> existingTechnician = technicianService.findById(id);
        if(existingTechnician.isEmpty()) return ResponseEntity.notFound().build();
        technicianService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Technician toEntity(TechnicianDTO dto) {
        Technician technicianEntity = new Technician();
        technicianEntity.setId(dto.id());
        technicianEntity.setName(dto.name());
        technicianEntity.setEmail(dto.email());
        technicianEntity.setPassword(dto.password());
        technicianEntity.setCreatedAt(dto.createdAt());
        technicianEntity.setUpdatedAt(dto.updatedAt());
        technicianEntity.setSpecialty(dto.specialty());
        return technicianEntity;
    }

    private TechnicianDTO toDTO(Technician entity) {
        return new TechnicianDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getSpecialty()
        );
    }
}
