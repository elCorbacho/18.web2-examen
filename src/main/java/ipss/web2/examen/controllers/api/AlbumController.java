package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.ApiResponseDTO;
import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.services.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Controlador REST para gestión de Álbumes - /api/albums
@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {
    
    private final AlbumService albumService;
    
    // POST /api/albums - Crear nuevo álbum
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AlbumResponseDTO>> crearAlbum(
            @Valid @RequestBody AlbumRequestDTO requestDTO) {
        AlbumResponseDTO response = albumService.crearAlbum(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponseDTO.<AlbumResponseDTO>builder()
                .success(true)
                .message("Álbum creado exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // GET /api/albums/{id} - Obtener álbum por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AlbumResponseDTO>> obtenerAlbumPorId(
            @PathVariable Long id) {
        AlbumResponseDTO response = albumService.obtenerAlbumPorId(id);
        return ResponseEntity.ok(
            ApiResponseDTO.<AlbumResponseDTO>builder()
                .success(true)
                .message("Álbum recuperado exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // GET /api/albums - Listar todos los álbumes
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AlbumResponseDTO>>> obtenerTodosLosAlbumes() {
        List<AlbumResponseDTO> response = albumService.obtenerTodosLosAlbums();
        return ResponseEntity.ok(
            ApiResponseDTO.<List<AlbumResponseDTO>>builder()
                .success(true)
                .message("Álbumes recuperados exitosamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // PUT /api/albums/{id} - Actualizar álbum
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AlbumResponseDTO>> actualizarAlbum(
            @PathVariable Long id,
            @Valid @RequestBody AlbumRequestDTO requestDTO) {
        AlbumResponseDTO response = albumService.actualizarAlbum(id, requestDTO);
        return ResponseEntity.ok(
            ApiResponseDTO.<AlbumResponseDTO>builder()
                .success(true)
                .message("Álbum actualizado correctamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // DELETE /api/albums/{id} - Eliminar álbum (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> eliminarAlbum(@PathVariable Long id) {
        albumService.eliminarAlbum(id);
        return ResponseEntity.ok(
            ApiResponseDTO.<String>builder()
                .success(true)
                .message("Álbum eliminado correctamente")
                .data("Álbum con ID: " + id + " ha sido marcado como inactivo")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
