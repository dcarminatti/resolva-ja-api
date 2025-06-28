package dev.dcarminatti.rja_api.api.controller;

import dev.dcarminatti.rja_api.exception.ResourceNotFoundException;
import dev.dcarminatti.rja_api.exception.InvalidRatingException;
import dev.dcarminatti.rja_api.model.entity.Rating;
import dev.dcarminatti.rja_api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.findAll();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long id) {
        Rating rating = ratingService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
        return ResponseEntity.ok(rating);
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@Valid @RequestBody Rating rating) {
        // Validate rating score (assuming 1-5 scale)
        if (rating.getScore() < 1 || rating.getScore() > 5) {
            throw new InvalidRatingException(rating.getScore(), 1, 5);
        }
        
        Rating savedRating = ratingService.save(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable Long id, @Valid @RequestBody Rating rating) {
        // Validate rating score
        if (rating.getScore() < 1 || rating.getScore() > 5) {
            throw new InvalidRatingException(rating.getScore(), 1, 5);
        }
        
        Rating updatedRating = ratingService.update(id, rating);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        if (!ratingService.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Rating", "id", id);
        }
        ratingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Business logic endpoints
    @GetMapping("/evaluator/{evaluatorId}")
    public ResponseEntity<List<Rating>> getRatingsByEvaluator(@PathVariable Long evaluatorId) {
        List<Rating> ratings = ratingService.findByEvaluator(evaluatorId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/score/{score}")
    public ResponseEntity<List<Rating>> getRatingsByScore(@PathVariable Integer score) {
        List<Rating> ratings = ratingService.findByScore(score);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/min-score/{minScore}")
    public ResponseEntity<List<Rating>> getRatingsByMinScore(@PathVariable Integer minScore) {
        List<Rating> ratings = ratingService.findByMinScore(minScore);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/max-score/{maxScore}")
    public ResponseEntity<List<Rating>> getRatingsByMaxScore(@PathVariable Integer maxScore) {
        List<Rating> ratings = ratingService.findByMaxScore(maxScore);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Rating>> searchRatingsByComment(@RequestParam String comment) {
        List<Rating> ratings = ratingService.findByCommentContaining(comment);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/average/evaluator/{evaluatorId}")
    public ResponseEntity<Double> getAverageScoreByEvaluator(@PathVariable Long evaluatorId) {
        Double averageScore = ratingService.getAverageScoreByEvaluator(evaluatorId);
        return ResponseEntity.ok(averageScore != null ? averageScore : 0.0);
    }

    @GetMapping("/count/evaluator/{evaluatorId}")
    public ResponseEntity<Long> countRatingsByEvaluator(@PathVariable Long evaluatorId) {
        Long count = ratingService.countByEvaluator(evaluatorId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/ordered/score-desc")
    public ResponseEntity<List<Rating>> getAllRatingsOrderedByScoreDesc() {
        List<Rating> ratings = ratingService.findAllOrderByScoreDesc();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/ordered/score-asc")
    public ResponseEntity<List<Rating>> getAllRatingsOrderedByScoreAsc() {
        List<Rating> ratings = ratingService.findAllOrderByScoreAsc();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/high-ratings")
    public ResponseEntity<List<Rating>> getHighRatings() {
        List<Rating> ratings = ratingService.findHighRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/low-ratings")
    public ResponseEntity<List<Rating>> getLowRatings() {
        List<Rating> ratings = ratingService.findLowRatings();
        return ResponseEntity.ok(ratings);
    }

    @PostMapping("/add")
    public ResponseEntity<Rating> addRating(
            @RequestParam Integer score,
            @RequestParam(required = false) String comment,
            @RequestParam Long evaluatorId) {
        
        if (score < 1 || score > 5) {
            throw new InvalidRatingException(score, 1, 5);
        }
        
        Rating rating = ratingService.addRating(score, comment, evaluatorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }
}
