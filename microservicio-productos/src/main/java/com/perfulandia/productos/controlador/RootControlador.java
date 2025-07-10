package com.perfulandia.productos.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootControlador {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("servicio", "Microservicio de Productos Perfulandia");
        respuesta.put("version", "1.0.0");
        respuesta.put("estado", "ACTIVO");
        respuesta.put("swagger", "http://localhost:8081/swagger-ui.html");
        respuesta.put("api-docs", "http://localhost:8081/v3/api-docs");
        respuesta.put("salud", "http://localhost:8081/api/salud");
        respuesta.put("productos", "http://localhost:8081/api/productos");
        
        return ResponseEntity.ok(respuesta);
    }
}
