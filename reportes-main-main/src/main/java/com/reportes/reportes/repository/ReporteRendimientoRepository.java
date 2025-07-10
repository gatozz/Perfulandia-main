package com.reportes.reportes.repository;

import com.reportes.reportes.model.ReporteRendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRendimientoRepository extends JpaRepository<ReporteRendimiento, Integer> {
}