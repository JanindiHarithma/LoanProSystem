package ui;

import dao.LoanDAO;
import model.Loan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoanApprovalForm extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea txtNotes;
    private LoanDAO loanDAO = new LoanDAO();
    private int selectedLoanId = -1;

    public LoanApprovalForm() {
        setTitle("Loan Approval");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadPendingLoans();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel lblTitle = new JLabel("LOAN APPROVAL MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(31, 56, 100));
        lblTitle.setBounds(250, 15, 400, 30);
        add(lblTitle);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{"Loan ID", "Customer", "Type", 
                        "Amount (Rs)", "Duration", "EMI (Rs)", "Date"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> selectLoan());
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 60, 800, 280);
        add(scroll);

        // Notes
        JLabel lblNotes = new JLabel("Officer Notes (required for rejection):");
        lblNotes.setFont(new Font("Arial", Font.BOLD, 12));
        lblNotes.setBounds(20, 355, 300, 25);
        add(lblNotes);

        txtNotes = new JTextArea();
        txtNotes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane notesScroll = new JScrollPane(txtNotes);
        notesScroll.setBounds(20, 380, 500, 80);
        add(notesScroll);

        // Buttons
        JButton btnApprove = new JButton("✅ APPROVE");
        btnApprove.setBounds(560, 390, 130, 40);
        btnApprove.setBackground(new Color(55, 86, 35));
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFont(new Font("Arial", Font.BOLD, 13));
        btnApprove.addActionListener(e -> approveLoan());
        add(btnApprove);

        JButton btnReject = new JButton("❌ REJECT");
        btnReject.setBounds(700, 390, 130, 40);
        btnReject.setBackground(new Color(192, 0, 0));
        btnReject.setForeground(Color.WHITE);
        btnReject.setFont(new Font("Arial", Font.BOLD, 13));
        btnReject.addActionListener(e -> rejectLoan());
        add(btnReject);

        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setBounds(20, 475, 120, 35);
        btnRefresh.setBackground(new Color(46, 117, 182));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadPendingLoans());
        add(btnRefresh);
    }

    private void loadPendingLoans() {
        tableModel.setRowCount(0);
        List<Loan> loans = loanDAO.getPendingLoans();
        for (Loan l : loans) {
            tableModel.addRow(new Object[]{
                l.getLoanId(), l.getCustomerName(), l.getLoanTypeName(),
                String.format("%.2f", l.getLoanAmount()),
                l.getDurationMonths() + " months",
                String.format("%.2f", l.getEmiAmount()),
                l.getAppliedDate()
            });
        }
        if (loans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending loans found!");
        }
    }

    private void selectLoan() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedLoanId = (int) tableModel.getValueAt(row, 0);
        }
    }

    private void approveLoan() {
        if (selectedLoanId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan first!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Approve Loan #" + selectedLoanId + "?");
        if (confirm == 0) {
            if (loanDAO.approveLoan(selectedLoanId, txtNotes.getText())) {
                JOptionPane.showMessageDialog(this, "Loan APPROVED successfully!");
                txtNotes.setText("");
                selectedLoanId = -1;
                loadPendingLoans();
            }
        }
    }

    private void rejectLoan() {
        if (selectedLoanId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a loan first!");
            return;
        }
        if (txtNotes.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter rejection reason in notes!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reject Loan #" + selectedLoanId + "?");
        if (confirm == 0) {
            if (loanDAO.rejectLoan(selectedLoanId, txtNotes.getText())) {
                JOptionPane.showMessageDialog(this, "Loan REJECTED!");
                txtNotes.setText("");
                selectedLoanId = -1;
                loadPendingLoans();
            }
        }
    }
}