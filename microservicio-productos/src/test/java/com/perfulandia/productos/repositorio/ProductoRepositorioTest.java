package com.perfulandia.productos.repositorio;

import com.perfulandia.productos.entidad.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de Repositorio - Producto")
class ProductoRepositorioTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    private Producto producto1;
    private Producto producto2;
    private Producto productoInactivo;

    @BeforeEach
    void setUp() {
        producto1 = new Producto();
        producto1.setNombre("Perfume Andino");
        producto1.setDescripcion("Fragancia de los Andes");
        producto1.setMarca("Perfulandia");
        producto1.setPrecio(new BigDecimal("45000"));
        producto1.setStock(50);
        producto1.setCategoria("Unisex");
        producto1.setTamaño("100ml");
        producto1.setActivo(true);

        producto2 = new Producto();
        producto2.setNombre("Brisa Valparaíso");
        producto2.setDescripcion("Fragancia marina");
        producto2.setMarca("Perfulandia");
        producto2.setPrecio(new BigDecimal("38000"));
        producto2.setStock(5); // Stock bajo
        producto2.setCategoria("Femenino");
        producto2.setTamaño("75ml");
        producto2.setActivo(true);

        productoInactivo = new Producto();
        productoInactivo.setNombre("Producto Inactivo");
        productoInactivo.setMarca("Perfulandia");
        productoInactivo.setPrecio(new BigDecimal("30000"));
        productoInactivo.setStock(20);
        productoInactivo.setActivo(false);

        entityManager.persistAndFlush(producto1);
        entityManager.persistAndFlush(producto2);
        entityManager.persistAndFlush(productoInactivo);
    }

    @Test
    @DisplayName("Debería encontrar solo productos activos")
    void deberiaEncontrarSoloProductosActivos() {
        // When
        List<Producto> productos = productoRepositorio.findByActivoTrue();

        // Then
        assertThat(productos).hasSize(2);
        assertThat(productos).allMatch(Producto::getActivo);
        assertThat(productos).extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Perfume Andino", "Brisa Valparaíso");
    }

    @Test
    @DisplayName("Debería encontrar productos por marca y activos")
    void deberiaEncontrarProductosPorMarcaYActivos() {
        // When
        List<Producto> productos = productoRepositorio.findByMarcaAndActivoTrue("Perfulandia");

        // Then
        assertThat(productos).hasSize(2);
        assertThat(productos).allMatch(p -> p.getMarca().equals("Perfulandia"));
        assertThat(productos).allMatch(Producto::getActivo);
    }

    @Test
    @DisplayName("Debería encontrar productos por categoría y activos")
    void deberiaEncontrarProductosPorCategoriaYActivos() {
        // When
        List<Producto> productos = productoRepositorio.findByCategoriaAndActivoTrue("Unisex");

        // Then
        assertThat(productos).hasSize(1);
        assertThat(productos.get(0).getCategoria()).isEqualTo("Unisex");
        assertThat(productos.get(0).getNombre()).isEqualTo("Perfume Andino");
    }

    @Test
    @DisplayName("Debería buscar productos por nombre parcial")
    void deberiaBuscarProductosPorNombreParcial() {
        // When
        List<Producto> productos = productoRepositorio.buscarPorNombre("Perfume");

        // Then
        assertThat(productos).hasSize(1);
        assertThat(productos.get(0).getNombre()).contains("Perfume");
    }

    @Test
    @DisplayName("Debería encontrar productos con stock bajo")
    void deberiaEncontrarProductosConStockBajo() {
        // When
        List<Producto> productos = productoRepositorio.buscarProductosConStockBajo(10);

        // Then
        assertThat(productos).hasSize(1);
        assertThat(productos.get(0).getStock()).isLessThan(10);
        assertThat(productos.get(0).getNombre()).isEqualTo("Brisa Valparaíso");
    }

    @Test
    @DisplayName("Debería encontrar producto por ID y activo")
    void deberiaEncontrarProductoPorIdYActivo() {
        // When
        Optional<Producto> producto = productoRepositorio.findByIdAndActivoTrue(producto1.getId());

        // Then
        assertThat(producto).isPresent();
        assertThat(producto.get().getNombre()).isEqualTo("Perfume Andino");
        assertThat(producto.get().getActivo()).isTrue();
    }

    @Test
    @DisplayName("No debería encontrar producto inactivo por ID")
    void noDeberiaEncontrarProductoInactivoPorId() {
        // When
        Optional<Producto> producto = productoRepositorio.findByIdAndActivoTrue(productoInactivo.getId());

        // Then
        assertThat(producto).isEmpty();
    }

    @Test
    @DisplayName("Debería persistir producto con fechas automáticas")
    void deberiaPersistirProductoConFechasAutomaticas() {
        // Given
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Nuevo Perfume");
        nuevoProducto.setMarca("Perfulandia");
        nuevoProducto.setPrecio(new BigDecimal("40000"));
        nuevoProducto.setStock(25);
        nuevoProducto.setActivo(true);

        // When
        Producto productoGuardado = productoRepositorio.save(nuevoProducto);

        // Then
        assertThat(productoGuardado.getId()).isNotNull();
        assertThat(productoGuardado.getFechaCreacion()).isNotNull();
        assertThat(productoGuardado.getFechaActualizacion()).isNotNull();
    }
}
