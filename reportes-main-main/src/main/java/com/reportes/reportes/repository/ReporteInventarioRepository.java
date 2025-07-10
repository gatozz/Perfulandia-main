package com.reportes.reportes.repository;

import com.reportes.reportes.model.ReporteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteInventarioRepository extends JpaRepository<ReporteInventario, Integer> {
}