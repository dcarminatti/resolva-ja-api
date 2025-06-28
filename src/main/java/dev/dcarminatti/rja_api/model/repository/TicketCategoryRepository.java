package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    
    Optional<TicketCategory> findByName(String name);
    
    @Query("SELECT tc FROM TicketCategory tc WHERE tc.name LIKE %:name%")
    List<TicketCategory> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT tc FROM TicketCategory tc WHERE tc.description LIKE %:description%")
    List<TicketCategory> findByDescriptionContaining(@Param("description") String description);
    
    boolean existsByName(String name);
}
