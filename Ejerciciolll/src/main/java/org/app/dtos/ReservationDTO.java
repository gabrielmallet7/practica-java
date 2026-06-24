package org.app.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.app.models.Vehicle;
import org.app.models.Status;

public class ReservationDTO {
    private Long reservationId;
    private String clientName;
    private Vehicle vehicle; // entidad embebida
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalCost;
    private Status status;

    public ReservationDTO() {
    }

    public ReservationDTO(Long reservationId, String clientName, LocalDate startDate, Vehicle vehicle, LocalDate endDate, BigDecimal totalCost, Status status) {
        this.reservationId = reservationId;
        this.clientName = clientName;
        this.startDate = startDate;
        this.vehicle = vehicle;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Getters y setters
}