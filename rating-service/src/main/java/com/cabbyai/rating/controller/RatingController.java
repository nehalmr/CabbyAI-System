package com.cabbyai.rating.controller;

import com.cabbyai.rating.entity.Rating;
import com.cabbyai.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@Tag(name = "Rating Management", description = "Rating and feedback system")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @PostMapping
    @Operation(summary = "Submit a rating")
    public ResponseEntity<?> submitRating(@RequestBody Rating rating) {
        try {
            Rating submittedRating = ratingService.submitRating(rating);
            return ResponseEntity.ok(submittedRating);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get ratings for a user")
    public ResponseEntity<List<Rating>> getUserRatings(@PathVariable Long userId) {
        List<Rating> ratings = ratingService.getUserRatings(userId);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get ratings submitted by a user")
    public ResponseEntity<List<Rating>> getRatingsByUser(@PathVariable Long userId) {
        List<Rating> ratings = ratingService.getRatingsByUser(userId);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/ride/{rideId}")
    @Operation(summary = "Get ratings for a ride")
    public ResponseEntity<List<Rating>> getRideRatings(@PathVariable Long rideId) {
        List<Rating> ratings = ratingService.getRideRatings(rideId);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/average/{userId}")
    @Operation(summary = "Get average rating for a user")
    public ResponseEntity<Map<String, Double>> getAverageRating(@PathVariable Long userId) {
        Double averageRating = ratingService.getAverageRating(userId);
        return ResponseEntity.ok(Map.of("averageRating", averageRating));
    }
    
    @GetMapping("/{ratingId}")
    @Operation(summary = "Get rating by ID")
    public ResponseEntity<?> getRatingById(@PathVariable Long ratingId) {
        Optional<Rating> rating = ratingService.getRatingById(ratingId);
        if (rating.isPresent()) {
            return ResponseEntity.ok(rating.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{ratingId}")
    @Operation(summary = "Update a rating")
    public ResponseEntity<?> updateRating(@PathVariable Long ratingId, @RequestBody Rating ratingDetails) {
        try {
            Rating updatedRating = ratingService.updateRating(ratingId, ratingDetails);
            return ResponseEntity.ok(updatedRating);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
