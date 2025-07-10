package com.reportes.reportes.controller;

import com.reportes.reportes.model.ReporteVenta;
import com.reportes.reportes.service.ReporteService;
import com.reportes.reportes.assemblers.ReporteVentaAssembler; // Import the assembler

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
@RequestMapping("/api/reportes/venta")
public class ReporteVentaController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteVentaAssembler reporteVentaAssembler; // Inject the assembler

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Lista de todas las ventas")
    public ResponseEntity<CollectionModel<EntityModel<ReporteVenta>>> obtenerTodosLosReportesVenta() {
        List<EntityModel<ReporteVenta>> reportes = reporteService.obtenerTodosLosReportesVenta().stream()
                .map(reporteVentaAssembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                CollectionModel.of(reportes, linkTo(methodOn(ReporteVentaController.class).obtenerTodosLosReportesVenta()).withSelfRel()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar venta por id", description = "Busca la venta por id")
    public ResponseEntity<EntityModel<ReporteVenta>> obtenerReporteVentaPorId(@PathVariable int id) {
        Optional<ReporteVenta> reporte = reporteService.obtenerReporteVentaPorId(id);
        return reporte.map(value -> new ResponseEntity<>(reporteVentaAssembler.toModel(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Agregar una venta", description = "Agrega una venta")
    public ResponseEntity<EntityModel<ReporteVenta>> crearReporteVenta(@RequestBody ReporteVenta reporte) {
        ReporteVenta nuevoReporte = reporteService.guardarReporteVenta(reporte);
        return new ResponseEntity<>(reporteVentaAssembler.toModel(nuevoReporte), HttpStatus.CREATED);
    }
}