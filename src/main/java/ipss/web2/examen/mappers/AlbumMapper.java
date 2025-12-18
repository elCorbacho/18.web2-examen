package ipss.web2.examen.mappers;

import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.models.Album;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {
    
    public AlbumResponseDTO toResponseDTO(Album album) {
        if (album == null) {
            return null;
        }
        
        AlbumResponseDTO dto = new AlbumResponseDTO();
        dto.setId(album.getId());
        dto.setNombre(album.getNombre());
        dto.setYear(album.getYear());
        dto.setDescripcion(album.getDescripcion());
        dto.setCreatedAt(album.getCreatedAt());
        dto.setUpdatedAt(album.getUpdatedAt());
        dto.setActive(album.getActive());
        
        return dto;
    }
    
    public Album toEntity(AlbumRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Album album = new Album();
        album.setNombre(dto.getNombre());
        album.setYear(dto.getYear());
        album.setDescripcion(dto.getDescripcion());
        album.setActive(true);
        
        return album;
    }
    
    public void updateEntity(AlbumRequestDTO dto, Album album) {
        if (dto == null) {
            return;
        }
        
        album.setNombre(dto.getNombre());
        album.setYear(dto.getYear());
        album.setDescripcion(dto.getDescripcion());
    }
}
