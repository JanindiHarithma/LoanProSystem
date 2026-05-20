package ui;

import dao.RepaymentDAO;
import dao.RepaymentDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RepaymentForm extends JFrame {

    private JComboBox<String> cmbLoan;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblEMI, lblTotal, lblPaid, lblBalance;
    private final RepaymentDAO repaymentDAO = new RepaymentDAO();
    private List<Object[]> loanList;
    private int selectedRepaymentId = -1;
    private double emiAmount = 0;

    public RepaymentForm() {
        setTitle("Repayment Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadLoans();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title
        JLabel lblTitle = new JLabel("LOAN REPAYMENT MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(31, 56, 100));
        lblTitle.setBounds(260, 15, 450, 30);
        add(lblTitle);

        // Loan selector
        JLabel lblLoan = new JLabel("Select Loan:");
        lblLoan.setFont(new Font("Arial", Font.BOLD, 12));
        lblLoan.setBounds(20, 60, 100, 25);
        add(lblLoan);

        cmbLoan = new JComboBox<>();
        cmbLoan.setBounds(130, 60, 350, 28);
        cmbLoan.addActionListener(e -> loadSchedule());
        add(cmbLoan);

        JButton btnGenerate = new JButton("Generate Schedule");
        btnGenerate.setBounds(495, 60, 160, 28);
        btnGenerate.setBackground(new Color(46, 117, 182));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 11));
        btnGenerate.addActionListener(e -> generateSchedule());
        add(btnGenerate);

        // Summary cards
        addInfoCard("Monthly EMI", "Rs 0.00", new Color(31, 56, 100), 20, 105);
        addInfoCard("Total Payable", "Rs 0.00", new Color(119, 36, 50), 230, 105);
        addInfoCard("Amount Paid", "Rs 0.00", new Color(55, 86, 35), 440, 105);
        addInfoCard("Balance", "Rs 0.00", new Color(192, 80, 77), 650, 105);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Due Date", "Amount Due",
                        "Amount Paid", "Status", "Penalty"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> selectRow());

        // Color rows by status
        table.setDefaultRenderer(Object.class,
            new javax.swing.table.DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                    t, val, sel, foc, row, col);
                String status = t.getValueAt(row, 4).toString();
                if (!sel) {
                    if ("PAID".equals(status))
                        c.setBackground(new Color(198, 239, 206));
                    else if ("OVERDUE".equals(status))
                        c.setBackground(new Color(255, 199, 206));
                    else
                        c.setBackground(new Color(255, 235, 156));
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 185, 850, 300);
        add(scroll);

        // Pay button
        JButton btnPay = new JButton("✅ Mark as PAID");
        btnPay.setBounds(20, 500, 180, 40);
        btnPay.setBackground(new Color(55, 86, 35));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Arial", Font.BOLD, 13));
        btnPay.addActionListener(e -> markAsPaid());
        add(btnPay);

        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setBounds(215, 500, 130, 40);
        btnRefresh.setBackground(new Color(46, 117, 182));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 13));
        btnRefresh.addActionListener(e -> loadSchedule());
        add(btnRefresh);

        // Legend
        JLabel legend = new JLabel(
            "🟢 Paid   🟡 Pending   🔴 Overdue");
        legend.setFont(new Font("Arial", Font.PLAIN, 12));
        legend.setBounds(500, 510, 300, 25);
        add(legend);
    }

    private JLabel addInfoCard(String title, String value,
                                Color color, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 200, 70);
        card.setBackground(color);

        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("Arial", Font.BOLD, 14));
        lblVal.setForeground(Color.WHITE);
        lblVal.setBounds(10, 8, 180, 25);
        card.add(lblVal);

        JLabel lblTit = new JLabel(title);
        lblTit.setFont(new Font("Arial", Font.PLAIN, 11));
        lblTit.setForeground(new Color(220, 220, 220));
        lblTit.setBounds(10, 38, 180, 20);
        card.add(lblTit);

        add(card);

        if ("Monthly EMI".equals(title)) lblEMI = lblVal;
        else if ("Total Payable".equals(title)) lblTotal = lblVal;
        else if ("Amount Paid".equals(title)) lblPaid = lblVal;
        else if ("Balance".equals(title)) lblBalance = lblVal;

        return lblVal;
    }

    private void loadLoans() {
        cmbLoan.removeAllItems();
        loanList = repaymentDAO.getApprovedLoansForDropdown();
        if (loanList.isEmpty()) {
            cmbLoan.addItem("No approved loans found");
            return;
        }
        for (Object[] loan : loanList) {
            cmbLoan.addItem("Loan #" + loan[0] + " - " + loan[1]
                + " | Rs " + loan[2]);
        }
    }

    private void generateSchedule() {
        int idx = cmbLoan.getSelectedIndex();
        if (idx < 0 || loanList == null || loanList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a loan!");
            return;
        }
        Object[] loan = loanList.get(idx);
        int loanId = (int) loan[0];
        double emi = (double) loan[3];
        int months = (int) loan[4];

        int confirm = JOptionPane.showConfirmDialog(this,
            "Generate " + months + " month repayment schedule?");
        if (confirm == 0) {
            if (repaymentDAO.generateSchedule(loanId, emi, months)) {
                JOptionPane.showMessageDialog(this,
                    "Schedule generated successfully!");
                loadSchedule();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Schedule already exists or error occurred!");
            }
        }
    }

    private void loadSchedule() {
        tableModel.setRowCount(0);
        int idx = cmbLoan.getSelectedIndex();
        if (idx < 0 || loanList == null || loanList.isEmpty()) return;

        Object[] loan = loanList.get(idx);
        int loanId = (int) loan[0];
        emiAmount = (double) loan[3];
        int months = (int) loan[4];

        List<Object[]> schedule = repaymentDAO.getSchedule(loanId);
        double totalPaid = 0;

        for (Object[] row : schedule) {
            tableModel.addRow(new Object[]{
                row[0], row[1],
                String.format("Rs %.2f", row[2]),
                String.format("Rs %.2f", row[3]),
                row[4],
                String.format("Rs %.2f", row[5])
            });
            if ("PAID".equals(row[4])) {
                totalPaid += (double) row[3];
            }
        }

        double totalPayable = emiAmount * months;
        double balance = totalPayable - totalPaid;

        if (lblEMI != null) lblEMI.setText(
            String.format("Rs %.2f", emiAmount));
        if (lblTotal != null) lblTotal.setText(
            String.format("Rs %.2f", totalPayable));
        if (lblPaid != null) lblPaid.setText(
            String.format("Rs %.2f", totalPaid));
        if (lblBalance != null) lblBalance.setText(
            String.format("Rs %.2f", balance));
    }

    private void selectRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedRepaymentId = (int) tableModel.getValueAt(row, 0);
        }
    }

    private void markAsPaid() {
        if (selectedRepaymentId == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an installment first!");
            return;
        }
        String status = tableModel.getValueAt(
            table.getSelectedRow(), 4).toString();
        if ("PAID".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This installment is already paid!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark installment as PAID?\nAmount: Rs "
            + String.format("%.2f", emiAmount));
        if (confirm == 0) {
            if (repaymentDAO.markAsPaid(selectedRepaymentId, emiAmount)) {
                JOptionPane.showMessageDialog(this,
                    "Payment recorded successfully!");
                selectedRepaymentId = -1;
                loadSchedule();
            }
        }
    }
}
