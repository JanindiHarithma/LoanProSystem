package ui;

import dao.CustomerDAO;
import dao.LoanDAO;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    private LoanDAO loanDAO = new LoanDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public MainDashboard() {
        setTitle("LoanPro - Loan Management System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(31, 56, 100));

        // Title Bar
        JLabel lblTitle = new JLabel("🏦  LoanPro - Loan Management System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(30, 20, 600, 35);
        add(lblTitle);

        JLabel lblSub = new JLabel("Banking & Finance Management");
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(new Color(173, 216, 230));
        lblSub.setBounds(30, 55, 400, 20);
        add(lblSub);

        // Summary Cards
        addCard("Total Customers", 
            String.valueOf(customerDAO.getAllCustomers().size()),
            new Color(46, 117, 182), 30, 100);

        addCard("Pending Loans",
            String.valueOf(loanDAO.getPendingLoans().size()),
            new Color(192, 80, 77), 250, 100);

        addCard("Approved Loans",
            String.valueOf(loanDAO.getApprovedLoans().size()),
            new Color(55, 86, 35), 470, 100);

        addCard("Loan Types", "4",
            new Color(119, 36, 50), 690, 100);

        // Menu Buttons
        JLabel lblMenu = new JLabel("MAIN MENU");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 16));
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setBounds(30, 240, 200, 25);
        add(lblMenu);

        // Row 1 buttons
        addMenuButton("👤  Customer Management", 30, 280,
            new Color(46, 117, 182), e -> {
                new CustomerForm().setVisible(true);
            });

        addMenuButton("📋  Loan Application", 250, 280,
            new Color(46, 117, 182), e -> {
                new LoanApplicationForm().setVisible(true);
            });

        addMenuButton("✅  Loan Approval", 470, 280,
            new Color(192, 80, 77), e -> {
                new LoanApprovalForm().setVisible(true);
            });

        // Row 2 buttons
        addMenuButton("💰  Repayment", 30, 370,
            new Color(55, 86, 35), e -> {
               new RepaymentForm().setVisible(true);
            });
        addMenuButton("📊  Reports", 250, 370,
            new Color(119, 36, 50), e -> {
                new ReportForm().setVisible(true);
            });

        addMenuButton("🚪  Exit", 470, 370,
            new Color(80, 80, 80), e -> System.exit(0));

        // Footer
        JLabel lblFooter = new JLabel(
            "LoanPro v1.0  |  Enterprise Application Development  |  NIBM 2026");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(173, 216, 230));
        lblFooter.setBounds(30, 530, 600, 20);
        add(lblFooter);
    }

    private void addCard(String title, String value, Color color, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 200, 110);
        card.setBackground(color);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 36));
        lblValue.setForeground(Color.WHITE);
        lblValue.setBounds(20, 15, 160, 50);
        card.add(lblValue);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(220, 220, 220));
        lblTitle.setBounds(20, 65, 160, 25);
        card.add(lblTitle);

        add(card);
    }

    private void addMenuButton(String text, int x, int y, Color color,
            java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 200, 70);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.addActionListener(action);
        add(btn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboard().setVisible(true);
        });
    }
}