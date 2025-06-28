package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.User;
import dev.dcarminatti.rja_api.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByRegistrationNumber(String registrationNumber);
    
    List<User> findByRole(Role role);
    
    List<User> findByAvailable(boolean available);
    
    List<User> findByRoleAndAvailable(Role role, boolean available);
    
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT u FROM User u JOIN u.serviceTypes st WHERE st.id = :serviceTypeId")
    List<User> findByServiceTypeId(@Param("serviceTypeId") Long serviceTypeId);
    
    boolean existsByEmail(String email);
    
    boolean existsByRegistrationNumber(String registrationNumber);
}
