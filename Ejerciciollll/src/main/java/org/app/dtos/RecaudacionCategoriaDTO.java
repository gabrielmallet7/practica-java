package org.app.dtos;

import org.app.models.Donation;
import org.app.models.DonationAssignment;

import java.math.BigDecimal;
import java.util.List;

public class RecaudacionCategoriaDTO {
    private String categoria;
    private Long donacionesRecibidas;
    private Long donacionesAsingnadas;
    private BigDecimal totalRecaudado;

    public RecaudacionCategoriaDTO() {
    }

    public RecaudacionCategoriaDTO(String categoria, Long donacionesRecibidas, Long donacionesAsingnadas, BigDecimal totalRecaudado) {
        this.categoria = categoria;
        this.donacionesRecibidas = donacionesRecibidas;
        this.donacionesAsingnadas = donacionesAsingnadas;
        this.totalRecaudado = totalRecaudado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Long getDonacionesRecibidas() {
        return donacionesRecibidas;
    }

    public void setDonacionesRecibidas(Long donacionesRecibidas) {
        this.donacionesRecibidas = donacionesRecibidas;
    }

    public Long getDonacionesAsingnadas() {
        return donacionesAsingnadas;
    }

    public void setDonacionesAsingnadas(Long donacionesAsingnadas) {
        this.donacionesAsingnadas = donacionesAsingnadas;
    }

    public BigDecimal getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(BigDecimal totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }
}
