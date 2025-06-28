package dev.dcarminatti.rja_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRatingException extends RuntimeException {
    
    private Integer rating;
    private Integer minRating;
    private Integer maxRating;

    public InvalidRatingException(Integer rating, Integer minRating, Integer maxRating) {
        super(String.format("Invalid rating %d: must be between %d and %d", rating, minRating, maxRating));
        this.rating = rating;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    public InvalidRatingException(String message) {
        super(message);
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public Integer getMaxRating() {
        return maxRating;
    }
}
