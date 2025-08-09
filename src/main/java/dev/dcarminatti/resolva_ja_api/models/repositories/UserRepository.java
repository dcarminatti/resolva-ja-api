package dev.dcarminatti.resolva_ja_api.models.repositories;

import dev.dcarminatti.resolva_ja_api.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
