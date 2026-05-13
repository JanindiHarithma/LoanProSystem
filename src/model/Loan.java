package model;

public class Loan {
    private int loanId;
    private int customerId;
    private int typeId;
    private double loanAmount;
    private int durationMonths;
    private double emiAmount;
    private String status;
    private String appliedDate;
    private String approvedDate;
    private String officerNotes;
    private String customerName;
    private String loanTypeName;

    public Loan() {}

    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }

    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }

    public double getEmiAmount() { return emiAmount; }
    public void setEmiAmount(double emiAmount) { this.emiAmount = emiAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }

    public String getApprovedDate() { return approvedDate; }
    public void setApprovedDate(String approvedDate) { this.approvedDate = approvedDate; }

    public String getOfficerNotes() { return officerNotes; }
    public void setOfficerNotes(String officerNotes) { this.officerNotes = officerNotes; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getLoanTypeName() { return loanTypeName; }
    public void setLoanTypeName(String loanTypeName) { this.loanTypeName = loanTypeName; }

    @Override
    public String toString() { return "Loan #" + loanId + " - " + customerName; }
}