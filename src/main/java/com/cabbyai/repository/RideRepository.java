package com.cabbyai.repository;

import com.cabbyai.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Ride> findByDriverIdOrderByCreatedAtDesc(Long driverId);
}
