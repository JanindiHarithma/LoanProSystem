package dao;

import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Save new customer
    public boolean saveCustomer(Customer c) {
        String sql = "INSERT INTO customer (nic_no, full_name, phone, email, address, monthly_income) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNicNo());
            ps.setString(2, c.getFullName());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getAddress());
            ps.setDouble(6, c.getMonthlyIncome());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
            return false;
        }
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setNicNo(rs.getString("nic_no"));
                c.setFullName(rs.getString("full_name"));
                c.setPhone(rs.getString("phone"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("address"));
                c.setMonthlyIncome(rs.getDouble("monthly_income"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error getting customers: " + e.getMessage());
        }
        return list;
    }

    // Update customer
    public boolean updateCustomer(Customer c) {
        String sql = "UPDATE customer SET nic_no=?, full_name=?, phone=?, email=?, address=?, monthly_income=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNicNo());
            ps.setString(2, c.getFullName());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getAddress());
            ps.setDouble(6, c.getMonthlyIncome());
            ps.setInt(7, c.getCustomerId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    // Delete customer
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
}