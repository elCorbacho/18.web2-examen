package ipss.web2.examen.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaminaRequestDTO {
    
    @NotBlank(message = "El nombre de la lámina es obligatorio")
    private String nombre;
    
    private String imagen;
    
    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    private LocalDate fechaLanzamiento;
    
    @NotBlank(message = "El tipo de lámina es obligatorio")
    private String tipoLamina;
    
    @NotNull(message = "El album_id es obligatorio")
    private Long albumId;
}
