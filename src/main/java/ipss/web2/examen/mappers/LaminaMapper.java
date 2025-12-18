package ipss.web2.examen.mappers;

import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.models.Lamina;
import org.springframework.stereotype.Component;

@Component
public class LaminaMapper {
    
    public LaminaResponseDTO toResponseDTO(Lamina lamina) {
        if (lamina == null) {
            return null;
        }
        
        LaminaResponseDTO dto = new LaminaResponseDTO();
        dto.setId(lamina.getId());
        dto.setNombre(lamina.getNombre());
        dto.setImagen(lamina.getImagen());
        dto.setFechaLanzamiento(lamina.getFechaLanzamiento());
        dto.setTipoLamina(lamina.getTipoLamina());
        dto.setAlbumId(lamina.getAlbum().getId());
        dto.setCreatedAt(lamina.getCreatedAt());
        dto.setUpdatedAt(lamina.getUpdatedAt());
        dto.setActive(lamina.getActive());
        
        return dto;
    }
    
    public Lamina toEntity(LaminaRequestDTO dto, Album album) {
        if (dto == null) {
            return null;
        }
        
        Lamina lamina = new Lamina();
        lamina.setNombre(dto.getNombre());
        lamina.setImagen(dto.getImagen());
        lamina.setFechaLanzamiento(dto.getFechaLanzamiento());
        lamina.setTipoLamina(dto.getTipoLamina());
        lamina.setAlbum(album);
        lamina.setActive(true);
        
        return lamina;
    }
    
    public void updateEntity(LaminaRequestDTO dto, Lamina lamina, Album album) {
        if (dto == null) {
            return;
        }
        
        lamina.setNombre(dto.getNombre());
        lamina.setImagen(dto.getImagen());
        lamina.setFechaLanzamiento(dto.getFechaLanzamiento());
        lamina.setTipoLamina(dto.getTipoLamina());
        lamina.setAlbum(album);
    }
}
