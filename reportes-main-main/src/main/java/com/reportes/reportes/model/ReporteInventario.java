package com.reportes.reportes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reporte_inventario")
public class ReporteInventario extends Reporte {

    @Column(name = "alerta_bajo_stock")
    private boolean alertaBajoStock;

    public ReporteInventario() {
        this.setFechaCreacion(LocalDate.now());
    }

    @Override
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("Reporte de Inventario\n");
        reporte.append("Fecha de Creación: ").append(this.getFechaCreacion()).append("\n");
        reporte.append("Alerta de Bajo Stock: ").append(this.alertaBajoStock).append("\n");
        return reporte.toString();
    }

    @Override
    public String mostrarReporte() {
        String reporteBase = super.mostrarReporte();
        return reporteBase + "\n" + generarReporte();
    }
}