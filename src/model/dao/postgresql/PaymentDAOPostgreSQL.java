package model.dao.postgresql;

import model.dao.PaymentDAO;
import model.entity.Payment;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDAOPostgreSQL implements PaymentDAO {
    
    private final DatabaseConnection dbConnection;
    
    public PaymentDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public Payment save(Payment payment) {
        String sql = "INSERT INTO payments (enrollment_id, amount, payment_method, status) " +
                     "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, payment.getEnrollmentId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getStatus().toString());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                payment.setId(rs.getInt("id"));
            }
            return payment;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save payment", e);
        }
    }
    
    @Override
    public Payment update(Payment payment) {
        String sql = "UPDATE payments SET enrollment_id = ?, amount = ?, payment_method = ?, status = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, payment.getEnrollmentId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getStatus().toString());
            stmt.setInt(5, payment.getId());
            
            stmt.executeUpdate();
            return payment;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update payment", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public Optional<Payment> findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToPayment(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM payments ORDER BY id";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
            
        } catch (SQLException e) {
            return payments;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM payments";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            return 0;
        }
    }
    
    @Override
    public List<Payment> findByEnrollmentId(int enrollmentId) {
        String sql = "SELECT * FROM payments WHERE enrollment_id = ? ORDER BY id";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
            
        } catch (SQLException e) {
            return payments;
        }
    }
    
    @Override
    public List<Payment> findPendingPayments() {
        String sql = "SELECT * FROM payments WHERE status = 'PENDING' ORDER BY id";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
            
        } catch (SQLException e) {
            return payments;
        }
    }
    
    @Override
    public List<Payment> findCompletedPayments() {
        String sql = "SELECT * FROM payments WHERE status = 'COMPLETED' ORDER BY id";
        List<Payment> payments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
            return payments;
            
        } catch (SQLException e) {
            return payments;
        }
    }
    
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getInt("id"));
        payment.setEnrollmentId(rs.getInt("enrollment_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setStatus(Payment.PaymentStatus.valueOf(rs.getString("status")));
        
        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            payment.setPaymentDate(paymentDate.toLocalDateTime());
        }
        
        return payment;
    }
}
