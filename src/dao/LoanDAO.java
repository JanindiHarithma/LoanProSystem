package dao;

import model.Loan;
import model.LoanType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // Get all loan types
    public List<LoanType> getAllLoanTypes() {
        List<LoanType> list = new ArrayList<>();
        String sql = "SELECT * FROM loan_type";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LoanType lt = new LoanType();
                lt.setTypeId(rs.getInt("type_id"));
                lt.setTypeName(rs.getString("type_name"));
                lt.setInterestRate(rs.getDouble("interest_rate"));
                lt.setMaxAmount(rs.getDouble("max_amount"));
                lt.setMaxDurationMonths(rs.getInt("max_duration_months"));
                list.add(lt);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    // Apply for loan
    public boolean applyLoan(Loan loan) {
        String sql = "INSERT INTO loan (customer_id, type_id, loan_amount, duration_months, emi_amount, status) VALUES (?,?,?,?,?,'PENDING')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, loan.getCustomerId());
            ps.setInt(2, loan.getTypeId());
            ps.setDouble(3, loan.getLoanAmount());
            ps.setInt(4, loan.getDurationMonths());
            ps.setDouble(5, loan.getEmiAmount());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error applying loan: " + e.getMessage());
            return false;
        }
    }

    // Get all pending loans
    public List<Loan> getPendingLoans() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT l.*, c.full_name, lt.type_name FROM loan l " +
                     "JOIN customer c ON l.customer_id = c.customer_id " +
                     "JOIN loan_type lt ON l.type_id = lt.type_id " +
                     "WHERE l.status = 'PENDING'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setCustomerId(rs.getInt("customer_id"));
                loan.setTypeId(rs.getInt("type_id"));
                loan.setLoanAmount(rs.getDouble("loan_amount"));
                loan.setDurationMonths(rs.getInt("duration_months"));
                loan.setEmiAmount(rs.getDouble("emi_amount"));
                loan.setStatus(rs.getString("status"));
                loan.setAppliedDate(rs.getString("applied_date"));
                loan.setCustomerName(rs.getString("full_name"));
                loan.setLoanTypeName(rs.getString("type_name"));
                list.add(loan);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    // Get all approved loans
    public List<Loan> getApprovedLoans() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT l.*, c.full_name, lt.type_name FROM loan l " +
                     "JOIN customer c ON l.customer_id = c.customer_id " +
                     "JOIN loan_type lt ON l.type_id = lt.type_id " +
                     "WHERE l.status = 'APPROVED'";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setCustomerId(rs.getInt("customer_id"));
                loan.setLoanAmount(rs.getDouble("loan_amount"));
                loan.setDurationMonths(rs.getInt("duration_months"));
                loan.setEmiAmount(rs.getDouble("emi_amount"));
                loan.setStatus(rs.getString("status"));
                loan.setCustomerName(rs.getString("full_name"));
                loan.setLoanTypeName(rs.getString("type_name"));
                list.add(loan);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    // Approve loan
    public boolean approveLoan(int loanId, String notes) {
        String sql = "UPDATE loan SET status='APPROVED', approved_date=NOW(), officer_notes=? WHERE loan_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, notes);
            ps.setInt(2, loanId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error approving loan: " + e.getMessage());
            return false;
        }
    }

    // Reject loan
    public boolean rejectLoan(int loanId, String notes) {
        String sql = "UPDATE loan SET status='REJECTED', officer_notes=? WHERE loan_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, notes);
            ps.setInt(2, loanId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error rejecting loan: " + e.getMessage());
            return false;
        }
    }

    // Get all loans for report
    public List<Loan> getAllLoansReport() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT l.*, c.full_name, lt.type_name FROM loan l " +
                     "JOIN customer c ON l.customer_id = c.customer_id " +
                     "JOIN loan_type lt ON l.type_id = lt.type_id " +
                     "ORDER BY l.applied_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setLoanAmount(rs.getDouble("loan_amount"));
                loan.setDurationMonths(rs.getInt("duration_months"));
                loan.setEmiAmount(rs.getDouble("emi_amount"));
                loan.setStatus(rs.getString("status"));
                loan.setAppliedDate(rs.getString("applied_date"));
                loan.setCustomerName(rs.getString("full_name"));
                loan.setLoanTypeName(rs.getString("type_name"));
                list.add(loan);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
}