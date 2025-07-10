package com.perfulandia.envios.controlador;

import com.perfulandia.envios.entidad.Pedido;
import com.perfulandia.envios.servicio.PedidoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gestión de pedidos de Perfulandia")
public class PedidoControlador {
    
    private final PedidoServicio pedidoServicio;
    
    @GetMapping
    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> obtenerTodosLosPedidos() {
        List<EntityModel<Pedido>> pedidos = pedidoServicio.obtenerTodosLosPedidos()
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        collectionModel.add(linkTo(methodOn(PedidoControlador.class).obtenerTodosLosPedidos()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID", description = "Retorna un pedido específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<EntityModel<Pedido>> obtenerPedidoPorId(
            @Parameter(description = "ID del pedido") @PathVariable Long id) {
        return pedidoServicio.obtenerPedidoPorId(id)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo pedido", description = "Crea un nuevo pedido en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del pedido inválidos")
    })
    public ResponseEntity<EntityModel<Pedido>> crearPedido(@Valid @RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoServicio.crearPedido(pedido);
        EntityModel<Pedido> pedidoModel = agregarEnlacesHateoas(nuevoPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoModel);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido", description = "Actualiza un pedido existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos del pedido inválidos")
    })
    public ResponseEntity<EntityModel<Pedido>> actualizarPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long id,
            @Valid @RequestBody Pedido pedido) {
        return pedidoServicio.actualizarPedido(id, pedido)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del pedido", description = "Actualiza únicamente el estado de un pedido")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<EntityModel<Pedido>> actualizarEstadoPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del pedido") @RequestParam Pedido.EstadoPedido estado) {
        return pedidoServicio.actualizarEstadoPedido(id, estado)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar pedidos por estado", description = "Retorna pedidos filtrados por estado")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPorEstado(
            @Parameter(description = "Estado del pedido") @PathVariable Pedido.EstadoPedido estado) {
        List<EntityModel<Pedido>> pedidos = pedidoServicio.buscarPorEstado(estado)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/cliente/{email}")
    @Operation(summary = "Buscar pedidos por email del cliente", description = "Retorna pedidos de un cliente específico")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPorEmailCliente(
            @Parameter(description = "Email del cliente") @PathVariable String email) {
        List<EntityModel<Pedido>> pedidos = pedidoServicio.buscarPorEmailCliente(email)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/ciudad/{ciudad}")
    @Operation(summary = "Buscar pedidos por ciudad", description = "Retorna pedidos filtrados por ciudad")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPorCiudad(
            @Parameter(description = "Ciudad") @PathVariable String ciudad) {
        List<EntityModel<Pedido>> pedidos = pedidoServicio.buscarPorCiudad(ciudad)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/region/{region}")
    @Operation(summary = "Buscar pedidos por región", description = "Retorna pedidos filtrados por región")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPorRegion(
            @Parameter(description = "Región") @PathVariable String region) {
        List<EntityModel<Pedido>> pedidos = pedidoServicio.buscarPorRegion(region)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/fechas")
    @Operation(summary = "Buscar pedidos por rango de fechas", description = "Retorna pedidos en un rango de fechas específico")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> buscarPorRangoFechas(
            @Parameter(description = "Fecha de inicio") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<EntityModel<Pedido>> pedidos = pedidoServicio.buscarPorRangoFechas(fechaInicio, fechaFin)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos);
        return ResponseEntity.ok(collectionModel);
    }
    
    private EntityModel<Pedido> agregarEnlacesHateoas(Pedido pedido) {
        EntityModel<Pedido> pedidoModel = EntityModel.of(pedido);
        pedidoModel.add(linkTo(methodOn(PedidoControlador.class).obtenerPedidoPorId(pedido.getId())).withSelfRel());
        pedidoModel.add(linkTo(methodOn(PedidoControlador.class).obtenerTodosLosPedidos()).withRel("pedidos"));
        pedidoModel.add(linkTo(methodOn(PedidoControlador.class).actualizarPedido(pedido.getId(), pedido)).withRel("actualizar"));
        pedidoModel.add(linkTo(methodOn(PedidoControlador.class).actualizarEstadoPedido(pedido.getId(), pedido.getEstado())).withRel("cambiar-estado"));
        return pedidoModel;
    }
}
