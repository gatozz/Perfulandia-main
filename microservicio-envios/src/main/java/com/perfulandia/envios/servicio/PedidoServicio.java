package com.perfulandia.envios.servicio;

import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.repositorio.PedidoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PedidoServicio {
    
    private final PedidoRepositorio pedidoRepositorio;
    
    public List<Pedido> obtenerTodosLosPedidos() {
        log.info("Obteniendo todos los pedidos");
        return pedidoRepositorio.findAll();
    }
    
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        log.info("Buscando pedido con ID: {}", id);
        return pedidoRepositorio.findById(id);
    }
    
    public Pedido crearPedido(Pedido pedido) {
        log.info("Creando nuevo pedido para cliente: {}", pedido.getNombreCliente());
        return pedidoRepositorio.save(pedido);
    }
    
    public Optional<Pedido> actualizarPedido(Long id, Pedido pedidoActualizado) {
        log.info("Actualizando pedido con ID: {}", id);
        return pedidoRepositorio.findById(id)
                .map(pedido -> {
                    pedido.setNombreCliente(pedidoActualizado.getNombreCliente());
                    pedido.setEmailCliente(pedidoActualizado.getEmailCliente());
                    pedido.setTelefonoCliente(pedidoActualizado.getTelefonoCliente());
                    pedido.setDireccionEntrega(pedidoActualizado.getDireccionEntrega());
                    pedido.setCiudad(pedidoActualizado.getCiudad());
                    pedido.setRegion(pedidoActualizado.getRegion());
                    pedido.setTotal(pedidoActualizado.getTotal());
                    return pedidoRepositorio.save(pedido);
                });
    }
    
    public Optional<Pedido> actualizarEstadoPedido(Long id, Pedido.EstadoPedido nuevoEstado) {
        log.info("Actualizando estado del pedido {} a {}", id, nuevoEstado);
        return pedidoRepositorio.findById(id)
                .map(pedido -> {
                    pedido.setEstado(nuevoEstado);
                    return pedidoRepositorio.save(pedido);
                });
    }
    
    public List<Pedido> buscarPorEstado(Pedido.EstadoPedido estado) {
        log.info("Buscando pedidos por estado: {}", estado);
        return pedidoRepositorio.findByEstado(estado);
    }
    
    public List<Pedido> buscarPorEmailCliente(String email) {
        log.info("Buscando pedidos por email del cliente: {}", email);
        return pedidoRepositorio.findByEmailCliente(email);
    }
    
    public List<Pedido> buscarPorCiudad(String ciudad) {
        log.info("Buscando pedidos por ciudad: {}", ciudad);
        return pedidoRepositorio.findByCiudad(ciudad);
    }
    
    public List<Pedido> buscarPorRegion(String region) {
        log.info("Buscando pedidos por región: {}", region);
        return pedidoRepositorio.findByRegion(region);
    }
    
    public List<Pedido> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Buscando pedidos entre {} y {}", fechaInicio, fechaFin);
        return pedidoRepositorio.buscarPorRangoFechas(fechaInicio, fechaFin);
    }
    
    public List<Pedido> buscarPorNombreCliente(String nombre) {
        log.info("Buscando pedidos por nombre del cliente: {}", nombre);
        return pedidoRepositorio.buscarPorNombreCliente(nombre);
    }
}
