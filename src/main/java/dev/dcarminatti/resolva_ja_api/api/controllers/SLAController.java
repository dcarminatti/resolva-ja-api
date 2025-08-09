package dev.dcarminatti.resolva_ja_api.api.controllers;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.SLA;
import dev.dcarminatti.resolva_ja_api.api.dtos.SlaDTO;
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
@RequestMapping("/api/v1/sla")
public class SLAController {
    @Autowired
    private SLAService slaService;

    @GetMapping
    public List<SlaDTO> getAll() {
        return slaService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlaDTO> getById(@PathVariable Long id) {
        Optional<SLA> sla = slaService.findById(id);
        if (sla.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(sla.get()));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody SlaDTO dto) {
        try {
            SLA sla = toEntity(dto);
            SLA saved = slaService.save(sla);
            return ResponseEntity.ok(toDTO(saved));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody SlaDTO dto) {
        try {
            SLA sla = toEntity(dto);
            sla.setId(id);
            SLA updated = slaService.update(sla);
            return ResponseEntity.ok(toDTO(updated));
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        slaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private SlaDTO toDTO(SLA sla) {
        return new SlaDTO(sla.getId(), sla.getDescription(), sla.getResponseTimeHours(), sla.getResolutionTimeHours());
    }

    private SLA toEntity(SlaDTO dto) {
        SLA sla = new SLA();
        sla.setId(dto.id());
        sla.setDescription(dto.description());
        sla.setResponseTimeHours(dto.responseTimeHours());
        sla.setResolutionTimeHours(dto.resolutionTimeHours());
        return sla;
    }
}

