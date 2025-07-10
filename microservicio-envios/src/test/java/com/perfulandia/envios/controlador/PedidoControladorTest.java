package com.perfulandia.envios.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.servicio.PedidoServicio;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoControlador.class)
@DisplayName("Pruebas Unitarias - Pedido Controlador")
class PedidoControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoServicio pedidoServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;

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
    }

    @Test
    @DisplayName("GET /api/pedidos - Debería retornar lista de pedidos con HAL+JSON")
    void deberiaRetornarListaDePedidos() throws Exception {
        // Given
        when(pedidoServicio.obtenerTodosLosPedidos()).thenReturn(Arrays.asList(pedido));

        // When & Then
        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.pedidoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pedidoList[0].nombreCliente", is("María González")))
                .andExpect(jsonPath("$._embedded.pedidoList[0].emailCliente", is("maria.gonzalez@email.cl")))
                .andExpect(jsonPath("$._embedded.pedidoList[0].total", is(87000)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/pedidos")));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} - Debería retornar pedido por ID con HAL+JSON")
    void deberiaRetornarPedidoPorId() throws Exception {
        // Given
        when(pedidoServicio.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedido));

        // When & Then
        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombreCliente", is("María González")))
                .andExpect(jsonPath("$.emailCliente", is("maria.gonzalez@email.cl")))
                .andExpect(jsonPath("$.ciudad", is("Santiago")))
                .andExpect(jsonPath("$.total", is(87000)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/pedidos/1")))
                .andExpect(jsonPath("$._links.pedidos.href", containsString("/api/pedidos")));
    }

    @Test
    @DisplayName("GET /api/pedidos/{id} - Debería retornar 404 cuando pedido no existe")
    void deberiaRetornar404CuandoPedidoNoExiste() throws Exception {
        // Given
        when(pedidoServicio.obtenerPedidoPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/pedidos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/pedidos - Debería crear pedido exitosamente con HAL+JSON")
    void deberiaCrearPedidoExitosamente() throws Exception {
        // Given
        when(pedidoServicio.crearPedido(any(Pedido.class))).thenReturn(pedido);

        // When & Then
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombreCliente", is("María González")))
                .andExpect(jsonPath("$.emailCliente", is("maria.gonzalez@email.cl")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/pedidos/1")));
    }

    @Test
    @DisplayName("POST /api/pedidos - Debería retornar 400 con datos inválidos")
    void deberiaRetornar400ConDatosInvalidos() throws Exception {
        // Given
        Pedido pedidoInvalido = new Pedido();
        // Sin nombre del cliente (campo obligatorio)

        // When & Then
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/pedidos/{id} - Debería actualizar pedido exitosamente")
    void deberiaActualizarPedidoExitosamente() throws Exception {
        // Given
        Pedido pedidoActualizado = new Pedido();
        pedidoActualizado.setId(1L);
        pedidoActualizado.setNombreCliente("María González Actualizada");
        pedidoActualizado.setEmailCliente("maria.nueva@email.cl");
        pedidoActualizado.setTelefonoCliente("+56987654321");
        pedidoActualizado.setDireccionEntrega("Nueva Dirección");
        pedidoActualizado.setCiudad("Valparaíso");
        pedidoActualizado.setRegion("Región de Valparaíso");
        pedidoActualizado.setTotal(new BigDecimal("95000"));

        when(pedidoServicio.actualizarPedido(anyLong(), any(Pedido.class)))
                .thenReturn(Optional.of(pedidoActualizado));

        // When & Then
        mockMvc.perform(put("/api/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombreCliente", is("María González Actualizada")))
                .andExpect(jsonPath("$.emailCliente", is("maria.nueva@email.cl")))
                .andExpect(jsonPath("$.ciudad", is("Valparaíso")));
    }

    @Test
    @DisplayName("PATCH /api/pedidos/{id}/estado - Debería actualizar estado del pedido")
    void deberiaActualizarEstadoPedido() throws Exception {
        // Given
        pedido.setEstado(Pedido.EstadoPedido.CONFIRMADO);
        when(pedidoServicio.actualizarEstadoPedido(1L, Pedido.EstadoPedido.CONFIRMADO))
                .thenReturn(Optional.of(pedido));

        // When & Then
        mockMvc.perform(patch("/api/pedidos/1/estado")
                        .param("estado", "CONFIRMADO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.estado", is("CONFIRMADO")));
    }

    @Test
    @DisplayName("GET /api/pedidos/estado/{estado} - Debería buscar por estado")
    void deberiaBuscarPorEstado() throws Exception {
        // Given
        when(pedidoServicio.buscarPorEstado(Pedido.EstadoPedido.PENDIENTE))
                .thenReturn(Arrays.asList(pedido));

        // When & Then
        mockMvc.perform(get("/api/pedidos/estado/PENDIENTE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.pedidoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pedidoList[0].estado", is("PENDIENTE")));
    }

    @Test
    @DisplayName("GET /api/pedidos/cliente/{email} - Debería buscar por email del cliente")
    void deberiaBuscarPorEmailCliente() throws Exception {
        // Given
        when(pedidoServicio.buscarPorEmailCliente("maria.gonzalez@email.cl"))
                .thenReturn(Arrays.asList(pedido));

        // When & Then
        mockMvc.perform(get("/api/pedidos/cliente/maria.gonzalez@email.cl"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.pedidoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pedidoList[0].emailCliente", is("maria.gonzalez@email.cl")));
    }

    @Test
    @DisplayName("GET /api/pedidos/ciudad/{ciudad} - Debería buscar por ciudad")
    void deberiaBuscarPorCiudad() throws Exception {
        // Given
        when(pedidoServicio.buscarPorCiudad("Santiago")).thenReturn(Arrays.asList(pedido));

        // When & Then
        mockMvc.perform(get("/api/pedidos/ciudad/Santiago"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.pedidoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.pedidoList[0].ciudad", is("Santiago")));
    }

    @Test
    @DisplayName("GET /api/pedidos/fechas - Debería buscar por rango de fechas")
    void deberiaBuscarPorRangoFechas() throws Exception {
        // Given
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(7);
        LocalDateTime fechaFin = LocalDateTime.now();
        when(pedidoServicio.buscarPorRangoFechas(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(pedido));

        // When & Then
        mockMvc.perform(get("/api/pedidos/fechas")
                        .param("fechaInicio", fechaInicio.toString())
                        .param("fechaFin", fechaFin.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.pedidoList", hasSize(1)));
    }
}
