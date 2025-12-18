package ipss.web2.examen.services;

import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.mappers.AlbumMapper;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlbumService {
    
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    
    /**
     * Crear un nuevo album
     */
    public AlbumResponseDTO crearAlbum(AlbumRequestDTO requestDTO) {
        Album album = albumMapper.toEntity(requestDTO);
        Album albumGuardado = albumRepository.save(album);
        return albumMapper.toResponseDTO(albumGuardado);
    }
    
    /**
     * Obtener album por ID
     */
    @Transactional(readOnly = true)
    public AlbumResponseDTO obtenerAlbumPorId(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album no encontrado con ID: " + id));
        return albumMapper.toResponseDTO(album);
    }
    
    /**
     * Obtener todos los albums activos
     */
    @Transactional(readOnly = true)
    public List<AlbumResponseDTO> obtenerTodosLosAlbums() {
        return albumRepository.findByActiveTrue()
                .stream()
                .map(albumMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar un album
     */
    public AlbumResponseDTO actualizarAlbum(Long id, AlbumRequestDTO requestDTO) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album no encontrado con ID: " + id));
        
        albumMapper.updateEntity(requestDTO, album);
        Album albumActualizado = albumRepository.save(album);
        return albumMapper.toResponseDTO(albumActualizado);
    }
    
    /**
     * Eliminar un album (soft delete)
     */
    public void eliminarAlbum(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album no encontrado con ID: " + id));
        
        album.setActive(false);
        albumRepository.save(album);
    }
}
