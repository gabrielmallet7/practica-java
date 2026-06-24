package org.example.dto;

import org.example.models.CreditType;

import java.math.BigDecimal;

public class CreditosPagadosDTO {
    private CreditType creditType;
    private Long cantCreditos;
    private BigDecimal sumaMontoPrestado;

    public CreditosPagadosDTO(CreditType creditType, Long cantCreditos, BigDecimal sumaMontoPrestado) {
        this.creditType = creditType;
        this.cantCreditos = cantCreditos;
        this.sumaMontoPrestado = sumaMontoPrestado;
    }

    public Long getCantCreditos() {
        return cantCreditos;
    }

    public void setCantCreditos(Long cantCreditos) {
        this.cantCreditos = cantCreditos;
    }

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getSumaMontoPrestado() {
        return sumaMontoPrestado;
    }

    public void setSumaMontoPrestado(BigDecimal sumaMontoPrestado) {
        this.sumaMontoPrestado = sumaMontoPrestado;
    }
}
