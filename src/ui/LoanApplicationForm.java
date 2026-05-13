package ui;

import dao.CustomerDAO;
import dao.LoanDAO;
import model.Customer;
import model.Loan;
import model.LoanType;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoanApplicationForm extends JFrame {

    private JComboBox<Customer> cmbCustomer;
    private JComboBox<LoanType> cmbLoanType;
    private JTextField txtAmount, txtDuration, txtEMI;
    private CustomerDAO customerDAO = new CustomerDAO();
    private LoanDAO loanDAO = new LoanDAO();

    public LoanApplicationForm() {
        setTitle("Loan Application");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel lblTitle = new JLabel("NEW LOAN APPLICATION");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(31, 56, 100));
        lblTitle.setBounds(120, 20, 300, 30);
        add(lblTitle);

        // Customer
        addLabel("Customer:", 30, 75);
        cmbCustomer = new JComboBox<>();
        cmbCustomer.setBounds(160, 75, 280, 28);
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer c : customers) cmbCustomer.addItem(c);
        add(cmbCustomer);

        // Loan Type
        addLabel("Loan Type:", 30, 120);
        cmbLoanType = new JComboBox<>();
        cmbLoanType.setBounds(160, 120, 280, 28);
        List<LoanType> types = loanDAO.getAllLoanTypes();
        for (LoanType lt : types) cmbLoanType.addItem(lt);
        cmbLoanType.addActionListener(e -> updateEMI());
        add(cmbLoanType);

        // Amount
        addLabel("Loan Amount (Rs):", 30, 165);
        txtAmount = new JTextField();
        txtAmount.setBounds(160, 165, 280, 28);
        txtAmount.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updateEMI(); }
        });
        add(txtAmount);

        // Duration
        addLabel("Duration (Months):", 30, 210);
        txtDuration = new JTextField();
        txtDuration.setBounds(160, 210, 280, 28);
        txtDuration.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDuration.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { updateEMI(); }
        });
        add(txtDuration);

        // EMI
        addLabel("Monthly EMI (Rs):", 30, 255);
        txtEMI = new JTextField("0.00");
        txtEMI.setBounds(160, 255, 280, 28);
        txtEMI.setEditable(false);
        txtEMI.setBackground(new Color(200, 230, 200));
        txtEMI.setFont(new Font("Arial", Font.BOLD, 13));
        txtEMI.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtEMI);

        // Buttons
        JButton btnApply = new JButton("Apply for Loan");
        btnApply.setBounds(80, 320, 160, 40);
        btnApply.setBackground(new Color(46, 117, 182));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFont(new Font("Arial", Font.BOLD, 13));
        btnApply.addActionListener(e -> applyLoan());
        add(btnApply);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(260, 320, 120, 40);
        btnCancel.setBackground(new Color(128, 128, 128));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancel.addActionListener(e -> dispose());
        add(btnCancel);
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 150, 25);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(lbl);
    }

    private void updateEMI() {
        try {
            double amount = Double.parseDouble(txtAmount.getText());
            int months = Integer.parseInt(txtDuration.getText());
            LoanType lt = (LoanType) cmbLoanType.getSelectedItem();
            if (lt != null && months > 0) {
                double r = lt.getInterestRate() / 12 / 100;
                double emi = (amount * r * Math.pow(1+r, months)) 
                           / (Math.pow(1+r, months) - 1);
                txtEMI.setText(String.format("%.2f", emi));
            }
        } catch (NumberFormatException ex) {
            txtEMI.setText("0.00");
        }
    }

    private void applyLoan() {
        if (txtAmount.getText().isEmpty() || txtDuration.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        Customer c = (Customer) cmbCustomer.getSelectedItem();
        LoanType lt = (LoanType) cmbLoanType.getSelectedItem();
        if (c == null || lt == null) {
            JOptionPane.showMessageDialog(this, "Please select customer and loan type!");
            return;
        }
        Loan loan = new Loan();
        loan.setCustomerId(c.getCustomerId());
        loan.setTypeId(lt.getTypeId());
        loan.setLoanAmount(Double.parseDouble(txtAmount.getText()));
        loan.setDurationMonths(Integer.parseInt(txtDuration.getText()));
        loan.setEmiAmount(Double.parseDouble(txtEMI.getText()));

        if (loanDAO.applyLoan(loan)) {
            JOptionPane.showMessageDialog(this, 
                "Loan application submitted!\nEMI: Rs " + txtEMI.getText());
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error submitting application!");
        }
    }
}