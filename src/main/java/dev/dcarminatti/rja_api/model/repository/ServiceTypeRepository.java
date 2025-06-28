package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    
    Optional<ServiceType> findByName(String name);
    
    @Query("SELECT st FROM ServiceType st WHERE st.name LIKE %:name%")
    List<ServiceType> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT st FROM ServiceType st WHERE st.description LIKE %:description%")
    List<ServiceType> findByDescriptionContaining(@Param("description") String description);
    
    boolean existsByName(String name);
}
