package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    Optional<Department> findByName(String name);
    
    List<Department> findByLocationId(Long locationId);
    
    List<Department> findByManagerId(Long managerId);
    
    @Query("SELECT d FROM Department d WHERE d.name LIKE %:name%")
    List<Department> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT d FROM Department d WHERE d.location.name LIKE %:locationName%")
    List<Department> findByLocationName(@Param("locationName") String locationName);
    
    boolean existsByName(String name);
    
    boolean existsByManagerId(Long managerId);
}
