package org.app.dtos;

import org.app.models.Donation;

import java.time.LocalDate;

public class DonationAssignmentDTO{
    Long assignmentId;
    Donation donation;
    String notes;
    LocalDate assignedDate;

    public DonationAssignmentDTO() {
    }

    public DonationAssignmentDTO(Donation donation, Long assignmentId, String notes, LocalDate assignedDate) {
        this.donation = donation;
        this.assignmentId = assignmentId;
        this.notes = notes;
        this.assignedDate = assignedDate;
    }

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