package com.reportes.reportes.controller;

import com.reportes.reportes.model.Reporte;
import com.reportes.reportes.model.ReporteInventario;
import com.reportes.reportes.service.ReporteService;
import com.reportes.reportes.assemblers.ReporteAssembler; // Import the assembler

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel; // For collections
import org.springframework.hateoas.EntityModel;    // For single entities
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteAssembler reporteAssembler; // Inject the assembler

    @GetMapping
    @Operation(summary = "Listar reportes", description = "Listar todos los reportes")
    @ApiResponse(responseCode = "200", description = "Reporte de inventario encontrado",
            content = @Content(schema = @Schema(implementation = ReporteInventario.class))) 
    @ApiResponse(responseCode = "404", description = "Reporte de inventario no encontrado") 
    public ResponseEntity<CollectionModel<EntityModel<Reporte>>> obtenerTodosLosReportes() {
        List<EntityModel<Reporte>> reportes = reporteService.obtenerTodosLosReportes().stream()
                .map(reporteAssembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                CollectionModel.of(reportes, linkTo(methodOn(ReporteController.class).obtenerTodosLosReportes()).withSelfRel()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por id", description = "Buscar reporte por id")
    public ResponseEntity<EntityModel<Reporte>> obtenerReportePorId(@PathVariable int id) {
        Optional<Reporte> reporte = reporteService.obtenerReportePorId(id);
        return reporte.map(value -> new ResponseEntity<>(reporteAssembler.toModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Agregar un reporte", description = "Agregar un reporte")
    public ResponseEntity<EntityModel<Reporte>> crearReporte(@RequestBody Reporte reporte) {
        Reporte nuevoReporte = reporteService.guardarReporte(reporte);
        return new ResponseEntity<>(reporteAssembler.toModel(nuevoReporte), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte por id", description = "Elimina un reporte")
    public ResponseEntity<Void> eliminarReporte(@PathVariable int id) {
        reporteService.eliminarReporte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}