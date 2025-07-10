package com.perfulandia.productos.servicio;

import com.perfulandia.productos.entidad.Producto;
import com.perfulandia.productos.repositorio.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductoServicio {
    
    private final ProductoRepositorio productoRepositorio;
    
    public List<Producto> obtenerTodosLosProductos() {
        log.info("Obteniendo todos los productos activos");
        return productoRepositorio.findByActivoTrue();
    }
    
    public Optional<Producto> obtenerProductoPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoRepositorio.findByIdAndActivoTrue(id);
    }
    
    public Producto crearProducto(Producto producto) {
        log.info("Creando nuevo producto: {}", producto.getNombre());
        return productoRepositorio.save(producto);
    }
    
    public Optional<Producto> actualizarProducto(Long id, Producto productoActualizado) {
        log.info("Actualizando producto con ID: {}", id);
        return productoRepositorio.findByIdAndActivoTrue(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setMarca(productoActualizado.getMarca());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setStock(productoActualizado.getStock());
                    producto.setCategoria(productoActualizado.getCategoria());
                    producto.setTamaño(productoActualizado.getTamaño());
                    return productoRepositorio.save(producto);
                });
    }
    
    public boolean eliminarProducto(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        return productoRepositorio.findByIdAndActivoTrue(id)
                .map(producto -> {
                    producto.setActivo(false);
                    productoRepositorio.save(producto);
                    return true;
                })
                .orElse(false);
    }
    
    public List<Producto> buscarPorMarca(String marca) {
        log.info("Buscando productos por marca: {}", marca);
        return productoRepositorio.findByMarcaAndActivoTrue(marca);
    }
    
    public List<Producto> buscarPorCategoria(String categoria) {
        log.info("Buscando productos por categoría: {}", categoria);
        return productoRepositorio.findByCategoriaAndActivoTrue(categoria);
    }
    
    public List<Producto> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);
        return productoRepositorio.buscarPorNombre(nombre);
    }
    
    public List<Producto> obtenerProductosConStockBajo(Integer stockMinimo) {
        log.info("Obteniendo productos con stock menor a: {}", stockMinimo);
        return productoRepositorio.buscarProductosConStockBajo(stockMinimo);
    }
}
