package org.example.dto;

public class ProductoDTO {
    private Long id;
    private String nombre;
    private Integer stock;
    private Boolean esActivo;

    public ProductoDTO() {
    }
    public ProductoDTO(Long id, String nombre, Integer stock, Boolean eseActivo) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.esActivo = eseActivo;
    }

    public Long getId() {
        return id;
    }

    public Boolean isActivo() {
        return esActivo;
    }

    public void setActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
