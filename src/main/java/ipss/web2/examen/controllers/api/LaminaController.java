package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.repository.LaminaRepository;
import ipss.web2.examen.services.LaminaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/laminas")
@RequiredArgsConstructor
public class LaminaController {
    
    private final LaminaService laminaService;
    
    /**
     * POST /api/laminas - Crear una nueva lámina
     */
    @PostMapping
    public ResponseEntity<LaminaResponseDTO> crearLamina(@Valid @RequestBody LaminaRequestDTO requestDTO) {
        // Simular búsqueda de album (en producción, usar AlbumRepository)
        Album album = new Album();
        album.setId(requestDTO.getAlbumId());
        
        LaminaResponseDTO response = laminaService.crearLamina(requestDTO, album);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/laminas/{id} - Obtener lámina por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LaminaResponseDTO> obtenerLaminaPorId(@PathVariable Long id) {
        LaminaResponseDTO response = laminaService.obtenerLaminaPorId(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/laminas - Obtener todas las láminas
     */
    @GetMapping
    public ResponseEntity<List<LaminaResponseDTO>> obtenerTodasLasLaminas() {
        List<LaminaResponseDTO> laminas = laminaService.obtenerTodasLasLaminas();
        return ResponseEntity.ok(laminas);
    }
    
    /**
     * GET /api/laminas/album/{albumId} - Obtener láminas por album
     */
    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<LaminaResponseDTO>> obtenerLaminasPorAlbum(@PathVariable Long albumId) {
        List<LaminaResponseDTO> laminas = laminaService.obtenerLaminasPorAlbum(albumId);
        return ResponseEntity.ok(laminas);
    }
    
    /**
     * PUT /api/laminas/{id} - Actualizar una lámina
     */
    @PutMapping("/{id}")
    public ResponseEntity<LaminaResponseDTO> actualizarLamina(
            @PathVariable Long id,
            @Valid @RequestBody LaminaRequestDTO requestDTO) {
        // Simular búsqueda de album (en producción, usar AlbumRepository)
        Album album = new Album();
        album.setId(requestDTO.getAlbumId());
        
        LaminaResponseDTO response = laminaService.actualizarLamina(id, requestDTO, album);
        return ResponseEntity.ok(response);
    }
    
    /**
     * DELETE /api/laminas/{id} - Eliminar una lámina
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLamina(@PathVariable Long id) {
        laminaService.eliminarLamina(id);
        return ResponseEntity.noContent().build();
    }
}
