package ipss.web2.examen.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponseDTO {
    
    private Long id;
    
    private String nombre;
    
    private Integer year;
    
    private String descripcion;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Boolean active;
}
