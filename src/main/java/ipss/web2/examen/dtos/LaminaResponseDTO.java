package ipss.web2.examen.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaminaResponseDTO {
    
    private Long id;
    
    private String nombre;
    
    private String imagen;
    
    private LocalDateTime fechaLanzamiento;
    
    private String tipoLamina;
    
    private Long albumId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Boolean active;
}
