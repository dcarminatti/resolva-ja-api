package dev.dcarminatti.rja_api.service;

import dev.dcarminatti.rja_api.model.entity.Rating;
import dev.dcarminatti.rja_api.model.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    // Basic CRUD operations
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Optional<Rating> findById(Long id) {
        return ratingRepository.findById(id);
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating update(Long id, Rating updatedRating) {
        return ratingRepository.findById(id)
                .map(rating -> {
                    rating.setScore(updatedRating.getScore());
                    rating.setComment(updatedRating.getComment());
                    rating.setEvaluator(updatedRating.getEvaluator());
                    rating.setRating(updatedRating.getRating());
                    return ratingRepository.save(rating);
                })
                .orElseThrow(() -> new RuntimeException("Rating not found with id: " + id));
    }

    public void deleteById(Long id) {
        ratingRepository.deleteById(id);
    }

    // Business logic methods
    public List<Rating> findByEvaluator(Long evaluatorId) {
        return ratingRepository.findByEvaluatorId(evaluatorId);
    }

    public List<Rating> findByScore(Integer score) {
        return ratingRepository.findByScore(score);
    }

    public List<Rating> findByMinScore(Integer minScore) {
        return ratingRepository.findByScoreGreaterThanEqual(minScore);
    }

    public List<Rating> findByMaxScore(Integer maxScore) {
        return ratingRepository.findByScoreLessThanEqual(maxScore);
    }

    public List<Rating> findByCommentContaining(String comment) {
        return ratingRepository.findByCommentContaining(comment);
    }

    public Double getAverageScoreByEvaluator(Long evaluatorId) {
        return ratingRepository.getAverageScoreByEvaluator(evaluatorId);
    }

    public Long countByEvaluator(Long evaluatorId) {
        return ratingRepository.countByEvaluatorId(evaluatorId);
    }

    public List<Rating> findAllOrderByScoreDesc() {
        return ratingRepository.findAllOrderByScoreDesc();
    }

    public List<Rating> findAllOrderByScoreAsc() {
        return ratingRepository.findAllOrderByScoreAsc();
    }

    public List<Rating> findHighRatings() {
        return ratingRepository.findByScoreGreaterThanEqual(4);
    }

    public List<Rating> findLowRatings() {
        return ratingRepository.findByScoreLessThanEqual(2);
    }

    public Rating addRating(Integer score, String comment, Long evaluatorId) {
        Rating rating = new Rating();
        rating.setScore(score);
        rating.setComment(comment);
        // Here you would typically fetch the User entity
        // For now, we'll assume it's set elsewhere
        return ratingRepository.save(rating);
    }
}
