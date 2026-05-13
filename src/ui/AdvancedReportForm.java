package ui;

import dao.OverdueReportDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdvancedReportForm extends JFrame {

    private JTabbedPane tabbedPane;
    private JTable overdueTable, portfolioTable;
    private DefaultTableModel overdueModel, portfolioModel;
    private OverdueReportDAO reportDAO = new OverdueReportDAO();

    // Summary labels
    private JLabel lblTotalOverdue, lblTotalPenalty;
    private JLabel lblTotalLoans, lblTotalAmount;

    public AdvancedReportForm() {
        setTitle("Advanced Reports - LoanPro");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadAllReports();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title panel
        JPanel titlePanel = new JPanel(null);
        titlePanel.setBackground(new Color(31, 56, 100));
        titlePanel.setPreferredSize(new Dimension(950, 60));

        JLabel lblTitle = new JLabel("ADVANCED MANAGEMENT REPORTS");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 15, 500, 30);
        titlePanel.add(lblTitle);

        JButton btnRefresh = new JButton("🔄 Refresh All");
        btnRefresh.setBounds(780, 15, 140, 30);
        btnRefresh.setBackground(new Color(46, 117, 182));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadAllReports());
        titlePanel.add(btnRefresh);

        add(titlePanel, BorderLayout.NORTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));

        // Tab 1 - Overdue Report
        tabbedPane.addTab("⚠ Overdue Payments Report",
            createOverduePanel());

        // Tab 2 - Portfolio Report
        tabbedPane.addTab("📊 Loan Portfolio Report",
            createPortfolioPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createOverduePanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        // Summary cards
        JPanel card1 = createCard("Total Overdue", "0",
            new Color(192, 80, 77), 20, 15);
        panel.add(card1);

        JPanel card2 = createCard("Total Penalty", "Rs 0.00",
            new Color(119, 36, 50), 240, 15);
        panel.add(card2);

        // Description
        JLabel lblDesc = new JLabel(
            "Report: Customers with overdue installments " +
            "(Tables used: repayment + loan + customer + loan_type)");
        lblDesc.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDesc.setForeground(new Color(80, 80, 80));
        lblDesc.setBounds(20, 100, 800, 20);
        panel.add(lblDesc);

        // Table
        overdueModel = new DefaultTableModel(
            new String[]{"Customer", "Phone", "Email",
                        "Loan ID", "Loan Type", "Amount (Rs)",
                        "Due Date", "EMI Due", "Penalty",
                        "Days Overdue"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        overdueTable = new JTable(overdueModel);
        overdueTable.setRowHeight(25);
        overdueTable.setBackground(new Color(255, 235, 156));

        JScrollPane scroll = new JScrollPane(overdueTable);
        scroll.setBounds(20, 125, 890, 340);
        panel.add(scroll);

        JLabel lblNote = new JLabel(
            "⚠ Contact these customers immediately for payment recovery");
        lblNote.setFont(new Font("Arial", Font.BOLD, 12));
        lblNote.setForeground(new Color(192, 0, 0));
        lblNote.setBounds(20, 475, 600, 25);
        panel.add(lblNote);

        return panel;
    }

    private JPanel createPortfolioPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 248, 255));

        // Summary cards
        JPanel card3 = createCard("Total Loan Types", "0",
            new Color(31, 56, 100), 20, 15);
        panel.add(card3);

        JPanel card4 = createCard("Total Disbursed", "Rs 0.00",
            new Color(55, 86, 35), 240, 15);
        panel.add(card4);

        // Description
        JLabel lblDesc = new JLabel(
            "Report: Loan portfolio summary by type " +
            "(Tables used: loan + loan_type + customer)");
        lblDesc.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDesc.setForeground(new Color(80, 80, 80));
        lblDesc.setBounds(20, 100, 800, 20);
        panel.add(lblDesc);

        // Table
        portfolioModel = new DefaultTableModel(
            new String[]{"Loan Type", "Total Loans",
                        "Total Amount (Rs)", "Average Amount (Rs)",
                        "Total Repayable (Rs)"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        portfolioTable = new JTable(portfolioModel);
        portfolioTable.setRowHeight(30);
        portfolioTable.setFont(new Font("Arial", Font.PLAIN, 13));

        // Color alternate rows
        portfolioTable.setDefaultRenderer(Object.class,
            new javax.swing.table.DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel,
                    boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0
                        ? new Color(235, 245, 251)
                        : Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(portfolioTable);
        scroll.setBounds(20, 125, 890, 300);
        panel.add(scroll);

        JLabel lblNote = new JLabel(
            "✅ Use this report to make loan portfolio decisions");
        lblNote.setFont(new Font("Arial", Font.BOLD, 12));
        lblNote.setForeground(new Color(55, 86, 35));
        lblNote.setBounds(20, 440, 500, 25);
        panel.add(lblNote);

        return panel;
    }

    private JPanel createCard(String title, String value,
                               Color color, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 200, 75);
        card.setBackground(color);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Arial", Font.BOLD, 20));
        lblVal.setForeground(Color.WHITE);
        lblVal.setBounds(15, 8, 170, 35);
        card.add(lblVal);

        JLabel lblTit = new JLabel(title);
        lblTit.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTit.setForeground(new Color(220, 220, 220));
        lblTit.setBounds(15, 48, 170, 20);
        card.add(lblTit);

        if ("Total Overdue".equals(title)) lblTotalOverdue = lblVal;
        else if ("Total Penalty".equals(title)) lblTotalPenalty = lblVal;
        else if ("Total Loan Types".equals(title)) lblTotalLoans = lblVal;
        else if ("Total Disbursed".equals(title)) lblTotalAmount = lblVal;

        return card;
    }

    private void loadAllReports() {
        loadOverdueReport();
        loadPortfolioReport();
    }

    private void loadOverdueReport() {
        overdueModel.setRowCount(0);
        List<Object[]> list = reportDAO.getOverdueReport();
        double totalPenalty = 0;

        for (Object[] row : list) {
            overdueModel.addRow(new Object[]{
                row[0], row[1], row[2],
                row[3],
                row[5],
                String.format("Rs %.2f", row[4]),
                row[6],
                String.format("Rs %.2f", row[7]),
                String.format("Rs %.2f", row[8]),
                row[9] + " days"
            });
            totalPenalty += (double) row[8];
        }

        if (lblTotalOverdue != null)
            lblTotalOverdue.setText(String.valueOf(list.size()));
        if (lblTotalPenalty != null)
            lblTotalPenalty.setText(
                String.format("Rs %.2f", totalPenalty));
    }

    private void loadPortfolioReport() {
        portfolioModel.setRowCount(0);
        List<Object[]> list = reportDAO.getLoanPortfolioReport();
        double totalDisbursed = 0;

        for (Object[] row : list) {
            portfolioModel.addRow(new Object[]{
                row[0],
                row[1],
                String.format("Rs %.2f", row[2]),
                String.format("Rs %.2f", row[3]),
                String.format("Rs %.2f", row[4])
            });
            totalDisbursed += (double) row[2];
        }

        if (lblTotalLoans != null)
            lblTotalLoans.setText(String.valueOf(list.size()));
        if (lblTotalAmount != null)
            lblTotalAmount.setText(
                String.format("Rs %.2f", totalDisbursed));
    }
}