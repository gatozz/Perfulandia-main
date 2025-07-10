package com.perfulandia.productos.controlador;

import com.perfulandia.productos.entidad.Producto;
import com.perfulandia.productos.servicio.ProductoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "API para gestión de productos de Perfulandia")
public class ProductoControlador {
    
    private final ProductoServicio productoServicio;
    
    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos activos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> obtenerTodosLosProductos() {
        List<EntityModel<Producto>> productos = productoServicio.obtenerTodosLosProductos()
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productos);
        collectionModel.add(linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        return productoServicio.obtenerProductoPorId(id)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    public ResponseEntity<EntityModel<Producto>> crearProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoServicio.crearProducto(producto);
        EntityModel<Producto> productoModel = agregarEnlacesHateoas(nuevoProducto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoModel);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    })
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        return productoServicio.actualizarProducto(id, producto)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema (eliminación lógica)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        if (productoServicio.eliminarProducto(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/buscar/marca/{marca}")
    @Operation(summary = "Buscar productos por marca", description = "Retorna productos filtrados por marca")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarPorMarca(
            @Parameter(description = "Marca del producto") @PathVariable String marca) {
        List<EntityModel<Producto>> productos = productoServicio.buscarPorMarca(marca)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/buscar/categoria/{categoria}")
    @Operation(summary = "Buscar productos por categoría", description = "Retorna productos filtrados por categoría")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarPorCategoria(
            @Parameter(description = "Categoría del producto") @PathVariable String categoria) {
        List<EntityModel<Producto>> productos = productoServicio.buscarPorCategoria(categoria)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar productos por nombre", description = "Retorna productos que contengan el nombre especificado")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> buscarPorNombre(
            @Parameter(description = "Nombre del producto") @RequestParam String nombre) {
        List<EntityModel<Producto>> productos = productoServicio.buscarPorNombre(nombre)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/stock-bajo")
    @Operation(summary = "Obtener productos con stock bajo", description = "Retorna productos con stock menor al especificado")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> obtenerProductosConStockBajo(
            @Parameter(description = "Stock mínimo") @RequestParam(defaultValue = "10") Integer stockMinimo) {
        List<EntityModel<Producto>> productos = productoServicio.obtenerProductosConStockBajo(stockMinimo)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productos);
        return ResponseEntity.ok(collectionModel);
    }
    
    private EntityModel<Producto> agregarEnlacesHateoas(Producto producto) {
        EntityModel<Producto> productoModel = EntityModel.of(producto);
        productoModel.add(linkTo(methodOn(ProductoControlador.class).obtenerProductoPorId(producto.getId())).withSelfRel());
        productoModel.add(linkTo(methodOn(ProductoControlador.class).obtenerTodosLosProductos()).withRel("productos"));
        productoModel.add(linkTo(methodOn(ProductoControlador.class).actualizarProducto(producto.getId(), producto)).withRel("actualizar"));
        productoModel.add(linkTo(methodOn(ProductoControlador.class).eliminarProducto(producto.getId())).withRel("eliminar"));
        return productoModel;
    }
}
