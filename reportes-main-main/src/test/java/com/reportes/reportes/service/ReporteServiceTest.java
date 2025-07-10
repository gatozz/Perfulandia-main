package com.reportes.reportes.service;

import com.reportes.reportes.model.Reporte;
import com.reportes.reportes.model.ReporteInventario;
import com.reportes.reportes.model.ReporteRendimiento;
import com.reportes.reportes.model.ReporteVenta;
import com.reportes.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock 
    private ReporteRepository reporteRepository;

    @InjectMocks 
    private ReporteService reporteService;

    private ReporteInventario reporteInventario;
    private ReporteRendimiento reporteRendimiento;
    private ReporteVenta reporteVenta;

    @BeforeEach
    void setUp() {
       
        reporteInventario = new ReporteInventario();
        reporteInventario.setId(1);
        reporteInventario.setFechaCreacion(LocalDate.now());
        reporteInventario.setAlertaBajoStock(true);

        reporteRendimiento = new ReporteRendimiento();
        reporteRendimiento.setId(2);
        reporteRendimiento.setFechaCreacion(LocalDate.now().minusDays(5));

        reporteVenta = new ReporteVenta();
        reporteVenta.setId(3);
        reporteVenta.setFechaCreacion(LocalDate.now().minusDays(10));
    }

    // --- Pruebas para Reporte general ---
    @Test
    @DisplayName("Debe obtener todos los reportes")
    void shouldGetAllReportes() {
        
        List<Reporte> allReports = Arrays.asList(reporteInventario, reporteRendimiento, reporteVenta);
        when(reporteRepository.findAll()).thenReturn(allReports);

        
        List<Reporte> foundReports = reporteService.obtenerTodosLosReportes();

        
        assertNotNull(foundReports);
        assertEquals(3, foundReports.size());
        assertTrue(foundReports.contains(reporteInventario));
        verify(reporteRepository, times(1)).findAll(); 
    }

    @Test
    @DisplayName("Debe obtener un reporte por ID existente")
    void shouldGetReporteByIdWhenExists() {
        
        when(reporteRepository.findById(1)).thenReturn(Optional.of(reporteInventario));

        
        Optional<Reporte> foundReporte = reporteService.obtenerReportePorId(1);

        
        assertTrue(foundReporte.isPresent());
        assertEquals(reporteInventario.getId(), foundReporte.get().getId());
        verify(reporteRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("No debe obtener un reporte por ID no existente")
    void shouldNotGetReporteByIdWhenNotExists() {
        
        when(reporteRepository.findById(99)).thenReturn(Optional.empty());

        
        Optional<Reporte> foundReporte = reporteService.obtenerReportePorId(99);

        
        assertFalse(foundReporte.isPresent());
        verify(reporteRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Debe guardar un nuevo reporte")
    void shouldSaveReporte() {
        
        ReporteInventario newReporte = new ReporteInventario();
        newReporte.setFechaCreacion(LocalDate.now());
        newReporte.setAlertaBajoStock(false);
        when(reporteRepository.save(any(Reporte.class))).thenReturn(newReporte);

        
        Reporte savedReporte = reporteService.guardarReporte(newReporte);

        
        assertNotNull(savedReporte);
        assertEquals(newReporte.getFechaCreacion(), savedReporte.getFechaCreacion());
        verify(reporteRepository, times(1)).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe eliminar un reporte por ID")
    void shouldDeleteReporteById() {
        
        doNothing().when(reporteRepository).deleteById(1);

        
        reporteService.eliminarReporte(1);

        
        verify(reporteRepository, times(1)).deleteById(1);
    }

    // --- Pruebas para ReporteInventario ---
    @Test
    @DisplayName("Debe obtener todos los reportes de inventario")
    void shouldGetAllReportesInventario() {
        
        List<ReporteInventario> inventarioReports = Arrays.asList(reporteInventario);
        when(reporteRepository.findAllReporteInventario()).thenReturn(inventarioReports);

        
        List<ReporteInventario> foundReports = reporteService.obtenerTodosLosReportesInventario();

        
        assertNotNull(foundReports);
        assertEquals(1, foundReports.size());
        assertEquals(reporteInventario, foundReports.get(0));
        verify(reporteRepository, times(1)).findAllReporteInventario();
    }

    @Test
    @DisplayName("Debe obtener reporte de inventario por ID existente")
    void shouldGetReporteInventarioByIdWhenExists() {
        
        when(reporteRepository.findReporteInventarioById(1)).thenReturn(Optional.of(reporteInventario));

        
        Optional<ReporteInventario> foundReporte = reporteService.obtenerReporteInventarioPorId(1);

        
        assertTrue(foundReporte.isPresent());
        assertEquals(reporteInventario.getId(), foundReporte.get().getId());
        verify(reporteRepository, times(1)).findReporteInventarioById(1);
    }

    @Test
    @DisplayName("Debe guardar un reporte de inventario")
    void shouldSaveReporteInventario() {
        
        ReporteInventario newReporte = new ReporteInventario();
        newReporte.setFechaCreacion(LocalDate.now());
        newReporte.setAlertaBajoStock(true);
        when(reporteRepository.saveReporteInventario(any(ReporteInventario.class))).thenReturn(newReporte);

        
        ReporteInventario savedReporte = reporteService.guardarReporteInventario(newReporte);

        
        assertNotNull(savedReporte);
        assertEquals(newReporte.getFechaCreacion(), savedReporte.getFechaCreacion());
        verify(reporteRepository, times(1)).saveReporteInventario(any(ReporteInventario.class));
    }

    // --- Pruebas para ReporteRendimiento ---
    @Test
    @DisplayName("Debe obtener todos los reportes de rendimiento")
    void shouldGetAllReportesRendimiento() {
        
        List<ReporteRendimiento> rendimientoReports = Arrays.asList(reporteRendimiento);
        when(reporteRepository.findAllReporteRendimiento()).thenReturn(rendimientoReports);

        
        List<ReporteRendimiento> foundReports = reporteService.obtenerTodosLosReportesRendimiento();

        
        assertNotNull(foundReports);
        assertEquals(1, foundReports.size());
        assertEquals(reporteRendimiento, foundReports.get(0));
        verify(reporteRepository, times(1)).findAllReporteRendimiento();
    }

    @Test
    @DisplayName("Debe obtener reporte de rendimiento por ID existente")
    void shouldGetReporteRendimientoByIdWhenExists() {
        
        when(reporteRepository.findReporteRendimientoById(2)).thenReturn(Optional.of(reporteRendimiento));

       
        Optional<ReporteRendimiento> foundReporte = reporteService.obtenerReporteRendimientoPorId(2);

        
        assertTrue(foundReporte.isPresent());
        assertEquals(reporteRendimiento.getId(), foundReporte.get().getId());
        verify(reporteRepository, times(1)).findReporteRendimientoById(2);
    }

    @Test
    @DisplayName("Debe guardar un reporte de rendimiento")
    void shouldSaveReporteRendimiento() {
        
        ReporteRendimiento newReporte = new ReporteRendimiento();
        newReporte.setFechaCreacion(LocalDate.now());
        when(reporteRepository.saveReporteRendimiento(any(ReporteRendimiento.class))).thenReturn(newReporte);

        
        ReporteRendimiento savedReporte = reporteService.guardarReporteRendimiento(newReporte);

        
        assertNotNull(savedReporte);
        assertEquals(newReporte.getFechaCreacion(), savedReporte.getFechaCreacion());
        verify(reporteRepository, times(1)).saveReporteRendimiento(any(ReporteRendimiento.class));
    }

    // --- Pruebas para ReporteVenta ---
    @Test
    @DisplayName("Debe obtener todos los reportes de venta")
    void shouldGetAllReportesVenta() {
        
        List<ReporteVenta> ventaReports = Arrays.asList(reporteVenta);
        when(reporteRepository.findAllReporteVenta()).thenReturn(ventaReports);

        
        List<ReporteVenta> foundReports = reporteService.obtenerTodosLosReportesVenta();

        
        assertNotNull(foundReports);
        assertEquals(1, foundReports.size());
        assertEquals(reporteVenta, foundReports.get(0));
        verify(reporteRepository, times(1)).findAllReporteVenta();
    }

    @Test
    @DisplayName("Debe obtener reporte de venta por ID existente")
    void shouldGetReporteVentaByIdWhenExists() {
        
        when(reporteRepository.findReporteVentaById(3)).thenReturn(Optional.of(reporteVenta));

        
        Optional<ReporteVenta> foundReporte = reporteService.obtenerReporteVentaPorId(3);

        
        assertTrue(foundReporte.isPresent());
        assertEquals(reporteVenta.getId(), foundReporte.get().getId());
        verify(reporteRepository, times(1)).findReporteVentaById(3);
    }

    @Test
    @DisplayName("Debe guardar un reporte de venta")
    void shouldSaveReporteVenta() {
        
        ReporteVenta newReporte = new ReporteVenta();
        newReporte.setFechaCreacion(LocalDate.now());
        when(reporteRepository.saveReporteVenta(any(ReporteVenta.class))).thenReturn(newReporte);

        
        ReporteVenta savedReporte = reporteService.guardarReporteVenta(newReporte);

        
        assertNotNull(savedReporte);
        assertEquals(newReporte.getFechaCreacion(), savedReporte.getFechaCreacion());
        verify(reporteRepository, times(1)).saveReporteVenta(any(ReporteVenta.class));
    }
}