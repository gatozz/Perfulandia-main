package com.reportes.reportes.assemblers;

import com.reportes.reportes.controller.ReporteVentaController;
import com.reportes.reportes.model.ReporteVenta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReporteVentaAssembler implements RepresentationModelAssembler<ReporteVenta, EntityModel<ReporteVenta>> {

    @Override
    public EntityModel<ReporteVenta> toModel(ReporteVenta reporteVenta) {
        return EntityModel.of(reporteVenta,
                linkTo(methodOn(ReporteVentaController.class).obtenerReporteVentaPorId(reporteVenta.getId())).withSelfRel(),
                linkTo(methodOn(ReporteVentaController.class).obtenerTodosLosReportesVenta()).withRel("reportesVenta"));
    }
}