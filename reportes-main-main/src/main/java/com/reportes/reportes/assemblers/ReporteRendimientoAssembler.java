package com.reportes.reportes.assemblers;

import com.reportes.reportes.controller.ReporteRendimientoController;
import com.reportes.reportes.model.ReporteRendimiento;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReporteRendimientoAssembler implements RepresentationModelAssembler<ReporteRendimiento, EntityModel<ReporteRendimiento>> {

    @Override
    public EntityModel<ReporteRendimiento> toModel(ReporteRendimiento reporteRendimiento) {
        return EntityModel.of(reporteRendimiento,
                linkTo(methodOn(ReporteRendimientoController.class).obtenerReporteRendimientoPorId(reporteRendimiento.getId())).withSelfRel(),
                linkTo(methodOn(ReporteRendimientoController.class).obtenerTodosLosReportesRendimiento()).withRel("reportesRendimiento"));
    }
}