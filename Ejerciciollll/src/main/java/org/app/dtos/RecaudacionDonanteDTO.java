package org.app.dtos;

import org.app.models.DonorType;

import java.math.BigDecimal;

public class RecaudacionDonanteDTO {
    private DonorType donorType;
    private Long cantDonaciones;
    private BigDecimal montoTotal;

    public RecaudacionDonanteDTO() {
    }

    public RecaudacionDonanteDTO(DonorType donorType, Long cantDonaciones, BigDecimal montoTotal) {
        this.donorType = donorType;
        this.cantDonaciones = cantDonaciones;
        this.montoTotal = montoTotal;
    }

    public DonorType getDonorType() {
        return donorType;
    }

    public void setDonorType(DonorType donorType) {
        this.donorType = donorType;
    }

    public Long getCantDonaciones() {
        return cantDonaciones;
    }

    public void setCantDonaciones(Long cantDonaciones) {
        this.cantDonaciones = cantDonaciones;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}
