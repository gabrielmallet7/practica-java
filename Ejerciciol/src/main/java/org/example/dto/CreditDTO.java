package org.example.dto;

import jakarta.persistence.*;
import org.example.models.CreditStatus;
import org.example.models.CreditType;
import org.example.models.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreditDTO {
    private Long id;
    private String clientName;
    private CreditType creditType;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private LocalDate start_date;
    private LocalDate dueDate;
    private CreditStatus status;

    public CreditDTO(){}

    public CreditDTO(Long id, String clientName, CreditType creditType, BigDecimal principalAmount, BigDecimal interestRate, LocalDate start_date, LocalDate dueDate, CreditStatus status) {
        this.id = id;
        this.clientName = clientName;
        this.creditType = creditType;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.start_date = start_date;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }
}
