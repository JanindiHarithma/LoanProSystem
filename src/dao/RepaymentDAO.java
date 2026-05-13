package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepaymentDAO {

    // Generate repayment schedule after loan approval
    public boolean generateSchedule(int loanId, double emiAmount, 
                                     int months) {
        String sql = "INSERT INTO repayment (loan_id, due_date, amount_due, status) "
                   + "VALUES (?, DATE_ADD(NOW(), INTERVAL ? MONTH), ?, 'PENDING')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 1; i <= months; i++) {
                ps.setInt(1, loanId);
                ps.setInt(2, i);
                ps.setDouble(3, emiAmount);
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            System.out.println("Error generating schedule: " + e.getMessage());
            return false;
        }
    }

    // Get repayment schedule for a loan
    public List<Object[]> getSchedule(int loanId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT r.repayment_id, r.due_date, r.amount_due, "
                   + "r.amount_paid, r.status, r.penalty "
                   + "FROM repayment r WHERE r.loan_id = ? "
                   + "ORDER BY r.due_date";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("repayment_id"),
                    rs.getString("due_date"),
                    rs.getDouble("amount_due"),
                    rs.getDouble("amount_paid"),
                    rs.getString("status"),
                    rs.getDouble("penalty")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    // Mark installment as paid
    public boolean markAsPaid(int repaymentId, double amountPaid) {
        String sql = "UPDATE repayment SET amount_paid=?, paid_date=NOW(), "
                   + "status='PAID' WHERE repayment_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, amountPaid);
            ps.setInt(2, repaymentId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Get approved loans for dropdown
    public List<Object[]> getApprovedLoansForDropdown() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT l.loan_id, c.full_name, l.loan_amount, "
                   + "l.emi_amount, l.duration_months "
                   + "FROM loan l "
                   + "JOIN customer c ON l.customer_id = c.customer_id "
                   + "WHERE l.status = 'APPROVED'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("loan_id"),
                    rs.getString("full_name"),
                    rs.getDouble("loan_amount"),
                    rs.getDouble("emi_amount"),
                    rs.getInt("duration_months")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
}