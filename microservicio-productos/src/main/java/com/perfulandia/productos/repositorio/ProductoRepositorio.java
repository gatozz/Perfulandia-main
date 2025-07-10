package com.perfulandia.productos.repositorio;

import com.perfulandia.productos.entidad.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    
    List<Producto> findByMarcaAndActivoTrue(String marca);
    
    List<Producto> findByCategoriaAndActivoTrue(String categoria);
    
    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre% AND p.activo = true")
    List<Producto> buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT p FROM Producto p WHERE p.stock < :stockMinimo AND p.activo = true")
    List<Producto> buscarProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);
    
    Optional<Producto> findByIdAndActivoTrue(Long id);
}
