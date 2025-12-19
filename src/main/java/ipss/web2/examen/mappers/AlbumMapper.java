package ipss.web2.examen.mappers;

import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.models.Album;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {
    
    public AlbumResponseDTO toResponseDTO(Album album) {
        return AlbumResponseDTO.builder()
            .id(album.getId())
            .nombre(album.getNombre())
            .year(album.getYear())
            .descripcion(album.getDescripcion())
            .createdAt(album.getCreatedAt())
            .updatedAt(album.getUpdatedAt())
            .build();
    }
    
    public Album toEntity(AlbumRequestDTO requestDTO) {
        return Album.builder()
            .nombre(requestDTO.getNombre())
            .year(requestDTO.getYear())
            .descripcion(requestDTO.getDescripcion())
            .active(true)
            .build();
    }
    
    public void updateEntity(AlbumRequestDTO requestDTO, Album album) {
        album.setNombre(requestDTO.getNombre());
        album.setYear(requestDTO.getYear());
        album.setDescripcion(requestDTO.getDescripcion());
    }
}
