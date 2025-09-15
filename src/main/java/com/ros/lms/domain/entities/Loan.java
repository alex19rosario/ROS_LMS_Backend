package com.ros.lms.domain.entities;

import jakarta.persistence.*;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name="LOANS")
public class Loan extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOAN_ID")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @Column(name = "LOAN_DATE")
    private LocalDateTime loanDate;

    @Column(name = "DUE_DATE")
    private LocalDateTime dueDate;

    @Column(name = "RETURN_DATE")
    private LocalDateTime returnDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STATUS_ID")
    private LoanStatus status;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STAFF_ID", nullable = false)
    private Staff staff;


    public Loan() {}

    public Loan(Member member, Book book, LoanStatus status, Staff staff) {
        this.member = member;
        this.book = book;
        this.loanDate = LocalDateTime.now();
        this.dueDate = this.loanDate.plusDays(3);
        this.status = status;
        this.staff = staff;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", member=" + member +
                ", book=" + book +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                ", staff=" + staff +
                '}';
    }
}
