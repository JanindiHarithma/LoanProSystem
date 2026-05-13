package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OverdueReportDAO {

    public List<Object[]> getOverdueReport() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.full_name, c.phone, c.email, "
                   + "l.loan_id, l.loan_amount, lt.type_name, "
                   + "r.due_date, r.amount_due, r.penalty, "
                   + "DATEDIFF(NOW(), r.due_date) AS days_overdue "
                   + "FROM repayment r "
                   + "JOIN loan l ON r.loan_id = l.loan_id "
                   + "JOIN customer c ON l.customer_id = c.customer_id "
                   + "JOIN loan_type lt ON l.type_id = lt.type_id "
                   + "WHERE r.status = 'OVERDUE' "
                   + "ORDER BY days_overdue DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getInt("loan_id"),
                    rs.getDouble("loan_amount"),
                    rs.getString("type_name"),
                    rs.getString("due_date"),
                    rs.getDouble("amount_due"),
                    rs.getDouble("penalty"),
                    rs.getInt("days_overdue")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }

    public List<Object[]> getLoanPortfolioReport() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT lt.type_name, "
                   + "COUNT(l.loan_id) AS total_loans, "
                   + "SUM(l.loan_amount) AS total_amount, "
                   + "AVG(l.loan_amount) AS avg_amount, "
                   + "SUM(l.emi_amount * l.duration_months) AS total_repayable "
                   + "FROM loan l "
                   + "JOIN loan_type lt ON l.type_id = lt.type_id "
                   + "JOIN customer c ON l.customer_id = c.customer_id "
                   + "WHERE l.status = 'APPROVED' "
                   + "GROUP BY lt.type_name "
                   + "ORDER BY total_amount DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("type_name"),
                    rs.getInt("total_loans"),
                    rs.getDouble("total_amount"),
                    rs.getDouble("avg_amount"),
                    rs.getDouble("total_repayable")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
}