package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.TechnicianMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianMetricsRepository extends JpaRepository<TechnicianMetrics, Long> {
    
    Optional<TechnicianMetrics> findByTechnicianId(Long technicianId);
    
    List<TechnicianMetrics> findByTotalRequestsHandledGreaterThanEqual(int totalRequests);
    
    List<TechnicianMetrics> findByAverageResponseTimeLessThanEqual(int responseTime);
    
    List<TechnicianMetrics> findByAverageResolutionTimeLessThanEqual(int resolutionTime);
    
    @Query("SELECT tm FROM TechnicianMetrics tm ORDER BY tm.totalResolvedRequests DESC")
    List<TechnicianMetrics> findAllOrderByResolvedRequestsDesc();
    
    @Query("SELECT tm FROM TechnicianMetrics tm ORDER BY tm.averageResponseTime ASC")
    List<TechnicianMetrics> findAllOrderByResponseTimeAsc();
    
    @Query("SELECT tm FROM TechnicianMetrics tm ORDER BY tm.averageResolutionTime ASC")
    List<TechnicianMetrics> findAllOrderByResolutionTimeAsc();
    
    @Query("SELECT tm FROM TechnicianMetrics tm WHERE tm.totalPendingRequests > 0")
    List<TechnicianMetrics> findTechniciansWithPendingRequests();
    
    @Query("SELECT AVG(tm.averageResponseTime) FROM TechnicianMetrics tm")
    Double getOverallAverageResponseTime();
    
    @Query("SELECT AVG(tm.averageResolutionTime) FROM TechnicianMetrics tm")
    Double getOverallAverageResolutionTime();
    
    @Query("SELECT SUM(tm.totalRequestsHandled) FROM TechnicianMetrics tm")
    Long getTotalRequestsHandledByAllTechnicians();
    
    boolean existsByTechnicianId(Long technicianId);
}
