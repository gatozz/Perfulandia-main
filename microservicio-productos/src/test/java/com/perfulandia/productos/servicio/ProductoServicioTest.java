package com.perfulandia.productos.servicio;

import com.perfulandia.productos.entidad.Producto;
import com.perfulandia.productos.repositorio.ProductoRepositorio;
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
@DisplayName("Pruebas Unitarias - Producto Servicio")
class ProductoServicioTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    @InjectMocks
    private ProductoServicio productoServicio;

    private Producto producto;
    private List<Producto> productos;

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

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Perfume Test 2");
        producto2.setMarca("Perfulandia");
        producto2.setPrecio(new BigDecimal("38000"));
        producto2.setStock(30);
        producto2.setActivo(true);

        productos = Arrays.asList(producto, producto2);
    }

    @Test
    @DisplayName("Debería obtener todos los productos activos")
    void deberiaObtenerTodosLosProductosActivos() {
        // Given
        when(productoRepositorio.findByActivoTrue()).thenReturn(productos);

        // When
        List<Producto> resultado = productoServicio.obtenerTodosLosProductos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactlyElementsOf(productos);
        verify(productoRepositorio).findByActivoTrue();
    }

    @Test
    @DisplayName("Debería obtener producto por ID cuando existe")
    void deberiaObtenerProductoPorIdCuandoExiste() {
        // Given
        when(productoRepositorio.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));

        // When
        Optional<Producto> resultado = productoServicio.obtenerProductoPorId(1L);

        // Then
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Perfume Test");
        verify(productoRepositorio).findByIdAndActivoTrue(1L);
    }

    @Test
    @DisplayName("Debería retornar vacío cuando producto no existe")
    void deberiaRetornarVacioCuandoProductoNoExiste() {
        // Given
        when(productoRepositorio.findByIdAndActivoTrue(999L)).thenReturn(Optional.empty());

        // When
        Optional<Producto> resultado = productoServicio.obtenerProductoPorId(999L);

        // Then
        assertThat(resultado).isEmpty();
        verify(productoRepositorio).findByIdAndActivoTrue(999L);
    }

    @Test
    @DisplayName("Debería crear producto exitosamente")
    void deberiaCrearProductoExitosamente() {
        // Given
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto);

        // When
        Producto resultado = productoServicio.crearProducto(producto);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombre()).isEqualTo("Perfume Test");
        verify(productoRepositorio).save(producto);
    }

    @Test
    @DisplayName("Debería actualizar producto cuando existe")
    void deberiaActualizarProductoCuandoExiste() {
        // Given
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Perfume Actualizado");
        productoActualizado.setDescripcion("Nueva descripción");
        productoActualizado.setMarca("Perfulandia");
        productoActualizado.setPrecio(new BigDecimal("50000"));
        productoActualizado.setStock(60);
        productoActualizado.setCategoria("Masculino");
        productoActualizado.setTamaño("75ml");

        when(productoRepositorio.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto);

        // When
        Optional<Producto> resultado = productoServicio.actualizarProducto(1L, productoActualizado);

        // Then
        assertThat(resultado).isPresent();
        verify(productoRepositorio).findByIdAndActivoTrue(1L);
        verify(productoRepositorio).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debería eliminar producto lógicamente")
    void deberiaEliminarProductoLogicamente() {
        // Given
        when(productoRepositorio.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(producto));
        when(productoRepositorio.save(any(Producto.class))).thenReturn(producto);

        // When
        boolean resultado = productoServicio.eliminarProducto(1L);

        // Then
        assertThat(resultado).isTrue();
        verify(productoRepositorio).findByIdAndActivoTrue(1L);
        verify(productoRepositorio).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debería retornar false al eliminar producto inexistente")
    void deberiaRetornarFalseAlEliminarProductoInexistente() {
        // Given
        when(productoRepositorio.findByIdAndActivoTrue(999L)).thenReturn(Optional.empty());

        // When
        boolean resultado = productoServicio.eliminarProducto(999L);

        // Then
        assertThat(resultado).isFalse();
        verify(productoRepositorio).findByIdAndActivoTrue(999L);
        verify(productoRepositorio, never()).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debería buscar productos por marca")
    void deberiaBuscarProductosPorMarca() {
        // Given
        when(productoRepositorio.findByMarcaAndActivoTrue("Perfulandia")).thenReturn(productos);

        // When
        List<Producto> resultado = productoServicio.buscarPorMarca("Perfulandia");

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(p -> p.getMarca().equals("Perfulandia"));
        verify(productoRepositorio).findByMarcaAndActivoTrue("Perfulandia");
    }

    @Test
    @DisplayName("Debería buscar productos por categoría")
    void deberiaBuscarProductosPorCategoria() {
        // Given
        when(productoRepositorio.findByCategoriaAndActivoTrue("Unisex")).thenReturn(Arrays.asList(producto));

        // When
        List<Producto> resultado = productoServicio.buscarPorCategoria("Unisex");

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCategoria()).isEqualTo("Unisex");
        verify(productoRepositorio).findByCategoriaAndActivoTrue("Unisex");
    }

    @Test
    @DisplayName("Debería buscar productos por nombre")
    void deberiaBuscarProductosPorNombre() {
        // Given
        when(productoRepositorio.buscarPorNombre("Test")).thenReturn(productos);

        // When
        List<Producto> resultado = productoServicio.buscarPorNombre("Test");

        // Then
        assertThat(resultado).hasSize(2);
        verify(productoRepositorio).buscarPorNombre("Test");
    }

    @Test
    @DisplayName("Debería obtener productos con stock bajo")
    void deberiaObtenerProductosConStockBajo() {
        // Given
        Producto productoStockBajo = new Producto();
        productoStockBajo.setStock(5);
        when(productoRepositorio.buscarProductosConStockBajo(10)).thenReturn(Arrays.asList(productoStockBajo));

        // When
        List<Producto> resultado = productoServicio.obtenerProductosConStockBajo(10);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStock()).isLessThan(10);
        verify(productoRepositorio).buscarProductosConStockBajo(10);
    }
}
