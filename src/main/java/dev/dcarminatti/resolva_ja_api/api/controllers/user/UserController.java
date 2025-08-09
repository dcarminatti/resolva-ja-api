package dev.dcarminatti.resolva_ja_api.api.controllers.user;

import dev.dcarminatti.resolva_ja_api.api.dtos.user.UserDTO;
import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.User;
import dev.dcarminatti.resolva_ja_api.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(this::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody UserDTO newUser) {
        try {
            User userEntity = toEntity(newUser);
            User savedUser = userService.save(userEntity);
            return ResponseEntity.ok(toDTO(savedUser));
        } catch (ValidateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody UserDTO updatedUser) {
        try {
            User existingUser = userService.findById(id);
            if(existingUser == null) return ResponseEntity.notFound().build();
            User userEntity = toEntity(updatedUser);
            userService.update(id, userEntity);
            return ResponseEntity.noContent().build();
        } catch (ValidateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User existingUser = userService.findById(id);
        if(existingUser == null) return ResponseEntity.notFound().build();
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private User toEntity(UserDTO dto) {
        User userEntity = new User();
        userEntity.setId(dto.id());
        userEntity.setName(dto.name());
        userEntity.setEmail(dto.email());
        userEntity.setPassword(dto.password());
        userEntity.setCreatedAt(dto.createdAt());
        userEntity.setUpdatedAt(dto.updatedAt());
        return userEntity;
    }

    private UserDTO toDTO(User entity) {
        return new UserDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
