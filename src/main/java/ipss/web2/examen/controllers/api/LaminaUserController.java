package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.ApiResponseDTO;
import ipss.web2.examen.dtos.LaminaCargaResponseDTO;
import ipss.web2.examen.dtos.LaminaCargueMasivoRequestDTO;
import ipss.web2.examen.dtos.LaminaCargueMasivoResponseDTO;
import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.services.LaminaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// Controlador REST para CRUD de Láminas - /api/laminas (valida catálogo, detecta repetidas)
@SuppressWarnings("null")
@RestController
@RequestMapping("/api/laminas")
@RequiredArgsConstructor
public class LaminaUserController {
    
    private final LaminaService laminaService;
    
    // POST /api/laminas - Agregar lámina validando catálogo y detectando repetidas
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LaminaCargaResponseDTO>> agregarLamina(
            @Valid @RequestBody LaminaRequestDTO laminaDTO) {
        
        if (laminaDTO.getAlbumId() == null) {
            throw new RuntimeException("El album_id es obligatorio en POST individual /api/laminas");
        }
        
        LaminaCargaResponseDTO response = laminaService.agregarLamina(laminaDTO.getAlbumId(), laminaDTO);
        
        String mensaje = response.esRepetida() 
            ? "Lámina agregada (repetida: " + response.cantidadRepetidas() + " copias totales)"
            : "Lámina agregada";
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.<LaminaCargaResponseDTO>builder()
                    .success(true)
                    .message(mensaje)
                    .data(response)
                    .timestamp(LocalDateTime.now())
                    .build());
    }
    
    // POST /api/laminas/masivo - Carga masiva de láminas con validación y reporte detallado
    @PostMapping("/masivo")
    public ResponseEntity<ApiResponseDTO<List<LaminaCargueMasivoResponseDTO>>> agregarLaminasMasivo(
            @Valid @RequestBody LaminaCargueMasivoRequestDTO cargueMasivo) {
        
        List<LaminaCargueMasivoResponseDTO> resultados = laminaService.agregarLaminasMasivo(cargueMasivo);
        
        // Contar éxitos y errores
        long exitosas = resultados.stream().filter(r -> r.laminaId() != null).count();
        long fallidas = resultados.size() - exitosas;
        
        String mensaje = String.format("Carga masiva completada: %d exitosas, %d fallidas", exitosas, fallidas);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.<List<LaminaCargueMasivoResponseDTO>>builder()
                    .success(fallidas == 0)
                    .message(mensaje)
                    .data(resultados)
                    .timestamp(LocalDateTime.now())
                    .build());
    }
    
    // GET /api/laminas/{id} - Obtener lámina por ID
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
    
    // GET /api/laminas - Obtener todas las láminas activas del sistema
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
    
    // GET /api/laminas/album/{albumId} - Obtener láminas poseídas de un álbum
    @GetMapping("/album/{albumId}")
    public ResponseEntity<ApiResponseDTO<List<LaminaResponseDTO>>> obtenerLaminasPorAlbum(@PathVariable Long albumId) {
        List<LaminaResponseDTO> laminas = laminaService.obtenerLaminasPorAlbum(albumId);
        
        return ResponseEntity.ok(ApiResponseDTO.<List<LaminaResponseDTO>>builder()
                .success(true)
                .message("Láminas del álbum obtenidas correctamente. Total: " + laminas.size())
                .data(laminas)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // PUT /api/laminas/{id} - Actualizar lámina
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LaminaResponseDTO>> actualizarLamina(
            @PathVariable Long id,
            @Valid @RequestBody LaminaRequestDTO requestDTO) {
        
        LaminaResponseDTO response = laminaService.actualizarLamina(id, requestDTO);
        
        return ResponseEntity.ok(ApiResponseDTO.<LaminaResponseDTO>builder()
                .success(true)
                .message("Lámina actualizada correctamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    // DELETE /api/laminas/{id} - Eliminar lámina (soft delete)
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
