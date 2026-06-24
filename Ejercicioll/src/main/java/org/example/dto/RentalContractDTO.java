package org.example.dto;

import org.example.models.ContractStatus;
import org.example.models.PropertyType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalContractDTO {
    private Long contractId;
    private String tenantName;
    private PropertyType propertyType;
    private BigDecimal monthlyRent;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContractStatus status;

    public RentalContractDTO(){}
    public RentalContractDTO(Long contractId, String tenantName, PropertyType propertyType, BigDecimal monthlyRent, LocalDate startDate, LocalDate endDate, ContractStatus status) {
        this.contractId = contractId;
        this.tenantName = tenantName;
        this.propertyType = propertyType;
        this.monthlyRent = monthlyRent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
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

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }
}
