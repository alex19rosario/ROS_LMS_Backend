package com.ros.lms.domain.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "LOAN_STATUSES")
public class LoanStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATUS_ID")
    private long id;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "status")
    private List<Loan> loans;

    public LoanStatus(){}

    public LoanStatus(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    @Override
    public String toString() {
        return "LoanStatus{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
