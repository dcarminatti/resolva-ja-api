package dev.dcarminatti.rja_api.model.repository;

import dev.dcarminatti.rja_api.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findByEvaluatorId(Long evaluatorId);
    
    List<Rating> findByScore(Integer score);
    
    List<Rating> findByScoreGreaterThanEqual(Integer score);
    
    List<Rating> findByScoreLessThanEqual(Integer score);
    
    @Query("SELECT r FROM Rating r WHERE r.comment LIKE %:comment%")
    List<Rating> findByCommentContaining(@Param("comment") String comment);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.evaluator.id = :evaluatorId")
    Double getAverageScoreByEvaluator(@Param("evaluatorId") Long evaluatorId);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.evaluator.id = :evaluatorId")
    Long countByEvaluatorId(@Param("evaluatorId") Long evaluatorId);
    
    @Query("SELECT r FROM Rating r ORDER BY r.score DESC")
    List<Rating> findAllOrderByScoreDesc();
    
    @Query("SELECT r FROM Rating r ORDER BY r.score ASC")
    List<Rating> findAllOrderByScoreAsc();
}
