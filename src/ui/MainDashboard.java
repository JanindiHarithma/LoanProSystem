package ui;

import dao.CustomerDAO;
import dao.LoanDAO;
import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    private LoanDAO loanDAO = new LoanDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private int totalCustomers, pendingLoans, approvedLoans;

    public MainDashboard() {
        setTitle("LoanPro - Loan Management System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        totalCustomers = customerDAO.getAllCustomers().size();
        pendingLoans = loanDAO.getPendingLoans().size();
        approvedLoans = loanDAO.getApprovedLoans().size();
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(31, 56, 100));

        // Title
        JLabel t = new JLabel("LoanPro - Loan Management System");
        t.setFont(new Font("Arial", Font.BOLD, 22));
        t.setForeground(Color.WHITE);
        t.setBounds(30, 15, 600, 35);
        add(t);

        // Cards
        addCard("Customers", totalCustomers, new Color(46,117,182), 30, 70);
        addCard("Pending", pendingLoans, new Color(192,80,77), 200, 70);
        addCard("Approved", approvedLoans, new Color(55,86,35), 370, 70);
        addCard("Loan Types", 4, new Color(119,36,50), 540, 70);

        // PIE CHART
        JPanel pie = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.setColor(new Color(31,56,100));
                g2.drawString("Loan Status - Pie Chart", 50, 20);

                int total = approvedLoans + pendingLoans + 1;
                int[] vals = {approvedLoans, pendingLoans, 1};
                Color[] cols = {new Color(55,86,35),
                    new Color(192,80,77), new Color(119,36,50)};
                String[] lbls = {"Approved","Pending","Rejected"};

                int start = 0;
                for (int i = 0; i < vals.length; i++) {
                    int angle = i == vals.length-1
                        ? 360-start
                        : (int)(360.0*vals[i]/total);
                    g2.setColor(cols[i]);
                    g2.fillArc(50, 30, 160, 160, start, angle);
                    g2.setColor(Color.WHITE);
                    g2.drawArc(50, 30, 160, 160, start, angle);
                    start += angle;
                    // Legend
                    g2.setColor(cols[i]);
                    g2.fillRect(20, 205+(i*20), 12, 12);
                    g2.setColor(Color.BLACK);
                    g2.setFont(new Font("Arial", Font.PLAIN, 11));
                    g2.drawString(lbls[i]+" ("+vals[i]+")",
                        36, 216+(i*20));
                }
            }
        };
        pie.setBounds(30, 190, 280, 270);
        pie.setBackground(Color.WHITE);
        pie.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(pie);

        // BAR CHART
        JPanel bar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.setColor(new Color(31,56,100));
                g2.drawString("Loan Overview - Bar Chart", 60, 20);

                String[] cats = {"Customers","Pending","Approved","Types"};
                int[] vals = {totalCustomers,pendingLoans,approvedLoans,4};
                Color[] cols = {new Color(46,117,182),new Color(192,80,77),
                    new Color(55,86,35),new Color(119,36,50)};

                int max = 1;
                for (int v : vals) if (v > max) max = v;

                for (int i = 0; i < vals.length; i++) {
                    int bh = max > 0
                        ? Math.max(5,(int)(180.0*vals[i]/max)) : 5;
                    int x = 30 + i*65;
                    int y = 220 - bh;
                    g2.setColor(cols[i]);
                    g2.fillRoundRect(x, y, 45, bh, 8, 8);
                    g2.setColor(new Color(31,56,100));
                    g2.setFont(new Font("Arial",Font.BOLD,12));
                    g2.drawString(String.valueOf(vals[i]),
                        x+15, y-5);
                    g2.setColor(Color.DARK_GRAY);
                    g2.setFont(new Font("Arial",Font.PLAIN,10));
                    g2.drawString(cats[i], x+2, 240);
                }
                // X axis line
                g2.setColor(Color.GRAY);
                g2.drawLine(20, 220, 290, 220);
            }
        };
        bar.setBounds(330, 190, 310, 270);
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(bar);

        // Menu Buttons
        JLabel m = new JLabel("MAIN MENU");
        m.setFont(new Font("Arial",Font.BOLD,14));
        m.setForeground(Color.WHITE);
        m.setBounds(670, 190, 200, 25);
        add(m);

        addBtn("👤 Customer Management", 670, 220,
            new Color(46,117,182),
            e -> new CustomerForm().setVisible(true));
        addBtn("📋 Loan Application", 670, 300,
            new Color(46,117,182),
            e -> new LoanApplicationForm().setVisible(true));
        addBtn("✅ Loan Approval", 670, 380,
            new Color(192,80,77),
            e -> new LoanApprovalForm().setVisible(true));
        addBtn("💰 Repayment", 860, 220,
            new Color(55,86,35),
            e -> new RepaymentForm().setVisible(true));
        addBtn("📊 Reports", 860, 300,
            new Color(119,36,50),
            e -> new ReportForm().setVisible(true));
        addBtn("🚪 Exit", 860, 380,
            new Color(80,80,80), e -> System.exit(0));

        // Footer
        JLabel f = new JLabel("LoanPro v1.0 | NIBM 2026 | EAD Coursework");
        f.setFont(new Font("Arial",Font.PLAIN,11));
        f.setForeground(new Color(173,216,230));
        f.setBounds(30, 590, 500, 20);
        add(f);
    }

    private void addCard(String title, int value,
            Color color, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 155, 95);
        card.setBackground(color);
        JLabel v = new JLabel(String.valueOf(value));
        v.setFont(new Font("Arial",Font.BOLD,34));
        v.setForeground(Color.WHITE);
        v.setBounds(15,8,130,45);
        card.add(v);
        JLabel tl = new JLabel(title);
        tl.setFont(new Font("Arial",Font.PLAIN,12));
        tl.setForeground(new Color(220,220,220));
        tl.setBounds(15,55,130,25);
        card.add(tl);
        add(card);
    }

    private void addBtn(String text, int x, int y, Color color,
            java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 175, 65);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial",Font.BOLD,11));
        btn.setFocusPainted(false);
        btn.addActionListener(action);
        add(btn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new MainDashboard().setVisible(true));
    }
}
