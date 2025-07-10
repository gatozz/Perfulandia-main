package com.reportes.reportes.assemblers;

import com.reportes.reportes.controller.ReporteController;
import com.reportes.reportes.model.Reporte;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReporteAssembler implements RepresentationModelAssembler<Reporte, EntityModel<Reporte>> {

    @Override
    public EntityModel<Reporte> toModel(Reporte reporte) {
        return EntityModel.of(reporte,
                linkTo(methodOn(ReporteController.class).obtenerReportePorId(reporte.getId())).withSelfRel(),
                linkTo(methodOn(ReporteController.class).obtenerTodosLosReportes()).withRel("reportes"),
                linkTo(methodOn(ReporteController.class).eliminarReporte(reporte.getId())).withRel("delete"));
    }
}
