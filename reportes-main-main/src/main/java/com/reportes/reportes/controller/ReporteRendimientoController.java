package com.reportes.reportes.controller;

import com.reportes.reportes.model.ReporteRendimiento;
import com.reportes.reportes.service.ReporteService;
import com.reportes.reportes.assemblers.ReporteRendimientoAssembler; // Import the assembler

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reportes/rendimiento")
public class ReporteRendimientoController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteRendimientoAssembler reporteRendimientoAssembler; // Inject the assembler

    @GetMapping
    @Operation(summary = "Listar reportes de rendimiento", description = "Aca se listan todos los reportes de rendimiento")
    public ResponseEntity<CollectionModel<EntityModel<ReporteRendimiento>>> obtenerTodosLosReportesRendimiento() {
        List<EntityModel<ReporteRendimiento>> reportes = reporteService.obtenerTodosLosReportesRendimiento().stream()
                .map(reporteRendimientoAssembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                CollectionModel.of(reportes, linkTo(methodOn(ReporteRendimientoController.class).obtenerTodosLosReportesRendimiento()).withSelfRel()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte de rendimiento", description = "Buscar reporte de rendimiento por id")
    public ResponseEntity<EntityModel<ReporteRendimiento>> obtenerReporteRendimientoPorId(@PathVariable int id) {
        Optional<ReporteRendimiento> reporte = reporteService.obtenerReporteRendimientoPorId(id);
        return reporte.map(value -> new ResponseEntity<>(reporteRendimientoAssembler.toModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Agregar un reporte de rendimiento id", description = "Agrega un reporte de rendimiento")
    public ResponseEntity<EntityModel<ReporteRendimiento>> crearReporteRendimiento(@RequestBody ReporteRendimiento reporte) {
        ReporteRendimiento nuevoReporte = reporteService.guardarReporteRendimiento(reporte);
        return new ResponseEntity<>(reporteRendimientoAssembler.toModel(nuevoReporte), HttpStatus.CREATED);
    }
}