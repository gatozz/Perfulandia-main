package com.reportes.reportes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reporte")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Reporte extends RepresentationModel<Reporte> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    public abstract String generarReporte();

    public String mostrarReporte() {
        return "Reporte ID " + id + " generado el " + fechaCreacion;
    }
}