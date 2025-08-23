package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.LoanStatuses;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "LOAN_STATUSES")
public class LoanStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATUS_ID")
    private long id;

    @Column(name = "CODE")
    private LoanStatuses code;

    @OneToMany(mappedBy = "status")
    private List<Loan> loans;

    public LoanStatus(){}

    public LoanStatus(LoanStatuses code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LoanStatuses getCode() {
        return code;
    }

    public void setCode(LoanStatuses code) {
        this.code = code;
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
                ", code='" + code.getVal() + '\'' +
                '}';
    }
}
