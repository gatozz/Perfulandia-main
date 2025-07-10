package com.perfulandia.envios.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.envios.entidad.Envio;
import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.servicio.EnvioServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioControlador.class)
@DisplayName("Pruebas Unitarias - Envío Controlador")
class EnvioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioServicio envioServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private Envio envio;
    private Pedido pedido;

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
    }

    @Test
    @DisplayName("GET /api/envios - Debería retornar lista de envíos con HAL+JSON")
    void deberiaRetornarListaDeEnvios() throws Exception {
        // Given
        when(envioServicio.obtenerTodosLosEnvios()).thenReturn(Arrays.asList(envio));

        // When & Then
        mockMvc.perform(get("/api/envios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.envioList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.envioList[0].codigoSeguimiento", is("PFL123456789")))
                .andExpect(jsonPath("$._embedded.envioList[0].empresaTransporte", is("Chilexpress")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/envios")));
    }

    @Test
    @DisplayName("GET /api/envios/{id} - Debería retornar envío por ID con HAL+JSON")
    void deberiaRetornarEnvioPorId() throws Exception {
        // Given
        when(envioServicio.obtenerEnvioPorId(1L)).thenReturn(Optional.of(envio));

        // When & Then
        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.codigoSeguimiento", is("PFL123456789")))
                .andExpect(jsonPath("$.empresaTransporte", is("Chilexpress")))
                .andExpect(jsonPath("$.estado", is("PREPARANDO")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/envios/1")))
                .andExpect(jsonPath("$._links.envios.href", containsString("/api/envios")));
    }

    @Test
    @DisplayName("GET /api/envios/seguimiento/{codigo} - Debería rastrear envío por código")
    void deberiaRastrearEnvioPorCodigo() throws Exception {
        // Given
        when(envioServicio.obtenerEnvioPorCodigoSeguimiento("PFL123456789"))
                .thenReturn(Optional.of(envio));

        // When & Then
        mockMvc.perform(get("/api/envios/seguimiento/PFL123456789"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.codigoSeguimiento", is("PFL123456789")))
                .andExpect(jsonPath("$.empresaTransporte", is("Chilexpress")))
                .andExpect(jsonPath("$.estado", is("PREPARANDO")))
                .andExpect(jsonPath("$._links.rastrear.href", containsString("/api/envios/seguimiento/PFL123456789")));
    }

    @Test
    @DisplayName("GET /api/envios/pedido/{pedidoId} - Debería obtener envío por pedido")
    void deberiaObtenerEnvioPorPedido() throws Exception {
        // Given
        when(envioServicio.obtenerEnvioPorPedido(1L)).thenReturn(Optional.of(envio));

        // When & Then
        mockMvc.perform(get("/api/envios/pedido/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.codigoSeguimiento", is("PFL123456789")))
                .andExpect(jsonPath("$.empresaTransporte", is("Chilexpress")));
    }

    @Test
    @DisplayName("POST /api/envios - Debería crear envío exitosamente")
    void deberiaCrearEnvioExitosamente() throws Exception {
        // Given
        when(envioServicio.crearEnvio(any(Envio.class))).thenReturn(envio);

        // When & Then
        mockMvc.perform(post("/api/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(envio)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.codigoSeguimiento", is("PFL123456789")))
                .andExpect(jsonPath("$.empresaTransporte", is("Chilexpress")));
    }

    @Test
    @DisplayName("PATCH /api/envios/{id}/estado - Debería actualizar estado del envío")
    void deberiaActualizarEstadoEnvio() throws Exception {
        // Given
        envio.setEstado(Envio.EstadoEnvio.EN_TRANSITO);
        when(envioServicio.actualizarEstadoEnvio(1L, Envio.EstadoEnvio.EN_TRANSITO))
                .thenReturn(Optional.of(envio));

        // When & Then
        mockMvc.perform(patch("/api/envios/1/estado")
                        .param("estado", "EN_TRANSITO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.estado", is("EN_TRANSITO")));
    }

    @Test
    @DisplayName("GET /api/envios/estado/{estado} - Debería buscar por estado")
    void deberiaBuscarPorEstado() throws Exception {
        // Given
        when(envioServicio.buscarPorEstado(Envio.EstadoEnvio.PREPARANDO))
                .thenReturn(Arrays.asList(envio));

        // When & Then
        mockMvc.perform(get("/api/envios/estado/PREPARANDO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.envioList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.envioList[0].estado", is("PREPARANDO")));
    }

    @Test
    @DisplayName("GET /api/envios/empresa/{empresa} - Debería buscar por empresa de transporte")
    void deberiaBuscarPorEmpresaTransporte() throws Exception {
        // Given
        when(envioServicio.buscarPorEmpresaTransporte("Chilexpress"))
                .thenReturn(Arrays.asList(envio));

        // When & Then
        mockMvc.perform(get("/api/envios/empresa/Chilexpress"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.envioList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.envioList[0].empresaTransporte", is("Chilexpress")));
    }
}
