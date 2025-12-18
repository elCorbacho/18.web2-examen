package ipss.web2.examen.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "album")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean active = true;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lamina> laminas;
}
