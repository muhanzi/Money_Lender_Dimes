package com.Dimes.Models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String borrowerName; // borrower's name
    private String phoneNumber;
    private String nationalIdNumber;
    private double loanAmount;
    private String periodType;
    private int loanPeriod;
    private double deposit;
    private Date date_issued;
    private String status;

    @ManyToOne
    @JoinColumn(name = "lender")
    private Lender lender;

    public Loan() {
    }

    public Loan(String borrowerName, String phoneNumber, String nationalIdNumber, double loanAmount, String periodType,
            int loanPeriod, double deposit, Date date_issued, String status, int lender) {
        this.borrowerName = borrowerName;
        this.phoneNumber = phoneNumber;
        this.nationalIdNumber = nationalIdNumber;
        this.loanAmount = loanAmount;
        this.periodType = periodType;
        this.loanPeriod = loanPeriod;
        this.deposit = deposit;
        this.date_issued = date_issued;
        this.status = status;
        this.lender = new Lender(0, "", 0.0, 0.0, 0.0, "", "", "", "");
    }

    public Date getDate_issued() {
        return date_issued;
    }

    public void setDate_issued(Date date_issued) {
        this.date_issued = date_issued; // lebon changed this
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Lender getLender() {
        return lender;
    }

    public void setLender(Lender lenderId) {
        this.lender = lenderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}
