package com.cabbyai.payment.service;

import com.cabbyai.payment.entity.Payment;
import com.cabbyai.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPaymentCreatesTransactionAndFinalStatus() {
        Payment payment = new Payment(10L, 20L, BigDecimal.TEN, Payment.PaymentMethod.CREDIT_CARD);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment(payment);

        assertNotNull(result.getTransactionId());
        assertTrue(result.getStatus() == Payment.PaymentStatus.COMPLETED
                || result.getStatus() == Payment.PaymentStatus.FAILED);
        assertNotNull(result.getGatewayResponse());
        verify(paymentRepository, times(2)).save(payment);
    }

    @Test
    void getPaymentByRideIdDelegatesToRepository() {
        Payment payment = new Payment();
        when(paymentRepository.findByRideId(10L)).thenReturn(Optional.of(payment));

        assertSame(payment, paymentService.getPaymentByRideId(10L).orElseThrow());
        verify(paymentRepository).findByRideId(10L);
    }

    @Test
    void getUserPaymentsReturnsNewestPayments() {
        List<Payment> payments = List.of(new Payment(), new Payment());
        when(paymentRepository.findByUserIdOrderByTimestampDesc(20L)).thenReturn(payments);

        assertEquals(payments, paymentService.getUserPayments(20L));
    }

    @Test
    void getPaymentByIdDelegatesToRepository() {
        Payment payment = new Payment();
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertTrue(paymentService.getPaymentById(1L).isPresent());
    }

    @Test
    void refundPaymentRefundsCompletedPayment() {
        Payment payment = new Payment();
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.refundPayment(1L);

        assertEquals(Payment.PaymentStatus.REFUNDED, result.getStatus());
        assertEquals("Payment refunded successfully", result.getGatewayResponse());
        verify(paymentRepository).save(payment);
    }

    @Test
    void refundPaymentRejectsMissingOrIncompletePayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> paymentService.refundPayment(1L));

        Payment pending = new Payment();
        pending.setStatus(Payment.PaymentStatus.PENDING);
        when(paymentRepository.findById(2L)).thenReturn(Optional.of(pending));
        assertThrows(RuntimeException.class, () -> paymentService.refundPayment(2L));
        verify(paymentRepository, never()).save(pending);
    }
}
