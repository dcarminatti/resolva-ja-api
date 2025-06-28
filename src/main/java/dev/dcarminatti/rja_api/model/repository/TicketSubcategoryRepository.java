package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.TicketSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketSubcategoryRepository extends JpaRepository<TicketSubcategory, Long> {
    
    Optional<TicketSubcategory> findByName(String name);
    
    List<TicketSubcategory> findByCategoryId(Long categoryId);
    
    List<TicketSubcategory> findByAssociatedSLAId(Long slaId);
    
    @Query("SELECT ts FROM TicketSubcategory ts JOIN ts.associatedServiceTypes st WHERE st.id = :serviceTypeId")
    List<TicketSubcategory> findByServiceTypeId(@Param("serviceTypeId") Long serviceTypeId);
    
    @Query("SELECT ts FROM TicketSubcategory ts WHERE ts.name LIKE %:name%")
    List<TicketSubcategory> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT ts FROM TicketSubcategory ts WHERE ts.description LIKE %:description%")
    List<TicketSubcategory> findByDescriptionContaining(@Param("description") String description);
    
    @Query("SELECT ts FROM TicketSubcategory ts WHERE ts.category.name LIKE %:categoryName%")
    List<TicketSubcategory> findByCategoryName(@Param("categoryName") String categoryName);
    
    boolean existsByName(String name);
}
