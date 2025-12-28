package model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a payment record for course enrollment.
 */
public class Payment {
    private int id;
    private int enrollmentId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private PaymentStatus status;

    public enum PaymentStatus {
        PENDING, COMPLETED, REFUNDED
    }

    // Default constructor
    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    // Parameterized constructor
    public Payment(int id, int enrollmentId, BigDecimal amount, 
                   String paymentMethod, PaymentStatus status) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Payment[id=%d, enrollmentId=%d, amount=%s, status=%s]",
                id, enrollmentId, amount, status);
    }
}
