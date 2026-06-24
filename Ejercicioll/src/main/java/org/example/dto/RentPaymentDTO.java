package org.example.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.example.models.RentalContract;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentPaymentDTO {
    private Long paymentId;
    private RentalContract contract;
    private LocalDate payDate;
    private BigDecimal amount;

    public RentPaymentDTO(){}

    public RentPaymentDTO(Long paymentId, RentalContract contract, LocalDate payDate, BigDecimal amount) {
        this.paymentId = paymentId;
        this.contract = contract;
        this.payDate = payDate;
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public RentalContract getContract() {
        return contract;
    }

    public void setContract(RentalContract contract) {
        this.contract = contract;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
