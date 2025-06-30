package com.cabbyai.rating.service;

import com.cabbyai.rating.entity.Rating;
import com.cabbyai.rating.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    public Rating submitRating(Rating rating) {
        // Validate score range
        if (rating.getScore() < 1 || rating.getScore() > 5) {
            throw new RuntimeException("Rating score must be between 1 and 5");
        }
        
        // Check if rating already exists for this ride and user combination
        Optional<Rating> existingRating = ratingRepository.findByRideIdAndFromUserId(
            rating.getRideId(), rating.getFromUserId());
        
        if (existingRating.isPresent()) {
            throw new RuntimeException("Rating already submitted for this ride");
        }
        
        return ratingRepository.save(rating);
    }
    
    public List<Rating> getUserRatings(Long userId) {
        return ratingRepository.findByToUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Rating> getRatingsByUser(Long userId) {
        return ratingRepository.findByFromUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Rating> getRideRatings(Long rideId) {
        return ratingRepository.findByRideId(rideId);
    }
    
    public Double getAverageRating(Long userId) {
        List<Rating> ratings = ratingRepository.findByToUserId(userId);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        
        double sum = ratings.stream().mapToInt(Rating::getScore).sum();
        return sum / ratings.size();
    }
    
    public Optional<Rating> getRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId);
    }
    
    public Rating updateRating(Long ratingId, Rating ratingDetails) {
        Optional<Rating> ratingOpt = ratingRepository.findById(ratingId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            
            if (ratingDetails.getScore() != null && 
                ratingDetails.getScore() >= 1 && ratingDetails.getScore() <= 5) {
                rating.setScore(ratingDetails.getScore());
            }
            
            if (ratingDetails.getComments() != null) {
                rating.setComments(ratingDetails.getComments());
            }
            
            return ratingRepository.save(rating);
        }
        throw new RuntimeException("Rating not found");
    }
}
