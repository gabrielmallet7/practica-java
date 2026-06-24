package org.app.dtos;

import java.time.LocalDate;

public class FiltrosReservaDTO {
    private String clientName;              // Obligatorio
    private LocalDate startDateFrom;        // Opcional
    private LocalDate startDateTo;          // Opcional
    private String status;                  // Opcional: 'RESERVED', 'COMPLETED', 'CANCELLED'
    private String brand;                   // Opcional

    public FiltrosReservaDTO() {
    }

    public LocalDate getStartDateTo() {
        return startDateTo;
    }

    public void setStartDateTo(LocalDate startDateTo) {
        this.startDateTo = startDateTo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getStartDateFrom() {
        return startDateFrom;
    }

    public void setStartDateFrom(LocalDate startDateFrom) {
        this.startDateFrom = startDateFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}