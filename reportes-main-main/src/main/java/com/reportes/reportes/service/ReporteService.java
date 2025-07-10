package com.reportes.reportes.service;

import com.reportes.reportes.model.Reporte;
import com.reportes.reportes.model.ReporteInventario;
import com.reportes.reportes.model.ReporteVenta;
import com.reportes.reportes.model.ReporteRendimiento;
import com.reportes.reportes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> obtenerTodosLosReportes() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> obtenerReportePorId(int id) {
        return reporteRepository.findById(id);
    }

    public Reporte guardarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminarReporte(int id) {
        reporteRepository.deleteById(id);
    }

    public List<ReporteInventario> obtenerTodosLosReportesInventario() {
        return reporteRepository.findAllReporteInventario();
    }

    public Optional<ReporteInventario> obtenerReporteInventarioPorId(int id) {
        return reporteRepository.findReporteInventarioById(id);
    }

    public ReporteInventario guardarReporteInventario(ReporteInventario reporteInventario) {
        return reporteRepository.saveReporteInventario(reporteInventario);
    }

     public List<ReporteVenta> obtenerTodosLosReportesVenta() {
        return reporteRepository.findAllReporteVenta();
    }

    public Optional<ReporteVenta> obtenerReporteVentaPorId(int id) {
        return reporteRepository.findReporteVentaById(id);
    }

    public ReporteVenta guardarReporteVenta(ReporteVenta reporte) {
        return reporteRepository.saveReporteVenta(reporte);
    }

    public List<ReporteRendimiento> obtenerTodosLosReportesRendimiento() {
        return reporteRepository.findAllReporteRendimiento();
    }

    public Optional<ReporteRendimiento> obtenerReporteRendimientoPorId(int id) {
        return reporteRepository.findReporteRendimientoById(id);
    }

    public ReporteRendimiento guardarReporteRendimiento(ReporteRendimiento reporte) {
        return reporteRepository.saveReporteRendimiento(reporte);
    }
}
