package dev.dcarminatti.resolva_ja_api.services;

import dev.dcarminatti.resolva_ja_api.exceptions.ValidateException;
import dev.dcarminatti.resolva_ja_api.models.entities.Administrator;
import dev.dcarminatti.resolva_ja_api.models.entities.Technician;
import dev.dcarminatti.resolva_ja_api.models.entities.User;
import dev.dcarminatti.resolva_ja_api.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private TechnicianService technicianService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    public User save(User user) {
        try {
            LocalDateTime now = LocalDateTime.now();
            user.setId(null);
            user.setCreatedAt(now.toString());
            user.setUpdatedAt(now.toString());
            this.validateUser(user);
            return userRepository.save(user);
        } catch (ValidateException ex) {
            throw new ValidateException("Error saving user: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while saving the user", ex);
        }
    }

    public void update(Long id, User user) {
        try {
            LocalDateTime now = LocalDateTime.now();
            user.setId(id);
            user.setUpdatedAt(now.toString());
            userRepository.save(user);
        } catch (ValidateException ex) {
            throw new ValidateException("Error updating user: " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while updating the user", ex);
        }
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new ValidateException("User name cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidateException("User email cannot be null or empty");
        }
        if (user.getPassword().length() < 6) {
            throw new ValidateException("User password must be at least 6 characters long");
        }

        try {
            LocalDateTime createdAt = LocalDateTime.parse(user.getCreatedAt());
            LocalDateTime updatedAt = LocalDateTime.parse(user.getUpdatedAt());
            LocalDateTime now = LocalDateTime.now();

            if (updatedAt.isBefore(createdAt)) {
                throw new ValidateException("User updatedAt cannot be before createdAt");
            }
        } catch (Exception e) {
            throw new ValidateException("Invalid date format for createdAt or updatedAt: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        Optional<Administrator> administrator = administratorService.findById(user.getId());
        Optional<Technician> technician = technicianService.findById(user.getId());
        if (administrator.isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (technician.isPresent()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_TECHNICIAN"));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
