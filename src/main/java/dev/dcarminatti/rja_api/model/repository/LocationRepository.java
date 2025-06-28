package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.Location;
import dev.dcarminatti.rja_api.model.enums.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    Optional<Location> findByName(String name);
    
    List<Location> findByLocationType(LocationType locationType);
    
    List<Location> findByParentLocationId(Long parentLocationId);
    
    @Query("SELECT l FROM Location l WHERE l.parentLocation IS NULL")
    List<Location> findRootLocations();
    
    @Query("SELECT l FROM Location l WHERE l.name LIKE %:name%")
    List<Location> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT l FROM Location l WHERE l.description LIKE %:description%")
    List<Location> findByDescriptionContaining(@Param("description") String description);
    
    boolean existsByName(String name);
}
