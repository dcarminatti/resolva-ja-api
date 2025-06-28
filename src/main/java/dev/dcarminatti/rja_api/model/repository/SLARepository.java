package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.SLA;
import dev.dcarminatti.rja_api.model.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SLARepository extends JpaRepository<SLA, Long> {
    
    Optional<SLA> findByName(String name);
    
    List<SLA> findByPriority(Priority priority);
    
    List<SLA> findByExpectedResponseTimeHoursLessThanEqual(Integer hours);
    
    List<SLA> findByExpectedResponseTimeHoursGreaterThanEqual(Integer hours);
    
    @Query("SELECT s FROM SLA s WHERE s.name LIKE %:name%")
    List<SLA> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT s FROM SLA s WHERE s.description LIKE %:description%")
    List<SLA> findByDescriptionContaining(@Param("description") String description);
    
    @Query("SELECT s FROM SLA s ORDER BY s.expectedResponseTimeHours ASC")
    List<SLA> findAllOrderByResponseTimeAsc();
    
    boolean existsByName(String name);
}
