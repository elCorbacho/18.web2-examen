package ipss.web2.examen.controllers.api;

import ipss.web2.examen.dtos.ApiResponseDTO;
import ipss.web2.examen.dtos.LaminaCargaResponseDTO;
import ipss.web2.examen.dtos.LaminaCargueMasivoRequestDTO;
import ipss.web2.examen.dtos.LaminaCargueMasivoResponseDTO;
import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.repository.AlbumRepository;
import ipss.web2.examen.services.LaminaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para CRUD de Láminas como Usuario
 * Rutas: /api/laminas
 * 
 * CARACTERÍSTICAS:
 * - Al agregar una lámina, VALIDA contra el catálogo del álbum
 * - DETECTA si la lámina es REPETIDA (copias previas)
 * - INFORMA si está o NO en el catálogo
 * - Retorna todas las copias de la lámina (incluyendo la nueva)
 */
@RestController
@RequestMapping("/api/laminas")
@RequiredArgsConstructor
public class LaminaUserController {
    
    private final LaminaService laminaService;
    private final AlbumRepository albumRepository;
    
    /**
     * POST /api/laminas - Agregar nueva lámina a un álbum
     * 
     * VALIDACIONES:
     * ✓ Verifica que el álbum existe
     * ✓ Verifica que el álbum tiene catálogo definido
     * ✓ Valida si la lámina está en el catálogo
     * ✓ Detecta si ya existen copias (repetidas)
     * ✓ Retorna información completa de repeticiones
     * 
     * RESPUESTA:
     * - esRepetida: true si ya existen copias
     * - estaEnCatalogo: true si está definida en el catálogo
     * - cantidadRepetidas: Total de copias (incluyendo la nueva)
     * - lamina: Datos de la lámina agregada
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LaminaCargaResponseDTO>> agregarLamina(
            @Valid @RequestBody LaminaRequestDTO laminaDTO) {
        
        if (laminaDTO.getAlbumId() == null) {
            throw new RuntimeException("El album_id es obligatorio en POST individual /api/laminas");
        }
        
        Album album = albumRepository.findById(laminaDTO.getAlbumId())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado con ID: " + laminaDTO.getAlbumId()));
        
        LaminaCargaResponseDTO response = laminaService.agregarLamina(album.getId(), laminaDTO, album);
        
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
    
    /**
     * POST /api/laminas/masivo - Agregar múltiples láminas
     * Carga masiva de hasta 10+ láminas en una sola solicitud
     * 
     * Características:
     * ✓ Valida cada lámina contra el catálogo
     * ✓ Detecta repetidas automáticamente
     * ✓ Continúa procesando incluso si hay errores
     * ✓ Retorna reporte detallado de cada lámina
     */
    @PostMapping("/masivo")
    public ResponseEntity<ApiResponseDTO<List<LaminaCargueMasivoResponseDTO>>> agregarLaminasMasivo(
            @Valid @RequestBody LaminaCargueMasivoRequestDTO cargueMasivo) {
        
        Album album = albumRepository.findById(cargueMasivo.albumId())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado con ID: " + cargueMasivo.albumId()));
        
        List<LaminaCargueMasivoResponseDTO> resultados = laminaService.agregarLaminasMasivo(cargueMasivo, album);
        
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
    
    /**
     * GET /api/laminas/{id} - Obtener lámina por ID
     * 
     * Retorna los detalles de una lámina específica
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
     * GET /api/laminas - Obtener todas las láminas activas del sistema
     * 
     * Retorna un listado de todas las láminas en el sistema (de todos los álbumes)
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
     * GET /api/laminas/album/{albumId} - Obtener todas las láminas de un álbum
     * 
     * Retorna un listado de todas las láminas que el usuario posee en un álbum específico
     */
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
    
    /**
     * PUT /api/laminas/{id} - Actualizar lámina
     * 
     * Permite modificar los datos de una lámina existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LaminaResponseDTO>> actualizarLamina(
            @PathVariable Long id,
            @Valid @RequestBody LaminaRequestDTO requestDTO) {
        
        Album album = albumRepository.findById(requestDTO.getAlbumId())
                .orElseThrow(() -> new RuntimeException("Álbum no encontrado con ID: " + requestDTO.getAlbumId()));
        
        LaminaResponseDTO response = laminaService.actualizarLamina(id, requestDTO, album);
        
        return ResponseEntity.ok(ApiResponseDTO.<LaminaResponseDTO>builder()
                .success(true)
                .message("Lámina actualizada correctamente")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build());
    }
    
    /**
     * DELETE /api/laminas/{id} - Eliminar lámina (soft delete)
     * 
     * Marca la lámina como inactiva sin borrar los datos de la base de datos
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
