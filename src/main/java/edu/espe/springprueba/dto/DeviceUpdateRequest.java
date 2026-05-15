package edu.espe.springprueba.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class DeviceUpdateRequest {
    @NotBlank @Size(min = 3, max = 120)
    private String nombre;

    @NotBlank @Size(min = 3, max = 120)
    private String sereal;

    @NotBlank @Size(min = 3, max = 120)
    private String categoria;


    public String getNombre() {
        return nombre;
    }

    private boolean available = false;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSereal() {
        return sereal;
    }

    public void setSereal(String sereal) {
        this.sereal = sereal;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}