package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.dtos.ApiResponseDTO;
import ipss.web2.examen.services.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestión de Albums
 * Endpoints: /api/albums
 */
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    
    private final AlbumService albumService;
    
    /**
     * POST /api/albums - Crear un nuevo album
     * 
     * @param requestDTO DTO con los datos del album a crear
     * @return ResponseEntity con ApiResponseDTO envolviendo el album creado (201 CREATED)
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AlbumResponseDTO>> crearAlbum(@Valid @RequestBody AlbumRequestDTO requestDTO) {
        AlbumResponseDTO response = albumService.crearAlbum(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.<AlbumResponseDTO>builder()
            .success(true)
            .message("Album creado exitosamente")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * GET /api/albums/{id} - Obtener album por ID
     * 
     * @param id ID del album a recuperar
     * @return ResponseEntity con ApiResponseDTO envolviendo el album encontrado (200 OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AlbumResponseDTO>> obtenerAlbumPorId(@PathVariable Long id) {
        AlbumResponseDTO response = albumService.obtenerAlbumPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.<AlbumResponseDTO>builder()
            .success(true)
            .message("Album obtenido correctamente")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * GET /api/albums - Obtener todos los albums activos
     * 
     * @return ResponseEntity con ApiResponseDTO envolviendo lista de albums (200 OK)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AlbumResponseDTO>>> obtenerTodosLosAlbums() {
        List<AlbumResponseDTO> albums = albumService.obtenerTodosLosAlbums();
        return ResponseEntity.ok(ApiResponseDTO.<List<AlbumResponseDTO>>builder()
            .success(true)
            .message("Albums obtenidos correctamente. Total: " + albums.size())
            .data(albums)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * PUT /api/albums/{id} - Actualizar un album existente
     * 
     * @param id ID del album a actualizar
     * @param requestDTO DTO con los nuevos datos del album
     * @return ResponseEntity con ApiResponseDTO envolviendo el album actualizado (200 OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AlbumResponseDTO>> actualizarAlbum(
            @PathVariable Long id,
            @Valid @RequestBody AlbumRequestDTO requestDTO) {
        AlbumResponseDTO response = albumService.actualizarAlbum(id, requestDTO);
        return ResponseEntity.ok(ApiResponseDTO.<AlbumResponseDTO>builder()
            .success(true)
            .message("Album actualizado correctamente")
            .data(response)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    /**
     * DELETE /api/albums/{id} - Eliminar un album (soft delete)
     * Marca el album como inactivo sin borrar los datos de la BD
     * 
     * @param id ID del album a eliminar
     * @return ResponseEntity con ApiResponseDTO con mensaje de confirmación (200 OK)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> eliminarAlbum(@PathVariable Long id) {
        albumService.eliminarAlbum(id);
        return ResponseEntity.ok(ApiResponseDTO.<String>builder()
            .success(true)
            .message("Album eliminado correctamente")
            .data("Album con ID: " + id + " ha sido marcado como inactivo")
            .timestamp(LocalDateTime.now())
            .build());
    }
}
