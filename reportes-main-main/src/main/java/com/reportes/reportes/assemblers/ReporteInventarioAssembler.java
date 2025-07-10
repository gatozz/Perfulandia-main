package com.reportes.reportes.assemblers;

import com.reportes.reportes.controller.ReporteInventarioController;
import com.reportes.reportes.model.ReporteInventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReporteInventarioAssembler implements RepresentationModelAssembler<ReporteInventario, EntityModel<ReporteInventario>> {

    @Override
    public EntityModel<ReporteInventario> toModel(ReporteInventario reporteInventario) {
        return EntityModel.of(reporteInventario,
                linkTo(methodOn(ReporteInventarioController.class).obtenerReporteInventarioPorId(reporteInventario.getId())).withSelfRel(),
                linkTo(methodOn(ReporteInventarioController.class).obtenerTodosLosReportesInventario()).withRel("reportesInventario"));
    }
}