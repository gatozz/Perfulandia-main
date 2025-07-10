package com.perfulandia.envios.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido extends RepresentationModel<Pedido> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;
    
    @NotBlank(message = "El email del cliente es obligatorio")
    @Column(name = "email_cliente", nullable = false, length = 100)
    private String emailCliente;
    
    @NotBlank(message = "El teléfono del cliente es obligatorio")
    @Column(name = "telefono_cliente", nullable = false, length = 20)
    private String telefonoCliente;
    
    @NotBlank(message = "La dirección de entrega es obligatoria")
    @Column(name = "direccion_entrega", nullable = false, length = 200)
    private String direccionEntrega;
    
    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false, length = 50)
    private String ciudad;
    
    @NotBlank(message = "La región es obligatoria")
    @Column(nullable = false, length = 50)
    private String region;
    
    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Envio envio;
    
    @PrePersist
    protected void onCreate() {
        fechaPedido = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    public enum EstadoPedido {
        PENDIENTE, CONFIRMADO, PREPARANDO, ENVIADO, ENTREGADO, CANCELADO
    }
}
