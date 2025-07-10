package com.reportes.reportes.repository;

import com.reportes.reportes.model.Reporte;
import com.reportes.reportes.model.ReporteInventario;
import com.reportes.reportes.model.ReporteVenta;
import com.reportes.reportes.model.ReporteRendimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ReporteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Métodos para Reporte (clase base)
    public List<Reporte> findAll() {
        return entityManager.createQuery("from Reporte", Reporte.class).getResultList();
    }

    public Optional<Reporte> findById(int id) {
        return Optional.ofNullable(entityManager.find(Reporte.class, id));
    }

    public Reporte save(Reporte reporte) {
        if (reporte.getId() == 0) { // El ID es 0 para una nueva entidad
            entityManager.persist(reporte);
            return reporte;
        } else {
            return entityManager.merge(reporte);
        }
    }

    public void deleteById(int id) {
        Optional<Reporte> reporteOptional = findById(id);
        reporteOptional.ifPresent(entityManager::remove);
    }

    // Métodos específicos para ReporteInventario
    public List<ReporteInventario> findAllReporteInventario() {
        return entityManager.createQuery("from ReporteInventario", ReporteInventario.class).getResultList();
    }

    public Optional<ReporteInventario> findReporteInventarioById(int id) {
        return Optional.ofNullable(entityManager.find(ReporteInventario.class, id));
    }

    public ReporteInventario saveReporteInventario(ReporteInventario reporteInventario) {
        if (reporteInventario.getId() == 0) {
            entityManager.persist(reporteInventario);
            return reporteInventario;
        } else {
            return entityManager.merge(reporteInventario);
        }
    }

     public List<ReporteVenta> findAllReporteVenta() {
        return entityManager.createQuery("from ReporteVenta", ReporteVenta.class).getResultList();
    }

    public Optional<ReporteVenta> findReporteVentaById(int id) {
        return Optional.ofNullable(entityManager.find(ReporteVenta.class, id));
    }

    public ReporteVenta saveReporteVenta(ReporteVenta reporte) {
        if (reporte.getId() == 0) {
            entityManager.persist(reporte);
            return reporte;
        } else {
            return entityManager.merge(reporte);
        }
    }

    public List<ReporteRendimiento> findAllReporteRendimiento() {
        return entityManager.createQuery("from ReporteRendimiento", ReporteRendimiento.class).getResultList();
    }

    public Optional<ReporteRendimiento> findReporteRendimientoById(int id) {
        return Optional.ofNullable(entityManager.find(ReporteRendimiento.class, id));
    }

    public ReporteRendimiento saveReporteRendimiento(ReporteRendimiento reporte) {
        if (reporte.getId() == 0) {
            entityManager.persist(reporte);
            return reporte;
        } else {
            return entityManager.merge(reporte);
        }
    }
}

