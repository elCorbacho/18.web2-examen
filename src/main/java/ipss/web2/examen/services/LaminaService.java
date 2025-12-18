package ipss.web2.examen.services;

import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.mappers.LaminaMapper;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.models.Lamina;
import ipss.web2.examen.repository.LaminaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LaminaService {
    
    private final LaminaRepository laminaRepository;
    private final LaminaMapper laminaMapper;
    
    /**
     * Crear una nueva lámina
     */
    public LaminaResponseDTO crearLamina(LaminaRequestDTO requestDTO, Album album) {
        Lamina lamina = laminaMapper.toEntity(requestDTO, album);
        Lamina laminaGuardada = laminaRepository.save(lamina);
        return laminaMapper.toResponseDTO(laminaGuardada);
    }
    
    /**
     * Obtener lámina por ID
     */
    @Transactional(readOnly = true)
    public LaminaResponseDTO obtenerLaminaPorId(Long id) {
        Lamina lamina = laminaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lámina no encontrada con ID: " + id));
        return laminaMapper.toResponseDTO(lamina);
    }
    
    /**
     * Obtener todas las láminas activas
     */
    @Transactional(readOnly = true)
    public List<LaminaResponseDTO> obtenerTodasLasLaminas() {
        return laminaRepository.findByActiveTrue()
                .stream()
                .map(laminaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtener láminas por ID de album
     */
    @Transactional(readOnly = true)
    public List<LaminaResponseDTO> obtenerLaminasPorAlbum(Long albumId) {
        return laminaRepository.findByAlbumId(albumId)
                .stream()
                .map(laminaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar una lámina
     */
    public LaminaResponseDTO actualizarLamina(Long id, LaminaRequestDTO requestDTO, Album album) {
        Lamina lamina = laminaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lámina no encontrada con ID: " + id));
        
        laminaMapper.updateEntity(requestDTO, lamina, album);
        Lamina laminaActualizada = laminaRepository.save(lamina);
        return laminaMapper.toResponseDTO(laminaActualizada);
    }
    
    /**
     * Eliminar una lámina (soft delete)
     */
    public void eliminarLamina(Long id) {
        Lamina lamina = laminaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lámina no encontrada con ID: " + id));
        
        lamina.setActive(false);
        laminaRepository.save(lamina);
    }
}
