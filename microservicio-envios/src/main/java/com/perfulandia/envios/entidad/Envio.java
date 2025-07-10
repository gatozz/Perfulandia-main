package com.perfulandia.envios.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio extends RepresentationModel<Envio> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo_seguimiento", unique = true, length = 50)
    private String codigoSeguimiento;
    
    @NotBlank(message = "La empresa de transporte es obligatoria")
    @Column(name = "empresa_transporte", nullable = false, length = 50)
    private String empresaTransporte;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEnvio estado = EstadoEnvio.PREPARANDO;
    
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    
    @Column(name = "fecha_entrega_estimada")
    private LocalDateTime fechaEntregaEstimada;
    
    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;
    
    @Column(length = 500)
    private String observaciones;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @PrePersist
    protected void onCreate() {
        if (codigoSeguimiento == null) {
            codigoSeguimiento = generarCodigoSeguimiento();
        }
    }
    
    private String generarCodigoSeguimiento() {
        return "PFL" + System.currentTimeMillis();
    }
    
    public enum EstadoEnvio {
        PREPARANDO, EN_TRANSITO, EN_REPARTO, ENTREGADO, DEVUELTO, EXTRAVIADO
    }
}
