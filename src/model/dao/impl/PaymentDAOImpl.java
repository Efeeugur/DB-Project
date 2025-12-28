package model.dao.impl;

import model.dao.PaymentDAO;
import model.entity.Payment;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of PaymentDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class PaymentDAOImpl implements PaymentDAO {
    
    private final Map<Integer, Payment> payments = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public Payment save(Payment payment) {
        payment.setId(nextId++);
        payments.put(payment.getId(), payment);
        return payment;
    }
    
    @Override
    public Optional<Payment> findById(int id) {
        return Optional.ofNullable(payments.get(id));
    }
    
    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments.values());
    }
    
    @Override
    public Payment update(Payment payment) {
        if (payments.containsKey(payment.getId())) {
            payments.put(payment.getId(), payment);
            return payment;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return payments.remove(id) != null;
    }
    
    @Override
    public int count() {
        return payments.size();
    }
    
    @Override
    public List<Payment> findByEnrollmentId(int enrollmentId) {
        return payments.values().stream()
                .filter(p -> p.getEnrollmentId() == enrollmentId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Payment> findPendingPayments() {
        return payments.values().stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.PENDING)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Payment> findCompletedPayments() {
        return payments.values().stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
                .collect(Collectors.toList());
    }
}
