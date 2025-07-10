package com.perfulandia.envios.controlador;

import com.perfulandia.envios.entidad.Envio;
import com.perfulandia.envios.servicio.EnvioServicio;
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
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "API para gestión de envíos de Perfulandia")
public class EnvioControlador {
    
    private final EnvioServicio envioServicio;
    
    @GetMapping
    @Operation(summary = "Obtener todos los envíos", description = "Retorna una lista de todos los envíos")
    @ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> obtenerTodosLosEnvios() {
        List<EntityModel<Envio>> envios = envioServicio.obtenerTodosLosEnvios()
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(envios);
        collectionModel.add(linkTo(methodOn(EnvioControlador.class).obtenerTodosLosEnvios()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener envío por ID", description = "Retorna un envío específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<EntityModel<Envio>> obtenerEnvioPorId(
            @Parameter(description = "ID del envío") @PathVariable Long id) {
        return envioServicio.obtenerEnvioPorId(id)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/seguimiento/{codigoSeguimiento}")
    @Operation(summary = "Rastrear envío por código", description = "Retorna información del envío usando el código de seguimiento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Código de seguimiento no encontrado")
    })
    public ResponseEntity<EntityModel<Envio>> rastrearEnvio(
            @Parameter(description = "Código de seguimiento") @PathVariable String codigoSeguimiento) {
        return envioServicio.obtenerEnvioPorCodigoSeguimiento(codigoSeguimiento)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Obtener envío por pedido", description = "Retorna el envío asociado a un pedido específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado para el pedido")
    })
    public ResponseEntity<EntityModel<Envio>> obtenerEnvioPorPedido(
            @Parameter(description = "ID del pedido") @PathVariable Long pedidoId) {
        return envioServicio.obtenerEnvioPorPedido(pedidoId)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo envío", description = "Crea un nuevo envío en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del envío inválidos")
    })
    public ResponseEntity<EntityModel<Envio>> crearEnvio(@Valid @RequestBody Envio envio) {
        Envio nuevoEnvio = envioServicio.crearEnvio(envio);
        EntityModel<Envio> envioModel = agregarEnlacesHateoas(nuevoEnvio);
        return ResponseEntity.status(HttpStatus.CREATED).body(envioModel);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar envío", description = "Actualiza un envío existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos del envío inválidos")
    })
    public ResponseEntity<EntityModel<Envio>> actualizarEnvio(
            @Parameter(description = "ID del envío") @PathVariable Long id,
            @Valid @RequestBody Envio envio) {
        return envioServicio.actualizarEnvio(id, envio)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado del envío", description = "Actualiza únicamente el estado de un envío")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<EntityModel<Envio>> actualizarEstadoEnvio(
            @Parameter(description = "ID del envío") @PathVariable Long id,
            @Parameter(description = "Nuevo estado del envío") @RequestParam Envio.EstadoEnvio estado) {
        return envioServicio.actualizarEstadoEnvio(id, estado)
                .map(this::agregarEnlacesHateoas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar envíos por estado", description = "Retorna envíos filtrados por estado")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> buscarPorEstado(
            @Parameter(description = "Estado del envío") @PathVariable Envio.EstadoEnvio estado) {
        List<EntityModel<Envio>> envios = envioServicio.buscarPorEstado(estado)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(envios);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/empresa/{empresaTransporte}")
    @Operation(summary = "Buscar envíos por empresa de transporte", description = "Retorna envíos filtrados por empresa de transporte")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> buscarPorEmpresaTransporte(
            @Parameter(description = "Empresa de transporte") @PathVariable String empresaTransporte) {
        List<EntityModel<Envio>> envios = envioServicio.buscarPorEmpresaTransporte(empresaTransporte)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(envios);
        return ResponseEntity.ok(collectionModel);
    }
    
    @GetMapping("/cliente/{email}")
    @Operation(summary = "Buscar envíos por email del cliente", description = "Retorna envíos de un cliente específico")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> buscarPorEmailCliente(
            @Parameter(description = "Email del cliente") @PathVariable String email) {
        List<EntityModel<Envio>> envios = envioServicio.buscarPorEmailCliente(email)
                .stream()
                .map(this::agregarEnlacesHateoas)
                .collect(Collectors.toList());
        
        CollectionModel<EntityModel<Envio>> collectionModel = CollectionModel.of(envios);
        return ResponseEntity.ok(collectionModel);
    }
    
    private EntityModel<Envio> agregarEnlacesHateoas(Envio envio) {
        EntityModel<Envio> envioModel = EntityModel.of(envio);
        envioModel.add(linkTo(methodOn(EnvioControlador.class).obtenerEnvioPorId(envio.getId())).withSelfRel());
        envioModel.add(linkTo(methodOn(EnvioControlador.class).obtenerTodosLosEnvios()).withRel("envios"));
        envioModel.add(linkTo(methodOn(EnvioControlador.class).actualizarEnvio(envio.getId(), envio)).withRel("actualizar"));
        envioModel.add(linkTo(methodOn(EnvioControlador.class).actualizarEstadoEnvio(envio.getId(), envio.getEstado())).withRel("cambiar-estado"));
        envioModel.add(linkTo(methodOn(EnvioControlador.class).rastrearEnvio(envio.getCodigoSeguimiento())).withRel("rastrear"));
        return envioModel;
    }
}
