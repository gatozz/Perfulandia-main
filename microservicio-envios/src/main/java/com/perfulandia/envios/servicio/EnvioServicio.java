package com.perfulandia.envios.servicio;

import com.perfulandia.envios.entidad.Envio;
import com.perfulandia.envios.repositorio.EnvioRepositorio;
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
public class EnvioServicio {
    
    private final EnvioRepositorio envioRepositorio;
    
    public List<Envio> obtenerTodosLosEnvios() {
        log.info("Obteniendo todos los envíos");
        return envioRepositorio.findAll();
    }
    
    public Optional<Envio> obtenerEnvioPorId(Long id) {
        log.info("Buscando envío con ID: {}", id);
        return envioRepositorio.findById(id);
    }
    
    public Optional<Envio> obtenerEnvioPorCodigoSeguimiento(String codigoSeguimiento) {
        log.info("Buscando envío por código de seguimiento: {}", codigoSeguimiento);
        return envioRepositorio.findByCodigoSeguimiento(codigoSeguimiento);
    }
    
    public Optional<Envio> obtenerEnvioPorPedido(Long pedidoId) {
        log.info("Buscando envío por ID de pedido: {}", pedidoId);
        return envioRepositorio.findByPedidoId(pedidoId);
    }
    
    public Envio crearEnvio(Envio envio) {
        log.info("Creando nuevo envío para pedido: {}", envio.getPedido().getId());
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setFechaEntregaEstimada(LocalDateTime.now().plusDays(3)); // Estimado 3 días
        return envioRepositorio.save(envio);
    }
    
    public Optional<Envio> actualizarEnvio(Long id, Envio envioActualizado) {
        log.info("Actualizando envío con ID: {}", id);
        return envioRepositorio.findById(id)
                .map(envio -> {
                    envio.setEmpresaTransporte(envioActualizado.getEmpresaTransporte());
                    envio.setObservaciones(envioActualizado.getObservaciones());
                    envio.setFechaEntregaEstimada(envioActualizado.getFechaEntregaEstimada());
                    return envioRepositorio.save(envio);
                });
    }
    
    public Optional<Envio> actualizarEstadoEnvio(Long id, Envio.EstadoEnvio nuevoEstado) {
        log.info("Actualizando estado del envío {} a {}", id, nuevoEstado);
        return envioRepositorio.findById(id)
                .map(envio -> {
                    envio.setEstado(nuevoEstado);
                    if (nuevoEstado == Envio.EstadoEnvio.ENTREGADO) {
                        envio.setFechaEntregaReal(LocalDateTime.now());
                    }
                    return envioRepositorio.save(envio);
                });
    }
    
    public List<Envio> buscarPorEstado(Envio.EstadoEnvio estado) {
        log.info("Buscando envíos por estado: {}", estado);
        return envioRepositorio.findByEstado(estado);
    }
    
    public List<Envio> buscarPorEmpresaTransporte(String empresaTransporte) {
        log.info("Buscando envíos por empresa de transporte: {}", empresaTransporte);
        return envioRepositorio.findByEmpresaTransporte(empresaTransporte);
    }
    
    public List<Envio> buscarPorEmailCliente(String email) {
        log.info("Buscando envíos por email del cliente: {}", email);
        return envioRepositorio.buscarPorEmailCliente(email);
    }
}
