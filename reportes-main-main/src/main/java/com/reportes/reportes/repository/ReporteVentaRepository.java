package com.reportes.reportes.repository;

import com.reportes.reportes.model.ReporteVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteVentaRepository extends JpaRepository<ReporteVenta, Integer> {
}