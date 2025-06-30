package com.cabbyai.payment.service;

import com.cabbyai.payment.entity.Payment;
import com.cabbyai.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    public Payment processPayment(Payment payment) {
        // Set initial status
        payment.setStatus(Payment.PaymentStatus.PROCESSING);
        
        // Generate transaction ID
        payment.setTransactionId(UUID.randomUUID().toString());
        
        // Save payment
        Payment savedPayment = paymentRepository.save(payment);
        
        // Simulate payment processing
        boolean paymentSuccess = simulatePaymentGateway(payment);
        
        if (paymentSuccess) {
            savedPayment.setStatus(Payment.PaymentStatus.COMPLETED);
            savedPayment.setGatewayResponse("Payment processed successfully");
        } else {
            savedPayment.setStatus(Payment.PaymentStatus.FAILED);
            savedPayment.setGatewayResponse("Payment processing failed");
        }
        
        return paymentRepository.save(savedPayment);
    }
    
    public Optional<Payment> getPaymentByRideId(Long rideId) {
        return paymentRepository.findByRideId(rideId);
    }
    
    public List<Payment> getUserPayments(Long userId) {
        return paymentRepository.findByUserIdOrderByTimestampDesc(userId);
    }
    
    public Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }
    
    public Payment refundPayment(Long paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            if (payment.getStatus() == Payment.PaymentStatus.COMPLETED) {
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
                payment.setGatewayResponse("Payment refunded successfully");
                return paymentRepository.save(payment);
            } else {
                throw new RuntimeException("Payment cannot be refunded");
            }
        }
        throw new RuntimeException("Payment not found");
    }
    
    private boolean simulatePaymentGateway(Payment payment) {
        // Simulate payment processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate 95% success rate
        return Math.random() < 0.95;
    }
}
