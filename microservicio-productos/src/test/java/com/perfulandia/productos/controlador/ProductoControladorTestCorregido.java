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
import org.springframework.hateoas.MediaTypes;
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
@DisplayName("Pruebas Unitarias - Producto Controlador (Corregido)")
class ProductoControladorTestCorregido {

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
    @DisplayName("GET /api/productos - Debería retornar lista de productos con HAL+JSON")
    void deberiaRetornarListaDeProductosConHalJson() throws Exception {
        // Given
        when(productoServicio.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre", is("Perfume Test")))
                .andExpect(jsonPath("$._embedded.productoList[0].marca", is("Perfulandia")))
                .andExpect(jsonPath("$._embedded.productoList[0].precio", is(45000)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/productos")));
    }

    @Test
    @DisplayName("GET /api/productos/{id} - Debería retornar producto por ID con HAL+JSON")
    void deberiaRetornarProductoPorIdConHalJson() throws Exception {
        // Given
        when(productoServicio.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre", is("Perfume Test")))
                .andExpect(jsonPath("$.marca", is("Perfulandia")))
                .andExpect(jsonPath("$.precio", is(45000)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/productos/1")))
                .andExpect(jsonPath("$._links.productos.href", containsString("/api/productos")))
                .andExpect(jsonPath("$._links.actualizar.href", containsString("/api/productos/1")))
                .andExpect(jsonPath("$._links.eliminar.href", containsString("/api/productos/1")));
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
    @DisplayName("POST /api/productos - Debería crear producto exitosamente con HAL+JSON")
    void deberiaCrearProductoExitosamenteConHalJson() throws Exception {
        // Given
        when(productoServicio.crearProducto(any(Producto.class))).thenReturn(producto);

        // When & Then
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre", is("Perfume Test")))
                .andExpect(jsonPath("$.marca", is("Perfulandia")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/productos/1")))
                .andExpect(jsonPath("$._links.productos.href", containsString("/api/productos")));
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
    @DisplayName("PUT /api/productos/{id} - Debería actualizar producto exitosamente con HAL+JSON")
    void deberiaActualizarProductoExitosamenteConHalJson() throws Exception {
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
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.nombre", is("Perfume Actualizado")))
                .andExpect(jsonPath("$.precio", is(50000)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/productos/1")));
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
    @DisplayName("GET /api/productos/buscar/marca/{marca} - Debería buscar por marca con HAL+JSON")
    void deberiaBuscarPorMarcaConHalJson() throws Exception {
        // Given
        when(productoServicio.buscarPorMarca("Perfulandia")).thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/buscar/marca/Perfulandia"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].marca", is("Perfulandia")));
    }

    @Test
    @DisplayName("GET /api/productos/buscar/categoria/{categoria} - Debería buscar por categoría")
    void deberiaBuscarPorCategoria() throws Exception {
        // Given
        when(productoServicio.buscarPorCategoria("Unisex")).thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/buscar/categoria/Unisex"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].categoria", is("Unisex")));
    }

    @Test
    @DisplayName("GET /api/productos/buscar/nombre - Debería buscar por nombre")
    void deberiaBuscarPorNombre() throws Exception {
        // Given
        when(productoServicio.buscarPorNombre("Test")).thenReturn(Arrays.asList(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/buscar/nombre?nombre=Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre", containsString("Test")));
    }

    @Test
    @DisplayName("GET /api/productos/stock-bajo - Debería obtener productos con stock bajo con HAL+JSON")
    void deberiaObtenerProductosConStockBajoConHalJson() throws Exception {
        // Given
        Producto productoStockBajo = new Producto();
        productoStockBajo.setId(2L);
        productoStockBajo.setNombre("Producto Stock Bajo");
        productoStockBajo.setMarca("Perfulandia");
        productoStockBajo.setPrecio(new BigDecimal("30000"));
        productoStockBajo.setStock(5);
        productoStockBajo.setActivo(true);
        
        when(productoServicio.obtenerProductosConStockBajo(10)).thenReturn(Arrays.asList(productoStockBajo));

        // When & Then
        mockMvc.perform(get("/api/productos/stock-bajo?stockMinimo=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.productoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.productoList[0].stock", is(5)))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre", is("Producto Stock Bajo")));
    }

    @Test
    @DisplayName("Debería verificar estructura HATEOAS completa")
    void deberiaVerificarEstructuraHateoasCompleta() throws Exception {
        // Given
        when(productoServicio.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto));

        // When & Then
        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Perfume Test")))
                .andExpect(jsonPath("$.descripcion", is("Descripción de prueba")))
                .andExpect(jsonPath("$.marca", is("Perfulandia")))
                .andExpect(jsonPath("$.precio", is(45000)))
                .andExpect(jsonPath("$.stock", is(50)))
                .andExpect(jsonPath("$.categoria", is("Unisex")))
                .andExpect(jsonPath("$.tamaño", is("100ml")))
                .andExpect(jsonPath("$.activo", is(true)))
                .andExpect(jsonPath("$._links", notNullValue()))
                .andExpect(jsonPath("$._links.self", notNullValue()))
                .andExpect(jsonPath("$._links.productos", notNullValue()))
                .andExpect(jsonPath("$._links.actualizar", notNullValue()))
                .andExpect(jsonPath("$._links.eliminar", notNullValue()));
    }
}
