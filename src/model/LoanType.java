package model;

public class LoanType {
    private int typeId;
    private String typeName;
    private double interestRate;
    private double maxAmount;
    private int maxDurationMonths;

    public LoanType() {}

    public LoanType(int typeId, String typeName, double interestRate, 
                    double maxAmount, int maxDurationMonths) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.interestRate = interestRate;
        this.maxAmount = maxAmount;
        this.maxDurationMonths = maxDurationMonths;
    }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public double getMaxAmount() { return maxAmount; }
    public void setMaxAmount(double maxAmount) { this.maxAmount = maxAmount; }

    public int getMaxDurationMonths() { return maxDurationMonths; }
    public void setMaxDurationMonths(int maxDurationMonths) { 
        this.maxDurationMonths = maxDurationMonths; 
    }

    @Override
    public String toString() { return typeName; }
}