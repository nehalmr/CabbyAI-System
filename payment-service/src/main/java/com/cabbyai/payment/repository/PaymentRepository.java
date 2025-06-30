package com.cabbyai.payment.repository;

import com.cabbyai.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRideId(Long rideId);
    List<Payment> findByUserIdOrderByTimestampDesc(Long userId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
}
