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
}
