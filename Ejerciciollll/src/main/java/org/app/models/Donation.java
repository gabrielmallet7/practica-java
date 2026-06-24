package org.app.models;

import jakarta.persistence.*;
import org.app.models.DonationStatus;
import org.app.models.DonorType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "donation")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_id")
    private Long donationId;

    @Column(name = "donor_name", nullable = false, length = 100)
    private String donorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "donor_type", nullable = false)
    private DonorType donorType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "donation_date", nullable = false)
    private LocalDate donationDate;

    @Column(nullable = false, length = 50)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status;

    // Constructores
    public Donation() { }

    public Donation(String donorName, DonorType donorType, BigDecimal amount,
                    LocalDate donationDate, String category) {
        this.donorName = donorName;
        this.donorType = donorType;
        this.amount = amount;
        this.donationDate = donationDate;
        this.category = category;
        this.status = DonationStatus.RECEIVED;
    }

    // Getters y setters
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