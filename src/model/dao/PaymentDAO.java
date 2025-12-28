package model.dao;

import model.entity.Payment;
import java.util.List;

/**
 * Data Access Object interface for Payment entity.
 */
public interface PaymentDAO extends GenericDAO<Payment> {
    
    /**
     * Finds all payments for an enrollment.
     * @param enrollmentId Enrollment ID
     * @return List of payments
     */
    List<Payment> findByEnrollmentId(int enrollmentId);
    
    /**
     * Finds all pending payments.
     * @return List of pending payments
     */
    List<Payment> findPendingPayments();
    
    /**
     * Finds all completed payments.
     * @return List of completed payments
     */
    List<Payment> findCompletedPayments();
}
