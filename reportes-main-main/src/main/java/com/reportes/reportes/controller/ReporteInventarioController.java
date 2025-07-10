package com.reportes.reportes.controller;

import com.reportes.reportes.model.ReporteInventario;
import com.reportes.reportes.service.ReporteService;
import com.reportes.reportes.assemblers.ReporteInventarioAssembler; // Import the assembler

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
@RequestMapping("/api/reportes/inventario")
public class ReporteInventarioController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteInventarioAssembler reporteInventarioAssembler; // Inject the assembler

    @GetMapping
    @Operation(summary = "Listar reportes de inventario", description = "Aca se listan todos los reportes de inventario")
    public ResponseEntity<CollectionModel<EntityModel<ReporteInventario>>> obtenerTodosLosReportesInventario() {
        List<EntityModel<ReporteInventario>> reportes = reporteService.obtenerTodosLosReportesInventario().stream()
                .map(reporteInventarioAssembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                CollectionModel.of(reportes, linkTo(methodOn(ReporteInventarioController.class).obtenerTodosLosReportesInventario()).withSelfRel()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte de inventario", description = "Buscar reporte de inventario por id")
    public ResponseEntity<EntityModel<ReporteInventario>> obtenerReporteInventarioPorId(@PathVariable int id) {
        Optional<ReporteInventario> reporte = reporteService.obtenerReporteInventarioPorId(id);
        return reporte.map(value -> new ResponseEntity<>(reporteInventarioAssembler.toModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Agregar un reporte de inventario id", description = "Agrega un reporte de inventario")
    public ResponseEntity<EntityModel<ReporteInventario>> crearReporteInventario(@RequestBody ReporteInventario reporteInventario) {
        ReporteInventario nuevoReporte = reporteService.guardarReporteInventario(reporteInventario);
        return new ResponseEntity<>(reporteInventarioAssembler.toModel(nuevoReporte), HttpStatus.CREATED);
    }
}