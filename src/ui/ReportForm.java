package ui;

import dao.LoanDAO;
import model.Loan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private LoanDAO loanDAO = new LoanDAO();
    private JLabel lblTotal, lblApproved, lblPending, lblRejected;

    public ReportForm() {
        setTitle("Loan Portfolio Report");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadReport();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title
        JLabel lblTitle = new JLabel("LOAN PORTFOLIO REPORT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(31, 56, 100));
        lblTitle.setBounds(280, 15, 400, 30);
        add(lblTitle);

        // Summary Cards
        addSummaryCard("Total Loans", "0", new Color(31, 56, 100), 20, 60);
        addSummaryCard("Approved", "0", new Color(55, 86, 35), 240, 60);
        addSummaryCard("Pending", "0", new Color(192, 80, 77), 460, 60);
        addSummaryCard("Rejected", "0", new Color(119, 36, 50), 680, 60);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{"Loan ID", "Customer", "Loan Type",
                        "Amount (Rs)", "Duration", "EMI (Rs)",
                        "Status", "Applied Date"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Color rows by status
        table.setDefaultRenderer(Object.class, 
            new javax.swing.table.DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                String status = (String) t.getValueAt(row, 6);
                if (!sel) {
                    if ("APPROVED".equals(status)) 
                        c.setBackground(new Color(198, 239, 206));
                    else if ("PENDING".equals(status)) 
                        c.setBackground(new Color(255, 235, 156));
                    else if ("REJECTED".equals(status)) 
                        c.setBackground(new Color(255, 199, 206));
                    else 
                        c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 175, 850, 320);
        add(scroll);

        // Buttons
        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setBounds(20, 510, 130, 35);
        btnRefresh.setBackground(new Color(46, 117, 182));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.addActionListener(e -> loadReport());
        add(btnRefresh);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(160, 510, 100, 35);
        btnClose.setBackground(new Color(128, 128, 128));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 12));
        btnClose.addActionListener(e -> dispose());
        add(btnClose);

        // Legend
        JLabel legend = new JLabel(
            "🟢 Approved   🟡 Pending   🔴 Rejected");
        legend.setFont(new Font("Arial", Font.PLAIN, 12));
        legend.setBounds(400, 515, 400, 25);
        add(legend);
    }

    private JLabel addSummaryCard(String title, String value, 
                                   Color color, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 190, 90);
        card.setBackground(color);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Arial", Font.BOLD, 30));
        lblVal.setForeground(Color.WHITE);
        lblVal.setBounds(15, 10, 160, 40);
        card.add(lblVal);

        JLabel lblTit = new JLabel(title);
        lblTit.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTit.setForeground(new Color(220, 220, 220));
        lblTit.setBounds(15, 55, 160, 20);
        card.add(lblTit);

        add(card);

        // Store reference to value label
        if ("Total Loans".equals(title)) lblTotal = lblVal;
        else if ("Approved".equals(title)) lblApproved = lblVal;
        else if ("Pending".equals(title)) lblPending = lblVal;
        else if ("Rejected".equals(title)) lblRejected = lblVal;

        return lblVal;
    }

    private void loadReport() {
        tableModel.setRowCount(0);
        List<Loan> loans = loanDAO.getAllLoansReport();

        int approved = 0, pending = 0, rejected = 0;

        for (Loan l : loans) {
            tableModel.addRow(new Object[]{
                l.getLoanId(),
                l.getCustomerName(),
                l.getLoanTypeName(),
                String.format("Rs %.2f", l.getLoanAmount()),
                l.getDurationMonths() + " months",
                String.format("Rs %.2f", l.getEmiAmount()),
                l.getStatus(),
                l.getAppliedDate()
            });
            if ("APPROVED".equals(l.getStatus())) approved++;
            else if ("PENDING".equals(l.getStatus())) pending++;
            else if ("REJECTED".equals(l.getStatus())) rejected++;
        }

        // Update summary cards
        if (lblTotal != null) lblTotal.setText(String.valueOf(loans.size()));
        if (lblApproved != null) lblApproved.setText(String.valueOf(approved));
        if (lblPending != null) lblPending.setText(String.valueOf(pending));
        if (lblRejected != null) lblRejected.setText(String.valueOf(rejected));
    }
}