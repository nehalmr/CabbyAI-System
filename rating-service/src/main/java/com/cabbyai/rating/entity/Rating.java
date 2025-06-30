package com.cabbyai.rating.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;
    
    @Column(nullable = false)
    private Long rideId;
    
    @Column(nullable = false)
    private Long fromUserId;
    
    @Column(nullable = false)
    private Long toUserId;
    
    @Column(nullable = false)
    private Integer score; // 1-5 rating
    
    @Column(length = 1000)
    private String comments;
    
    @Enumerated(EnumType.STRING)
    private RatingType type;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    public enum RatingType {
        USER_TO_DRIVER, DRIVER_TO_USER
    }
    
    // Constructors
    public Rating() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Rating(Long rideId, Long fromUserId, Long toUserId, Integer score, String comments, RatingType type) {
        this();
        this.rideId = rideId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.score = score;
        this.comments = comments;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getRatingId() { return ratingId; }
    public void setRatingId(Long ratingId) { this.ratingId = ratingId; }
    
    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }
    
    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
    
    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    
    public RatingType getType() { return type; }
    public void setType(RatingType type) { this.type = type; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
