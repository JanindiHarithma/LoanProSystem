package model;

public class Customer {
    private int customerId;
    private String nicNo;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private double monthlyIncome;

    // Constructor
    public Customer(int customerId, String nicNo, String fullName, 
                    String phone, String email, String address, double monthlyIncome) {
        this.customerId = customerId;
        this.nicNo = nicNo;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.monthlyIncome = monthlyIncome;
    }

    // Empty constructor
    public Customer() {}

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getNicNo() { return nicNo; }
    public void setNicNo(String nicNo) { this.nicNo = nicNo; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(double monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    @Override
    public String toString() { return fullName; }
}
