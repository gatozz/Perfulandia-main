package com.perfulandia.productos.configuracion;

import com.perfulandia.productos.entidad.Producto;
import com.perfulandia.productos.repositorio.ProductoRepositorio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CargadorDatos implements CommandLineRunner {
    
    private final ProductoRepositorio productoRepositorio;
    
    @Override
    public void run(String... args) throws Exception {
        if (productoRepositorio.count() == 0) {
            log.info("Cargando datos iniciales de productos chilenos...");
            cargarProductosChilenos();
        }
    }
    
    private void cargarProductosChilenos() {
        List<Producto> productos = Arrays.asList(
            new Producto(null, "Perfume Andino", "Fragancia inspirada en los Andes chilenos con notas de lavanda y eucalipto", 
                        "Perfulandia", new BigDecimal("45000"), 50, "Unisex", "100ml", null, null, true),
            
            new Producto(null, "Esencia Patagónica", "Perfume con aromas de la Patagonia chilena, notas de cedro y menta", 
                        "Perfulandia", new BigDecimal("52000"), 30, "Masculino", "75ml", null, null, true),
            
            new Producto(null, "Brisa Valparaíso", "Fragancia marina inspirada en el puerto de Valparaíso", 
                        "Perfulandia", new BigDecimal("38000"), 75, "Femenino", "50ml", null, null, true),
            
            new Producto(null, "Aroma Atacama", "Perfume con esencias del desierto de Atacama, notas secas y especiadas", 
                        "Perfulandia", new BigDecimal("48000"), 40, "Unisex", "100ml", null, null, true),
            
            new Producto(null, "Fragancia Chiloé", "Inspirado en la isla de Chiloé con notas de madera y mar", 
                        "Perfulandia", new BigDecimal("42000"), 60, "Masculino", "75ml", null, null, true),
            
            new Producto(null, "Perfume Santiago", "Elegante fragancia urbana inspirada en la capital chilena", 
                        "Perfulandia", new BigDecimal("55000"), 25, "Femenino", "100ml", null, null, true),
            
            new Producto(null, "Esencia Cordillera", "Perfume con aromas de la cordillera de los Andes", 
                        "Perfulandia", new BigDecimal("46000"), 35, "Unisex", "50ml", null, null, true),
            
            new Producto(null, "Aroma Viña del Mar", "Fragancia costera con notas florales y marinas", 
                        "Perfulandia", new BigDecimal("41000"), 55, "Femenino", "75ml", null, null, true),
            
            new Producto(null, "Perfume Temuco", "Inspirado en la región de La Araucanía con notas de pino y tierra", 
                        "Perfulandia", new BigDecimal("44000"), 45, "Masculino", "100ml", null, null, true),
            
            new Producto(null, "Brisa Antofagasta", "Fragancia del norte de Chile con notas minerales y salinas", 
                        "Perfulandia", new BigDecimal("39000"), 65, "Unisex", "50ml", null, null, true)
        );
        
        productoRepositorio.saveAll(productos);
        log.info("Se han cargado {} productos chilenos exitosamente", productos.size());
    }
}
