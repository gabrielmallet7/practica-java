package org.app.dtos;

import org.app.models.DonationStatus;
import org.app.models.DonorType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DonationDTO{
    Long donationId;
    String donorName;
    DonorType donorType;
    BigDecimal amount;
    LocalDate donationDate;
    String category;
    DonationStatus status;

    public DonationDTO() {
    }

    public DonationDTO(String donorName, DonorType donorType, BigDecimal amount, LocalDate donationDate, String category) {
        this.donorName = donorName;
        this.donorType = donorType;
        this.amount = amount;
        this.donationDate = donationDate;
        this.category = category;
    }

    public DonationDTO(Long donationId, LocalDate donationDate, BigDecimal amount, String DonorType, String donorName, String category, DonationStatus status) {
        this.donationId = donationId;
        this.donationDate = donationDate;
        this.amount = amount;
        this.donorType = donorType;
        this.donorName = donorName;
        this.category = category;
        this.status = status;
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public DonorType getDonorType() {
        return donorType;
    }

    public void setDonorType(DonorType donorType) {
        this.donorType = donorType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }
}