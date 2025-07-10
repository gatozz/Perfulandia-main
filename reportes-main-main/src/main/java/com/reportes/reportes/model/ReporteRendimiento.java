package com.reportes.reportes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reporte_rendimiento")
public class ReporteRendimiento extends Reporte {

    public ReporteRendimiento() {
        this.setFechaCreacion(LocalDate.now());
    }

    @Override
    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("Reporte de Rendimiento\n");
        reporte.append("Fecha de Creación: ").append(this.getFechaCreacion()).append("\n");
        return reporte.toString();
    }

    @Override
    public String mostrarReporte() {
        String reporteBase = super.mostrarReporte();
        return reporteBase + "\n" + generarReporte();
    }
}