package com.perfulandia.envios.servicio;

import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.repositorio.PedidoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Pedido Servicio")
class PedidoServicioTest {

    @Mock
    private PedidoRepositorio pedidoRepositorio;

    @InjectMocks
    private PedidoServicio pedidoServicio;

    private Pedido pedido;
    private List<Pedido> pedidos;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNombreCliente("María González");
        pedido.setEmailCliente("maria.gonzalez@email.cl");
        pedido.setTelefonoCliente("+56912345678");
        pedido.setDireccionEntrega("Av. Providencia 1234");
        pedido.setCiudad("Santiago");
        pedido.setRegion("Región Metropolitana");
        pedido.setTotal(new BigDecimal("87000"));
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);

        Pedido pedido2 = new Pedido();
        pedido2.setId(2L);
        pedido2.setNombreCliente("Carlos Rodríguez");
        pedido2.setEmailCliente("carlos.rodriguez@email.cl");
        pedido2.setEstado(Pedido.EstadoPedido.CONFIRMADO);

        pedidos = Arrays.asList(pedido, pedido2);
    }

    @Test
    @DisplayName("Debería obtener todos los pedidos")
    void deberiaObtenerTodosLosPedidos() {
        // Given
        when(pedidoRepositorio.findAll()).thenReturn(pedidos);

        // When
        List<Pedido> resultado = pedidoServicio.obtenerTodosLosPedidos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactlyElementsOf(pedidos);
        verify(pedidoRepositorio).findAll();
    }

    @Test
    @DisplayName("Debería obtener pedido por ID cuando existe")
    void deberiaObtenerPedidoPorIdCuandoExiste() {
        // Given
        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));

        // When
        Optional<Pedido> resultado = pedidoServicio.obtenerPedidoPorId(1L);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombreCliente()).isEqualTo("María González");
        verify(pedidoRepositorio).findById(1L);
    }

    @Test
    @DisplayName("Debería crear pedido exitosamente")
    void deberiaCrearPedidoExitosamente() {
        // Given
        when(pedidoRepositorio.save(any(Pedido.class))).thenReturn(pedido);

        // When
        Pedido resultado = pedidoServicio.crearPedido(pedido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreCliente()).isEqualTo("María González");
        verify(pedidoRepositorio).save(pedido);
    }

    @Test
    @DisplayName("Debería actualizar pedido cuando existe")
    void deberiaActualizarPedidoCuandoExiste() {
        // Given
        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setNombreCliente("María González Actualizada");
        pedidoActualizado.setEmailCliente("maria.nueva@email.cl");
        pedidoActualizado.setTelefonoCliente("+56987654321");
        pedidoActualizado.setDireccionEntrega("Nueva Dirección 456");
        pedidoActualizado.setCiudad("Valparaíso");
        pedidoActualizado.setRegion("Región de Valparaíso");
        pedidoActualizado.setTotal(new BigDecimal("95000"));

        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositorio.save(any(Pedido.class))).thenReturn(pedido);

        // When
        Optional<Pedido> resultado = pedidoServicio.actualizarPedido(1L, pedidoActualizado);

        // Then
        assertThat(resultado).isPresent();
        verify(pedidoRepositorio).findById(1L);
        verify(pedidoRepositorio).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Debería actualizar estado del pedido")
    void deberiaActualizarEstadoDelPedido() {
        // Given
        when(pedidoRepositorio.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositorio.save(any(Pedido.class))).thenReturn(pedido);

        // When
        Optional<Pedido> resultado = pedidoServicio.actualizarEstadoPedido(1L, Pedido.EstadoPedido.CONFIRMADO);

        // Then
        assertThat(resultado).isPresent();
        verify(pedidoRepositorio).findById(1L);
        verify(pedidoRepositorio).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Debería buscar pedidos por estado")
    void deberiaBuscarPedidosPorEstado() {
        // Given
        when(pedidoRepositorio.findByEstado(Pedido.EstadoPedido.PENDIENTE))
                .thenReturn(Arrays.asList(pedido));

        // When
        List<Pedido> resultado = pedidoServicio.buscarPorEstado(Pedido.EstadoPedido.PENDIENTE);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo(Pedido.EstadoPedido.PENDIENTE);
        verify(pedidoRepositorio).findByEstado(Pedido.EstadoPedido.PENDIENTE);
    }

    @Test
    @DisplayName("Debería buscar pedidos por email del cliente")
    void deberiaBuscarPedidosPorEmailCliente() {
        // Given
        when(pedidoRepositorio.findByEmailCliente("maria.gonzalez@email.cl"))
                .thenReturn(Arrays.asList(pedido));

        // When
        List<Pedido> resultado = pedidoServicio.buscarPorEmailCliente("maria.gonzalez@email.cl");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmailCliente()).isEqualTo("maria.gonzalez@email.cl");
        verify(pedidoRepositorio).findByEmailCliente("maria.gonzalez@email.cl");
    }

    @Test
    @DisplayName("Debería buscar pedidos por ciudad")
    void deberiaBuscarPedidosPorCiudad() {
        // Given
        when(pedidoRepositorio.findByCiudad("Santiago")).thenReturn(Arrays.asList(pedido));

        // When
        List<Pedido> resultado = pedidoServicio.buscarPorCiudad("Santiago");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCiudad()).isEqualTo("Santiago");
        verify(pedidoRepositorio).findByCiudad("Santiago");
    }

    @Test
    @DisplayName("Debería buscar pedidos por región")
    void deberiaBuscarPedidosPorRegion() {
        // Given
        when(pedidoRepositorio.findByRegion("Región Metropolitana"))
                .thenReturn(Arrays.asList(pedido));

        // When
        List<Pedido> resultado = pedidoServicio.buscarPorRegion("Región Metropolitana");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRegion()).isEqualTo("Región Metropolitana");
        verify(pedidoRepositorio).findByRegion("Región Metropolitana");
    }

    @Test
    @DisplayName("Debería buscar pedidos por rango de fechas")
    void deberiaBuscarPedidosPorRangoFechas() {
        // Given
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(7);
        LocalDateTime fechaFin = LocalDateTime.now();
        when(pedidoRepositorio.buscarPorRangoFechas(fechaInicio, fechaFin))
                .thenReturn(Arrays.asList(pedido));

        // When
        List<Pedido> resultado = pedidoServicio.buscarPorRangoFechas(fechaInicio, fechaFin);

        // Then
        assertThat(resultado).hasSize(1);
        verify(pedidoRepositorio).buscarPorRangoFechas(fechaInicio, fechaFin);
    }

    @Test
    @DisplayName("Debería buscar pedidos por nombre del cliente")
    void deberiaBuscarPedidosPorNombreCliente() {
        // Given
        when(pedidoRepositorio.buscarPorNombreCliente("María"))
                .thenReturn(Arrays.asList(pedido));

        // When
        List<Pedido> resultado = pedidoServicio.buscarPorNombreCliente("María");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreCliente()).contains("María");
        verify(pedidoRepositorio).buscarPorNombreCliente("María");
    }
}
