package org.example.dto;

import java.math.BigDecimal;

public class ContratoNoCompletadosDTO {
    private Long contractId;
    private BigDecimal montoTotal;
    private BigDecimal montoPagado;

    public ContratoNoCompletadosDTO(Long contractId, BigDecimal montoTotal, BigDecimal montoPagado) {
        this.contractId = contractId;
        this.montoTotal = montoTotal;
        this.montoPagado = montoPagado;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }
}
