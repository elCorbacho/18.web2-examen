package ipss.web2.examen.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequestDTO {
    
    @NotBlank(message = "El nombre del álbum es obligatorio")
    private String nombre;
    
    @NotNull(message = "El año de lanzamiento es obligatorio")
    private Integer year;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}
