package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.services.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    
    private final AlbumService albumService;
    
    /**
     * POST /api/albums - Crear un nuevo album
     */
    @PostMapping
    public ResponseEntity<AlbumResponseDTO> crearAlbum(@Valid @RequestBody AlbumRequestDTO requestDTO) {
        AlbumResponseDTO response = albumService.crearAlbum(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/albums/{id} - Obtener album por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> obtenerAlbumPorId(@PathVariable Long id) {
        AlbumResponseDTO response = albumService.obtenerAlbumPorId(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/albums - Obtener todos los albums
     */
    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> obtenerTodosLosAlbums() {
        List<AlbumResponseDTO> albums = albumService.obtenerTodosLosAlbums();
        return ResponseEntity.ok(albums);
    }
    
    /**
     * PUT /api/albums/{id} - Actualizar un album
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponseDTO> actualizarAlbum(
            @PathVariable Long id,
            @Valid @RequestBody AlbumRequestDTO requestDTO) {
        AlbumResponseDTO response = albumService.actualizarAlbum(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * DELETE /api/albums/{id} - Eliminar un album
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlbum(@PathVariable Long id) {
        albumService.eliminarAlbum(id);
        return ResponseEntity.noContent().build();
    }
}
