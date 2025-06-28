package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.User;
import dev.dcarminatti.rja_api.model.enums.Role;
import dev.dcarminatti.rja_api.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Basic CRUD operations
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setRegistrationNumber(updatedUser.getRegistrationNumber());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setRole(updatedUser.getRole());
                    user.setAvailable(updatedUser.isAvailable());
                    user.setServiceTypes(updatedUser.getServiceTypes());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // Business logic methods
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByRegistrationNumber(String registrationNumber) {
        return userRepository.findByRegistrationNumber(registrationNumber);
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> findAvailableUsers() {
        return userRepository.findByAvailable(true);
    }

    public List<User> findAvailableTechnicians() {
        return userRepository.findByRoleAndAvailable(Role.TECHNICIAN, true);
    }

    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }

    public List<User> findByServiceType(Long serviceTypeId) {
        return userRepository.findByServiceTypeId(serviceTypeId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByRegistrationNumber(String registrationNumber) {
        return userRepository.existsByRegistrationNumber(registrationNumber);
    }

    public User setAvailability(Long userId, boolean available) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setAvailable(available);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public long countByRole(Role role) {
        return userRepository.findByRole(role).size();
    }
}
