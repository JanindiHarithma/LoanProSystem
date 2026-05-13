package ui;

import dao.CustomerDAO;
import model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerForm extends JFrame {

    private JTextField txtNic, txtName, txtPhone, txtEmail, txtIncome;
    private JTextArea txtAddress;
    private JTable table;
    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO = new CustomerDAO();
    private int selectedCustomerId = -1;

    public CustomerForm() {
        setTitle("Customer Management");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel lblTitle = new JLabel("CUSTOMER MANAGEMENT");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(31, 56, 100));
        lblTitle.setBounds(250, 15, 400, 30);
        add(lblTitle);

        addLabel("NIC No:", 30, 70);
        txtNic = addField(150, 70);
        addLabel("Full Name:", 30, 110);
        txtName = addField(150, 110);
        addLabel("Phone:", 30, 150);
        txtPhone = addField(150, 150);
        addLabel("Email:", 30, 190);
        txtEmail = addField(150, 190);

        addLabel("Address:", 30, 230);
        txtAddress = new JTextArea();
        txtAddress.setBounds(150, 230, 220, 60);
        txtAddress.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txtAddress);

        addLabel("Monthly Income:", 30, 305);
        txtIncome = addField(150, 305);

        JButton btnSave = makeButton("Save", 30, 350, new Color(46, 117, 182));
        btnSave.addActionListener(e -> saveCustomer());
        add(btnSave);

        JButton btnUpdate = makeButton("Update", 140, 350, new Color(55, 86, 35));
        btnUpdate.addActionListener(e -> updateCustomer());
        add(btnUpdate);

        JButton btnDelete = makeButton("Delete", 250, 350, new Color(192, 0, 0));
        btnDelete.addActionListener(e -> deleteCustomer());
        add(btnDelete);

        JButton btnClear = makeButton("Clear", 360, 350, new Color(128, 128, 128));
        btnClear.addActionListener(e -> clearFields());
        add(btnClear);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "NIC", "Name", "Phone", "Email", "Income"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getSelectionModel().addListSelectionListener(e -> selectRow());
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(480, 60, 350, 400);
        add(scroll);
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 120, 25);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        add(lbl);
        return lbl;
    }

    private JTextField addField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 220, 28);
        txt.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(txt);
        return txt;
    }

    private JButton makeButton(String text, int x, int y, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 100, 35);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        return btn;
    }

    private void saveCustomer() {
        if (txtNic.getText().isEmpty() || txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIC and Name are required!");
            return;
        }
        Customer c = new Customer();
        c.setNicNo(txtNic.getText());
        c.setFullName(txtName.getText());
        c.setPhone(txtPhone.getText());
        c.setEmail(txtEmail.getText());
        c.setAddress(txtAddress.getText());
        c.setMonthlyIncome(Double.parseDouble(
            txtIncome.getText().isEmpty() ? "0" : txtIncome.getText()));
        if (customerDAO.saveCustomer(c)) {
            JOptionPane.showMessageDialog(this, "Customer saved successfully!");
            clearFields();
            loadCustomers();
        } else {
            JOptionPane.showMessageDialog(this, "Error saving customer!");
        }
    }

    private void updateCustomer() {
        if (selectedCustomerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first!");
            return;
        }
        Customer c = new Customer();
        c.setCustomerId(selectedCustomerId);
        c.setNicNo(txtNic.getText());
        c.setFullName(txtName.getText());
        c.setPhone(txtPhone.getText());
        c.setEmail(txtEmail.getText());
        c.setAddress(txtAddress.getText());
        c.setMonthlyIncome(Double.parseDouble(
            txtIncome.getText().isEmpty() ? "0" : txtIncome.getText()));
        if (customerDAO.updateCustomer(c)) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully!");
            clearFields();
            loadCustomers();
        }
    }

    private void deleteCustomer() {
        if (selectedCustomerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer first!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this customer?");
        if (confirm == 0) {
            if (customerDAO.deleteCustomer(selectedCustomerId)) {
                JOptionPane.showMessageDialog(this, "Customer deleted!");
                clearFields();
                loadCustomers();
            }
        }
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> list = customerDAO.getAllCustomers();
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(), c.getNicNo(), c.getFullName(),
                c.getPhone(), c.getEmail(), c.getMonthlyIncome()
            });
        }
    }

    private void selectRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedCustomerId = (int) tableModel.getValueAt(row, 0);
            txtNic.setText(tableModel.getValueAt(row, 1).toString());
            txtName.setText(tableModel.getValueAt(row, 2).toString());
            txtPhone.setText(tableModel.getValueAt(row, 3).toString());
            txtEmail.setText(tableModel.getValueAt(row, 4).toString());
            txtIncome.setText(tableModel.getValueAt(row, 5).toString());
        }
    }

    private void clearFields() {
        txtNic.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtIncome.setText("");
        selectedCustomerId = -1;
        table.clearSelection();
    }
}