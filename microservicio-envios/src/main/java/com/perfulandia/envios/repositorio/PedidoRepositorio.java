package com.perfulandia.envios.repositorio;

import com.perfulandia.envios.entidad.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);
    
    List<Pedido> findByEmailCliente(String emailCliente);
    
    List<Pedido> findByCiudad(String ciudad);
    
    List<Pedido> findByRegion(String region);
    
    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :fechaInicio AND :fechaFin")
    List<Pedido> buscarPorRangoFechas(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                     @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Pedido p WHERE p.nombreCliente LIKE %:nombre%")
    List<Pedido> buscarPorNombreCliente(@Param("nombre") String nombre);
}
