package com.perfulandia.productos.controlador;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/salud")
@Tag(name = "Salud", description = "Endpoints para verificar el estado del microservicio")
public class SaludControlador {

    @GetMapping
    @Operation(summary = "Verificar estado del servicio", description = "Retorna el estado actual del microservicio de productos")
    @ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente")
    public ResponseEntity<Map<String, Object>> verificarSalud() {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "ACTIVO");
        respuesta.put("servicio", "Microservicio de Productos Perfulandia");
        respuesta.put("version", "1.0.0");
        respuesta.put("timestamp", LocalDateTime.now());
        respuesta.put("puerto", 8081);
        
        return ResponseEntity.ok(respuesta);
    }
}
