package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.ApiResponseDTO;
import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.repository.AlbumRepository;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.services.LaminaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestión de Láminas
 * Endpoints: /api/laminas
 */
@RestController
@RequestMapping("/api/laminas")
@RequiredArgsConstructor
public class LaminaController {
    
    private final LaminaService laminaService;
    private final AlbumRepository albumRepository;
    
    /**
     * POST /api/laminas - Crear una nueva lámina
     * 
     * @param requestDTO DTO con los datos de la lámina a crear
     * @return ResponseEntity con ApiResponseDTO envolviendo la lámina creada (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LaminaResponseDTO>> crearLamina(@Valid @RequestBody LaminaRequestDTO requestDTO) {
        Album album = albumRepository.findById(requestDTO.getAlbumId())
                .orElseThrow(() -> new RuntimeException("Album no encontrado con ID: " + requestDTO.getAlbumId()));
        
        LaminaResponseDTO response = laminaService.crearLamina(requestDTO, album);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.<LaminaResponseDTO>builder()
            .success(true)
            .message("Lámina creada exitosamente")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * GET /api/laminas/{id} - Obtener lámina por ID
     * 
     * @param id ID de la lámina a recuperar
     * @return ResponseEntity con ApiResponseDTO envolviendo la lámina encontrada (200 OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LaminaResponseDTO>> obtenerLaminaPorId(@PathVariable Long id) {
        LaminaResponseDTO response = laminaService.obtenerLaminaPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.<LaminaResponseDTO>builder()
            .success(true)
            .message("Lámina obtenida correctamente")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * GET /api/laminas - Obtener todas las láminas activas
     * 
     * @return ResponseEntity con ApiResponseDTO envolviendo lista de láminas (200 OK)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LaminaResponseDTO>>> obtenerTodasLasLaminas() {
        List<LaminaResponseDTO> laminas = laminaService.obtenerTodasLasLaminas();
        return ResponseEntity.ok(ApiResponseDTO.<List<LaminaResponseDTO>>builder()
            .success(true)
            .message("Láminas obtenidas correctamente. Total: " + laminas.size())
            .data(laminas)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * GET /api/laminas/album/{albumId} - Obtener láminas por album
     * 
     * @param albumId ID del album
     * @return ResponseEntity con ApiResponseDTO envolviendo lista de láminas del album (200 OK)
     */
    @GetMapping("/album/{albumId}")
    public ResponseEntity<ApiResponseDTO<List<LaminaResponseDTO>>> obtenerLaminasPorAlbum(@PathVariable Long albumId) {
        List<LaminaResponseDTO> laminas = laminaService.obtenerLaminasPorAlbum(albumId);
        return ResponseEntity.ok(ApiResponseDTO.<List<LaminaResponseDTO>>builder()
            .success(true)
            .message("Láminas del album obtenidas correctamente. Total: " + laminas.size())
            .data(laminas)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * PUT /api/laminas/{id} - Actualizar una lámina existente
     * 
     * @param id ID de la lámina a actualizar
     * @param requestDTO DTO con los nuevos datos de la lámina
     * @return ResponseEntity con ApiResponseDTO envolviendo la lámina actualizada (200 OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LaminaResponseDTO>> actualizarLamina(
            @PathVariable Long id,
            @Valid @RequestBody LaminaRequestDTO requestDTO) {
        Album album = albumRepository.findById(requestDTO.getAlbumId())
                .orElseThrow(() -> new RuntimeException("Album no encontrado con ID: " + requestDTO.getAlbumId()));
        
        LaminaResponseDTO response = laminaService.actualizarLamina(id, requestDTO, album);
        return ResponseEntity.ok(ApiResponseDTO.<LaminaResponseDTO>builder()
            .success(true)
            .message("Lámina actualizada correctamente")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * DELETE /api/laminas/{id} - Eliminar una lámina (soft delete)
     * Marca la lámina como inactiva sin borrar los datos de la BD
     * 
     * @param id ID de la lámina a eliminar
     * @return ResponseEntity con ApiResponseDTO con mensaje de confirmación (200 OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> eliminarLamina(@PathVariable Long id) {
        laminaService.eliminarLamina(id);
        return ResponseEntity.ok(ApiResponseDTO.<String>builder()
            .success(true)
            .message("Lámina eliminada correctamente")
            .data("Lámina con ID: " + id + " ha sido marcada como inactiva")
            .timestamp(LocalDateTime.now())
            .build());
    }
}
