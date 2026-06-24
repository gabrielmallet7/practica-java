package org.example.dto;

import java.math.BigDecimal;

public class CreditosNoFinalizadosDTO {
    private BigDecimal cantidadPrestada;
    private BigDecimal cantidadPagada;

    public CreditosNoFinalizadosDTO(BigDecimal cantidadPrestada, BigDecimal cantidadPagada) {
        this.cantidadPrestada = cantidadPrestada;
        this.cantidadPagada = cantidadPagada;
    }

    public BigDecimal getCantidadPrestada() {
        return cantidadPrestada;
    }

    public void setCantidadPrestada(BigDecimal cantidadPrestada) {
        this.cantidadPrestada = cantidadPrestada;
    }

    public BigDecimal getCantidadPagada() {
        return cantidadPagada;
    }

    public void setCantidadPagada(BigDecimal cantidadPagada) {
        this.cantidadPagada = cantidadPagada;
    }
}
