package com.perfulandia.envios.configuracion;

import com.perfulandia.envios.entidad.DetallePedido;
import com.perfulandia.envios.entidad.Envio;
import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.repositorio.PedidoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CargadorDatos implements CommandLineRunner {
    
    private final PedidoRepositorio pedidoRepositorio;
    
    @Override
    public void run(String... args) throws Exception {
        if (pedidoRepositorio.count() == 0) {
            log.info("Cargando datos iniciales de pedidos y envíos chilenos...");
            cargarPedidosChilenos();
        }
    }
    
    private void cargarPedidosChilenos() {
        // Pedido 1 - Santiago
        Pedido pedido1 = new Pedido();
        pedido1.setNombreCliente("María González");
        pedido1.setEmailCliente("maria.gonzalez@email.cl");
        pedido1.setTelefonoCliente("+56912345678");
        pedido1.setDireccionEntrega("Av. Providencia 1234, Providencia");
        pedido1.setCiudad("Santiago");
        pedido1.setRegion("Región Metropolitana");
        pedido1.setTotal(new BigDecimal("87000"));
        pedido1.setEstado(Pedido.EstadoPedido.CONFIRMADO);
        
        DetallePedido detalle1 = new DetallePedido();
        detalle1.setProductoId(1L);
        detalle1.setNombreProducto("Perfume Andino");
        detalle1.setCantidad(1);
        detalle1.setPrecioUnitario(new BigDecimal("45000"));
        detalle1.setSubtotal(new BigDecimal("45000"));
        detalle1.setPedido(pedido1);
        
        DetallePedido detalle2 = new DetallePedido();
        detalle2.setProductoId(3L);
        detalle2.setNombreProducto("Brisa Valparaíso");
        detalle2.setCantidad(1);
        detalle2.setPrecioUnitario(new BigDecimal("38000"));
        detalle2.setSubtotal(new BigDecimal("38000"));
        detalle2.setPedido(pedido1);
        
        pedido1.setDetalles(Arrays.asList(detalle1, detalle2));
        
        Envio envio1 = new Envio();
        envio1.setEmpresaTransporte("Chilexpress");
        envio1.setEstado(Envio.EstadoEnvio.EN_TRANSITO);
        envio1.setFechaEnvio(LocalDateTime.now().minusDays(1));
        envio1.setFechaEntregaEstimada(LocalDateTime.now().plusDays(2));
        envio1.setObservaciones("Entrega en horario de oficina");
        envio1.setPedido(pedido1);
        
        pedido1.setEnvio(envio1);
        
        // Pedido 2 - Valparaíso
        Pedido pedido2 = new Pedido();
        pedido2.setNombreCliente("Carlos Rodríguez");
        pedido2.setEmailCliente("carlos.rodriguez@email.cl");
        pedido2.setTelefonoCliente("+56987654321");
        pedido2.setDireccionEntrega("Calle Almirante Cochrane 567, Valparaíso");
        pedido2.setCiudad("Valparaíso");
        pedido2.setRegion("Región de Valparaíso");
        pedido2.setTotal(new BigDecimal("52000"));
        pedido2.setEstado(Pedido.EstadoPedido.ENVIADO);
        
        DetallePedido detalle3 = new DetallePedido();
        detalle3.setProductoId(2L);
        detalle3.setNombreProducto("Esencia Patagónica");
        detalle3.setCantidad(1);
        detalle3.setPrecioUnitario(new BigDecimal("52000"));
        detalle3.setSubtotal(new BigDecimal("52000"));
        detalle3.setPedido(pedido2);
        
        pedido2.setDetalles(Arrays.asList(detalle3));
        
        Envio envio2 = new Envio();
        envio2.setEmpresaTransporte("Correos de Chile");
        envio2.setEstado(Envio.EstadoEnvio.EN_REPARTO);
        envio2.setFechaEnvio(LocalDateTime.now().minusDays(2));
        envio2.setFechaEntregaEstimada(LocalDateTime.now().plusDays(1));
        envio2.setObservaciones("Contactar al cliente antes de la entrega");
        envio2.setPedido(pedido2);
        
        pedido2.setEnvio(envio2);
        
        // Pedido 3 - Concepción
        Pedido pedido3 = new Pedido();
        pedido3.setNombreCliente("Ana Martínez");
        pedido3.setEmailCliente("ana.martinez@email.cl");
        pedido3.setTelefonoCliente("+56956789012");
        pedido3.setDireccionEntrega("Av. O'Higgins 890, Concepción");
        pedido3.setCiudad("Concepción");
        pedido3.setRegion("Región del Biobío");
        pedido3.setTotal(new BigDecimal("90000"));
        pedido3.setEstado(Pedido.EstadoPedido.PREPARANDO);
        
        DetallePedido detalle4 = new DetallePedido();
        detalle4.setProductoId(4L);
        detalle4.setNombreProducto("Aroma Atacama");
        detalle4.setCantidad(1);
        detalle4.setPrecioUnitario(new BigDecimal("48000"));
        detalle4.setSubtotal(new BigDecimal("48000"));
        detalle4.setPedido(pedido3);
        
        DetallePedido detalle5 = new DetallePedido();
        detalle5.setProductoId(5L);
        detalle5.setNombreProducto("Fragancia Chiloé");
        detalle5.setCantidad(1);
        detalle5.setPrecioUnitario(new BigDecimal("42000"));
        detalle5.setSubtotal(new BigDecimal("42000"));
        detalle5.setPedido(pedido3);
        
        pedido3.setDetalles(Arrays.asList(detalle4, detalle5));
        
        Envio envio3 = new Envio();
        envio3.setEmpresaTransporte("Starken");
        envio3.setEstado(Envio.EstadoEnvio.PREPARANDO);
        envio3.setObservaciones("Pedido en preparación");
        envio3.setPedido(pedido3);
        
        pedido3.setEnvio(envio3);
        
        // Pedido 4 - La Serena
        Pedido pedido4 = new Pedido();
        pedido4.setNombreCliente("Luis Fernández");
        pedido4.setEmailCliente("luis.fernandez@email.cl");
        pedido4.setTelefonoCliente("+56923456789");
        pedido4.setDireccionEntrega("Av. Francisco de Aguirre 123, La Serena");
        pedido4.setCiudad("La Serena");
        pedido4.setRegion("Región de Coquimbo");
        pedido4.setTotal(new BigDecimal("55000"));
        pedido4.setEstado(Pedido.EstadoPedido.ENTREGADO);
        
        DetallePedido detalle6 = new DetallePedido();
        detalle6.setProductoId(6L);
        detalle6.setNombreProducto("Perfume Santiago");
        detalle6.setCantidad(1);
        detalle6.setPrecioUnitario(new BigDecimal("55000"));
        detalle6.setSubtotal(new BigDecimal("55000"));
        detalle6.setPedido(pedido4);
        
        pedido4.setDetalles(Arrays.asList(detalle6));
        
        Envio envio4 = new Envio();
        envio4.setEmpresaTransporte("Chilexpress");
        envio4.setEstado(Envio.EstadoEnvio.ENTREGADO);
        envio4.setFechaEnvio(LocalDateTime.now().minusDays(5));
        envio4.setFechaEntregaEstimada(LocalDateTime.now().minusDays(2));
        envio4.setFechaEntregaReal(LocalDateTime.now().minusDays(1));
        envio4.setObservaciones("Entregado exitosamente");
        envio4.setPedido(pedido4);
        
        pedido4.setEnvio(envio4);
        
        // Pedido 5 - Temuco
        Pedido pedido5 = new Pedido();
        pedido5.setNombreCliente("Patricia Silva");
        pedido5.setEmailCliente("patricia.silva@email.cl");
        pedido5.setTelefonoCliente("+56934567890");
        pedido5.setDireccionEntrega("Av. Alemania 456, Temuco");
        pedido5.setCiudad("Temuco");
        pedido5.setRegion("Región de La Araucanía");
        pedido5.setTotal(new BigDecimal("85000"));
        pedido5.setEstado(Pedido.EstadoPedido.CONFIRMADO);
        
        DetallePedido detalle7 = new DetallePedido();
        detalle7.setProductoId(9L);
        detalle7.setNombreProducto("Perfume Temuco");
        detalle7.setCantidad(1);
        detalle7.setPrecioUnitario(new BigDecimal("44000"));
        detalle7.setSubtotal(new BigDecimal("44000"));
        detalle7.setPedido(pedido5);
        
        DetallePedido detalle8 = new DetallePedido();
        detalle8.setProductoId(8L);
        detalle8.setNombreProducto("Aroma Viña del Mar");
        detalle8.setCantidad(1);
        detalle8.setPrecioUnitario(new BigDecimal("41000"));
        detalle8.setSubtotal(new BigDecimal("41000"));
        detalle8.setPedido(pedido5);
        
        pedido5.setDetalles(Arrays.asList(detalle7, detalle8));
        
        Envio envio5 = new Envio();
        envio5.setEmpresaTransporte("Correos de Chile");
        envio5.setEstado(Envio.EstadoEnvio.PREPARANDO);
        envio5.setObservaciones("Preparando para envío");
        envio5.setPedido(pedido5);
        
        pedido5.setEnvio(envio5);
        
        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2, pedido3, pedido4, pedido5);
        pedidoRepositorio.saveAll(pedidos);
        
        log.info("Se han cargado {} pedidos chilenos con sus respectivos envíos exitosamente", pedidos.size());
    }
}
