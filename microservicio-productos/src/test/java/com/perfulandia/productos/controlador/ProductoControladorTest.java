package com.perfulandia.productos.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.productos.entidad.Producto;
import com.perfulandia.productos.servicio.ProductoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoControlador.class)
@DisplayName("Pruebas Unitarias - Producto Controlador")
class ProductoControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoServicio productoServicio;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Perfume Test");
        producto.setDescripcion("Descripción de prueba");
        producto.setMarca("Perfulandia");
        producto.setPrecio(new BigDecimal("45000"));
        producto.setStock(50);
        producto.setCategoria("Unisex");
        producto.setTamaño("100ml");
        producto.setActivo(true);
    }

    @Test
    @DisplayName("GET /api/productos - Debería retornar lista de productos")
    void deberiaRetornarListaDeProductos() throws Exception {
        // Given
        when(productoServicio.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre", is("Perfume Test")))
                .andExpect(jsonPath("$._embedded.productoList[0].marca", is("Perfulandia")))
                .andExpect(jsonPath("$._embedded.productoList[0].precio", is(45000)));
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debería retornar producto por ID")
    void deberiaRetornarProductoPorId() throws Exception {
        // Given
        when(productoServicio.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.nombre", is("Perfume Test")))
                .andExpect(jsonPath("$.marca", is("Perfulandia")))
                .andExpect(jsonPath("$.precio", is(45000)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/productos/1")));
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debería retornar 404 cuando producto no existe")
    void deberiaRetornar404CuandoProductoNoExiste() throws Exception {
        // Given
        when(productoServicio.obtenerProductoPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/productos - Debería crear producto exitosamente")
    void deberiaCrearProductoExitosamente() throws Exception {
        // Given
        when(productoServicio.crearProducto(any(Producto.class))).thenReturn(producto);

        // When & Then
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.nombre", is("Perfume Test")))
                .andExpect(jsonPath("$.marca", is("Perfulandia")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/productos/1")));
    }

    @Test
    @DisplayName("POST /api/productos - Debería retornar 400 con datos inválidos")
    void deberiaRetornar400ConDatosInvalidos() throws Exception {
        // Given
        Producto productoInvalido = new Producto();
        // Sin nombre ni marca (campos obligatorios)

        // When & Then
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/productos/{id} - Debería actualizar producto exitosamente")
    void deberiaActualizarProductoExitosamente() throws Exception {
        // Given
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Perfume Actualizado");
        productoActualizado.setMarca("Perfulandia");
        productoActualizado.setPrecio(new BigDecimal("50000"));
        productoActualizado.setStock(60);

        when(productoServicio.actualizarProducto(anyLong(), any(Producto.class)))
                .thenReturn(Optional.of(productoActualizado));

        // When & Then
        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.nombre", is("Perfume Actualizado")))
                .andExpect(jsonPath("$.precio", is(50000)));
    }

    @Test
    @DisplayName("DELETE /api/productos/{id} - Debería eliminar producto exitosamente")
    void deberiaEliminarProductoExitosamente() throws Exception {
        // Given
        when(productoServicio.eliminarProducto(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/productos/{id} - Debería retornar 404 cuando producto no existe")
    void deberiaRetornar404AlEliminarProductoInexistente() throws Exception {
        // Given
        when(productoServicio.eliminarProducto(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/productos/buscar/marca/{marca} - Debería buscar por marca")
    void deberiaBuscarPorMarca() throws Exception {
        // Given
        when(productoServicio.buscarPorMarca("Perfulandia")).thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/buscar/marca/Perfulandia"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].marca", is("Perfulandia")));
    }

    @Test
    @DisplayName("GET /api/productos/stock-bajo - Debería obtener productos con stock bajo")
    void deberiaObtenerProductosConStockBajo() throws Exception {
        // Given
        Producto productoStockBajo = new Producto();
        productoStockBajo.setId(2L);
        productoStockBajo.setNombre("Producto Stock Bajo");
        productoStockBajo.setStock(5);
        
        when(productoServicio.obtenerProductosConStockBajo(10)).thenReturn(Arrays.asList(productoStockBajo));

        // When & Then
        mockMvc.perform(get("/api/productos/stock-bajo?stockMinimo=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].stock", is(5)));
    }
}
