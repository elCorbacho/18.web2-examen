package ipss.web2.examen.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "lamina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lamina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "imagen", nullable = false)
    private String imagen;

    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDateTime fechaLanzamiento;

    @Column(name = "tipo_lamina", nullable = false)
    private String tipoLamina;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

}
