package com.perfulandia.envios.servicio;

import com.perfulandia.envios.entidad.Envio;
import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.repositorio.EnvioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - Envío Servicio")
class EnvioServicioTest {

    @Mock
    private EnvioRepositorio envioRepositorio;

    @InjectMocks
    private EnvioServicio envioServicio;

    private Envio envio;
    private Pedido pedido;
    private List<Envio> envios;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNombreCliente("María González");
        pedido.setEmailCliente("maria.gonzalez@email.cl");
        pedido.setTotal(new BigDecimal("87000"));

        envio = new Envio();
        envio.setId(1L);
        envio.setCodigoSeguimiento("PFL123456789");
        envio.setEmpresaTransporte("Chilexpress");
        envio.setEstado(Envio.EstadoEnvio.PREPARANDO);
        envio.setObservaciones("Envío de prueba");
        envio.setPedido(pedido);

        Envio envio2 = new Envio();
        envio2.setId(2L);
        envio2.setCodigoSeguimiento("PFL987654321");
        envio2.setEmpresaTransporte("Correos de Chile");
        envio2.setEstado(Envio.EstadoEnvio.EN_TRANSITO);

        envios = Arrays.asList(envio, envio2);
    }

    @Test
    @DisplayName("Debería obtener todos los envíos")
    void deberiaObtenerTodosLosEnvios() {
        // Given
        when(envioRepositorio.findAll()).thenReturn(envios);

        // When
        List<Envio> resultado = envioServicio.obtenerTodosLosEnvios();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactlyElementsOf(envios);
        verify(envioRepositorio).findAll();
    }

    @Test
    @DisplayName("Debería obtener envío por ID cuando existe")
    void deberiaObtenerEnvioPorIdCuandoExiste() {
        // Given
        when(envioRepositorio.findById(1L)).thenReturn(Optional.of(envio));

        // When
        Optional<Envio> resultado = envioServicio.obtenerEnvioPorId(1L);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCodigoSeguimiento()).isEqualTo("PFL123456789");
        verify(envioRepositorio).findById(1L);
    }

    @Test
    @DisplayName("Debería obtener envío por código de seguimiento")
    void deberiaObtenerEnvioPorCodigoSeguimiento() {
        // Given
        when(envioRepositorio.findByCodigoSeguimiento("PFL123456789")).thenReturn(Optional.of(envio));

        // When
        Optional<Envio> resultado = envioServicio.obtenerEnvioPorCodigoSeguimiento("PFL123456789");

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCodigoSeguimiento()).isEqualTo("PFL123456789");
        verify(envioRepositorio).findByCodigoSeguimiento("PFL123456789");
    }

    @Test
    @DisplayName("Debería crear envío exitosamente")
    void deberiaCrearEnvioExitosamente() {
        // Given
        when(envioRepositorio.save(any(Envio.class))).thenReturn(envio);

        // When
        Envio resultado = envioServicio.crearEnvio(envio);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigoSeguimiento()).isEqualTo("PFL123456789");
        assertThat(resultado.getFechaEnvio()).isNotNull();
        assertThat(resultado.getFechaEntregaEstimada()).isNotNull();
        verify(envioRepositorio).save(envio);
    }

    @Test
    @DisplayName("Debería actualizar estado del envío")
    void deberiaActualizarEstadoDelEnvio() {
        // Given
        when(envioRepositorio.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepositorio.save(any(Envio.class))).thenReturn(envio);

        // When
        Optional<Envio> resultado = envioServicio.actualizarEstadoEnvio(1L, Envio.EstadoEnvio.ENTREGADO);

        // Then
        assertThat(resultado).isPresent();
        verify(envioRepositorio).findById(1L);
        verify(envioRepositorio).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debería buscar envíos por estado")
    void deberiaBuscarEnviosPorEstado() {
        // Given
        when(envioRepositorio.findByEstado(Envio.EstadoEnvio.PREPARANDO))
                .thenReturn(Arrays.asList(envio));

        // When
        List<Envio> resultado = envioServicio.buscarPorEstado(Envio.EstadoEnvio.PREPARANDO);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo(Envio.EstadoEnvio.PREPARANDO);
        verify(envioRepositorio).findByEstado(Envio.EstadoEnvio.PREPARANDO);
    }

    @Test
    @DisplayName("Debería buscar envíos por empresa de transporte")
    void deberiaBuscarEnviosPorEmpresaTransporte() {
        // Given
        when(envioRepositorio.findByEmpresaTransporte("Chilexpress"))
                .thenReturn(Arrays.asList(envio));

        // When
        List<Envio> resultado = envioServicio.buscarPorEmpresaTransporte("Chilexpress");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmpresaTransporte()).isEqualTo("Chilexpress");
        verify(envioRepositorio).findByEmpresaTransporte("Chilexpress");
    }
}
