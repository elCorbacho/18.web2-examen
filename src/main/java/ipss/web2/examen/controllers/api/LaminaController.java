package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.ApiResponseDTO;
import ipss.web2.examen.dtos.LaminaCatalogoRequestDTO;
import ipss.web2.examen.dtos.LaminaCatalogoResponseDTO;
import ipss.web2.examen.dtos.LaminasEstadoDTO;
import ipss.web2.examen.services.LaminaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Controlador REST para Catálogo de Láminas - /api/albums/{albumId}/catalogo
@RestController
@RequestMapping("/api/albums/{albumId}/catalogo")
@RequiredArgsConstructor
public class LaminaController {
    
    private final LaminaService laminaService;

    
    //POST /api/albums/{albumId}/catalogo - Crear catálogo de láminas
    @PostMapping
    public ResponseEntity<ApiResponseDTO<List<LaminaCatalogoResponseDTO>>> crearCatalogo(
            @PathVariable Long albumId,
            @Valid @RequestBody List<LaminaCatalogoRequestDTO> catalogo) {
        
        List<LaminaCatalogoResponseDTO> response = laminaService.crearCatalogo(albumId, catalogo);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.<List<LaminaCatalogoResponseDTO>>builder()
                    .success(true)
                    .message("Catálogo de láminas creado exitosamente con " + response.size() + " láminas")
                    .data(response)
                    .timestamp(LocalDateTime.now())
                    .build());
    }
    
    // GET /api/albums/{albumId}/catalogo - Obtener catálogo de láminas
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LaminaCatalogoResponseDTO>>> obtenerCatalogo(@PathVariable Long albumId) {
        List<LaminaCatalogoResponseDTO> catalogo = laminaService.obtenerCatalogo(albumId);
        
        return ResponseEntity.ok(ApiResponseDTO.<List<LaminaCatalogoResponseDTO>>builder()
                .success(true)
                .message("Catálogo de láminas obtenido: " + catalogo.size() + " láminas disponibles")
                .data(catalogo)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // GET /api/albums/{albumId}/catalogo/estado - Ver estado del álbum
    @GetMapping("/estado")
    public ResponseEntity<ApiResponseDTO<LaminasEstadoDTO>> obtenerEstado(@PathVariable Long albumId) {
        LaminasEstadoDTO estado = laminaService.obtenerEstado(albumId);
        
        return ResponseEntity.ok(ApiResponseDTO.<LaminasEstadoDTO>builder()
                .success(true)
                .message("Estado del álbum obtenido")
                .data(estado)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
