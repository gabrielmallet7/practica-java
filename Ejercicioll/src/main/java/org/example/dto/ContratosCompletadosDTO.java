package org.example.dto;

import org.example.models.PropertyType;

import java.math.BigDecimal;

public class ContratosCompletadosDTO {

    private PropertyType propertyType;
    private Long cantidad;
    private BigDecimal montoTotal;

    public ContratosCompletadosDTO(PropertyType propertyType, Long cantidad, BigDecimal montoTotal) {
        this.propertyType = propertyType;
        this.cantidad = cantidad;
        this.montoTotal = montoTotal;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}
