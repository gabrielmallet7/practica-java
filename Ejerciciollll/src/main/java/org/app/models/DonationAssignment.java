package org.app.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donation_assignment")
public class DonationAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    @Lob
    private String notes;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    // Constructores
    public DonationAssignment() { }

    public DonationAssignment(Donation donation, String notes, LocalDate assignedDate) {
        this.donation = donation;
        this.notes = notes;
        this.assignedDate = assignedDate;
    }

    // Getters y setters
    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }
}