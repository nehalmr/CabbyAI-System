package com.cabbyai.rating.repository;

import com.cabbyai.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByToUserIdOrderByCreatedAtDesc(Long toUserId);
    List<Rating> findByFromUserIdOrderByCreatedAtDesc(Long fromUserId);
    List<Rating> findByRideId(Long rideId);
    List<Rating> findByToUserId(Long toUserId);
    Optional<Rating> findByRideIdAndFromUserId(Long rideId, Long fromUserId);
    List<Rating> findByType(Rating.RatingType type);
}
