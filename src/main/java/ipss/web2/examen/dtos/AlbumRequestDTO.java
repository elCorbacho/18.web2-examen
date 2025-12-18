package ipss.web2.examen.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequestDTO {
    
    @NotBlank(message = "El nombre del album es obligatorio")
    private String nombre;
    
    @NotNull(message = "El año del album es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    private Integer year;
    
    @NotBlank(message = "La descripción del album es obligatoria")
    private String descripcion;
}
