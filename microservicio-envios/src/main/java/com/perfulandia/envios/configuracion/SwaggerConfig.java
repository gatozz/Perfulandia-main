package com.perfulandia.envios.configuracion;

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
                new Server().url("http://localhost:8082").description("Servidor de Desarrollo"),
                new Server().url("/").description("Servidor Actual")
            ))
                .info(new Info()
                        .title("API Microservicio de Envíos - Perfulandia")
                        .description("API REST para la gestión de pedidos y envíos de Perfulandia. " +
                               "Permite crear pedidos, gestionar envíos y realizar seguimiento de entregas.")
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
