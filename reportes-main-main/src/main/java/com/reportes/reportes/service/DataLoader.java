package com.reportes.reportes.service; // O el paquete que elijas

import com.reportes.reportes.model.ReporteInventario;
import com.reportes.reportes.model.ReporteRendimiento;
import com.reportes.reportes.model.ReporteVenta;
import com.reportes.reportes.repository.ReporteInventarioRepository;
import com.reportes.reportes.repository.ReporteRendimientoRepository;
import com.reportes.reportes.repository.ReporteVentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final ReporteInventarioRepository reporteInventarioRepository;
    private final ReporteRendimientoRepository reporteRendimientoRepository;
    private final ReporteVentaRepository reporteVentaRepository;

    public DataLoader(ReporteInventarioRepository reporteInventarioRepository,
                      ReporteRendimientoRepository reporteRendimientoRepository,
                      ReporteVentaRepository reporteVentaRepository) {
        this.reporteInventarioRepository = reporteInventarioRepository;
        this.reporteRendimientoRepository = reporteRendimientoRepository;
        this.reporteVentaRepository = reporteVentaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Cargar datos de ejemplo para ReporteInventario
        if (reporteInventarioRepository.count() == 0) {
            ReporteInventario reporteInventario1 = new ReporteInventario();
            reporteInventario1.setFechaCreacion(LocalDate.of(2025, 6, 25));
            reporteInventario1.setAlertaBajoStock(true);
            reporteInventarioRepository.save(reporteInventario1);

            ReporteInventario reporteInventario2 = new ReporteInventario();
            reporteInventario2.setFechaCreacion(LocalDate.of(2025, 6, 30));
            reporteInventario2.setAlertaBajoStock(false);
            reporteInventarioRepository.save(reporteInventario2);

            System.out.println("Cargados 2 Reportes de Inventario.");
        }

        // Cargar datos de ejemplo para ReporteRendimiento
        if (reporteRendimientoRepository.count() == 0) {
            ReporteRendimiento reporteRendimiento1 = new ReporteRendimiento();
            reporteRendimiento1.setFechaCreacion(LocalDate.of(2025, 7, 1));
            reporteRendimientoRepository.save(reporteRendimiento1);

            ReporteRendimiento reporteRendimiento2 = new ReporteRendimiento();
            reporteRendimiento2.setFechaCreacion(LocalDate.of(2025, 7, 2));
            reporteRendimientoRepository.save(reporteRendimiento2);

            System.out.println("Cargados 2 Reportes de Rendimiento.");
        }

        // Cargar datos de ejemplo para ReporteVenta
        if (reporteVentaRepository.count() == 0) {
            ReporteVenta reporteVenta1 = new ReporteVenta();
            reporteVenta1.setFechaCreacion(LocalDate.of(2025, 6, 15));
            reporteVentaRepository.save(reporteVenta1);

            ReporteVenta reporteVenta2 = new ReporteVenta();
            reporteVenta2.setFechaCreacion(LocalDate.of(2025, 6, 20));
            reporteVentaRepository.save(reporteVenta2);

            System.out.println("Cargados 2 Reportes de Venta.");
        }
    }
}