package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.ResourceAlreadyExistsException;
import dev.dcarminatti.rja_api.model.entity.User;
import dev.dcarminatti.rja_api.model.enums.Role;
import dev.dcarminatti.rja_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", user.getEmail());
        }
        if (userService.existsByRegistrationNumber(user.getRegistrationNumber())) {
            throw new ResourceAlreadyExistsException("User", "registrationNumber", user.getRegistrationNumber());
        }
        
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/registration/{registrationNumber}")
    public ResponseEntity<User> getUserByRegistrationNumber(@PathVariable String registrationNumber) {
        User user = userService.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "registrationNumber", registrationNumber));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        List<User> users = userService.findByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/available")
    public ResponseEntity<List<User>> getAvailableUsers() {
        List<User> users = userService.findAvailableUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/technicians/available")
    public ResponseEntity<List<User>> getAvailableTechnicians() {
        List<User> technicians = userService.findAvailableTechnicians();
        return ResponseEntity.ok(technicians);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        List<User> users = userService.findByNameContaining(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/service-type/{serviceTypeId}")
    public ResponseEntity<List<User>> getUsersByServiceType(@PathVariable Long serviceTypeId) {
        List<User> users = userService.findByServiceType(serviceTypeId);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<User> updateUserAvailability(@PathVariable Long id, @RequestParam boolean available) {
        User user = userService.setAvailability(id, available);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/count/role/{role}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable Role role) {
        long count = userService.countByRole(role);
        return ResponseEntity.ok(count);
    }
}
