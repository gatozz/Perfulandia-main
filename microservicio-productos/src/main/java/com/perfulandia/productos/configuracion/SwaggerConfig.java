package com.perfulandia.productos.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                new Server().url("http://localhost:8081").description("Servidor de Desarrollo"),
                new Server().url("/").description("Servidor Actual")
            ))
                .info(new Info()
                        .title("API Microservicio de Productos - Perfulandia")
                        .description("API REST para la gestión de productos de perfumes de Perfulandia. " +
                               "Permite crear, consultar, actualizar y eliminar productos, así como realizar búsquedas avanzadas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Perfulandia")
                                .email("desarrollo@perfulandia.cl")
                                .url("https://perfulandia.cl"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
