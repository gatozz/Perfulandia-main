package com.perfulandia.envios.repositorio;

import com.perfulandia.envios.entidad.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepositorio extends JpaRepository<Envio, Long> {
    
    Optional<Envio> findByCodigoSeguimiento(String codigoSeguimiento);
    
    List<Envio> findByEstado(Envio.EstadoEnvio estado);
    
    List<Envio> findByEmpresaTransporte(String empresaTransporte);
    
    Optional<Envio> findByPedidoId(Long pedidoId);
    
    @Query("SELECT e FROM Envio e WHERE e.pedido.emailCliente = :email")
    List<Envio> buscarPorEmailCliente(@Param("email") String email);
}
