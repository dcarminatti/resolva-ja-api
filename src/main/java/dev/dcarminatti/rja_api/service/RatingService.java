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
}
