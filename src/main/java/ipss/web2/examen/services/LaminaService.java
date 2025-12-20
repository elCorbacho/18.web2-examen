package ipss.web2.examen.services;

import ipss.web2.examen.dtos.LaminaCargaResponseDTO;
import ipss.web2.examen.dtos.LaminaCargueMasivoRequestDTO;
import ipss.web2.examen.dtos.LaminaCargueMasivoResponseDTO;
import ipss.web2.examen.dtos.LaminaCatalogoRequestDTO;
import ipss.web2.examen.dtos.LaminaCatalogoResponseDTO;
import ipss.web2.examen.dtos.LaminaRequestDTO;
import ipss.web2.examen.dtos.LaminaResponseDTO;
import ipss.web2.examen.dtos.LaminasEstadoDTO;
import ipss.web2.examen.exceptions.ResourceNotFoundException;
import ipss.web2.examen.mappers.LaminaMapper;
import ipss.web2.examen.models.Album;
import ipss.web2.examen.models.Lamina;
import ipss.web2.examen.models.LaminaCatalogo;
import ipss.web2.examen.repositories.LaminaCatalogoRepository;
import ipss.web2.examen.repository.LaminaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("null")
@Service
@RequiredArgsConstructor
@Transactional
public class LaminaService {
    
    private final LaminaRepository laminaRepository;
    private final LaminaCatalogoRepository laminaCatalogoRepository;
    private final LaminaMapper laminaMapper;
    
    /**
     * Crear catálogo de láminas para un álbum
     */
    public List<LaminaCatalogoResponseDTO> crearCatalogo(Long albumId, List<LaminaCatalogoRequestDTO> laminasCatalogo, Album album) {
        // Validar que no exista catálogo previo
        long existentes = laminaCatalogoRepository.countByAlbumAndActiveTrue(album);
        if (existentes > 0) {
            throw new RuntimeException("Este álbum ya tiene un catálogo de láminas definido");
        }
        
        return laminasCatalogo.stream()
            .map(dto -> {
                LaminaCatalogo catalogo = laminaMapper.toCatalogoEntity(dto, album);
                return laminaCatalogoRepository.save(catalogo);
            })
            .map(laminaMapper::toCatalogoResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Agregar una lámina al álbum con validación contra catálogo
     * OBLIGATORIO: La lámina DEBE estar en el catálogo
     */
    public LaminaCargaResponseDTO agregarLamina(Long albumId, LaminaRequestDTO laminaDTO, Album album) {
        // Validar que el catálogo exista
        List<LaminaCatalogo> catalogo = laminaCatalogoRepository.findByAlbumAndActiveTrue(album);
        if (catalogo.isEmpty()) {
            throw new RuntimeException("Debe crear un catálogo de láminas primero");
        }
        
        // Buscar si la lámina existe en el catálogo (OBLIGATORIO)
        Optional<LaminaCatalogo> enCatalogo = laminaCatalogoRepository
            .findByAlbumAndNombreAndActiveTrue(album, laminaDTO.getNombre());
        
        // VALIDACIÓN: Lámina DEBE estar en catálogo
        if (enCatalogo.isEmpty()) {
            throw new RuntimeException(
                "❌ ERROR: La lámina '" + laminaDTO.getNombre() + "' NO existe en el catálogo del álbum. " +
                "Solo puedes agregar láminas que están definidas en el catálogo."
            );
        }
        
        // Buscar si ya existen copias de esta lámina
        List<Lamina> laminasExistentes = laminaRepository.findByAlbumAndNombreAndActiveTrue(album, laminaDTO.getNombre());
        boolean esRepetida = !laminasExistentes.isEmpty();
        int cantidadTotal = laminasExistentes.size() + 1;
        
        // Crear y guardar la lámina
        Lamina lamina = laminaMapper.toEntity(laminaDTO, album);
        Lamina laminaGuardada = laminaRepository.save(lamina);
        
        return new LaminaCargaResponseDTO(
            esRepetida,
            true,  // Ahora siempre es true porque validamos que esté en catálogo
            cantidadTotal,
            laminaMapper.toResponseDTO(laminaGuardada)
        );
    }
    
    /**
     * Obtener estado: láminas poseídas, faltantes y repetidas
     */
    @Transactional(readOnly = true)
    public LaminasEstadoDTO obtenerEstado(Long albumId, Album album) {
        // Obtener catálogo
        List<LaminaCatalogo> catalogoCompleto = laminaCatalogoRepository.findByAlbumAndActiveTrue(album);
        
        // Obtener láminas poseídas
        List<Lamina> laminasPoseidas = laminaRepository.findByAlbumAndActiveTrue(album);
        
        // Crear set de nombres poseídos
        Set<String> nombresPoseidos = laminasPoseidas.stream()
            .map(Lamina::getNombre)
            .collect(Collectors.toSet());
        
        // LÁMINAS FALTANTES: en catálogo pero no poseídas
        List<LaminaCatalogoResponseDTO> laminasFaltantes = catalogoCompleto.stream()
            .filter(catalogo -> !nombresPoseidos.contains(catalogo.getNombre()))
            .map(laminaMapper::toCatalogoResponseDTO)
            .collect(Collectors.toList());
        
        // LÁMINAS REPETIDAS: contar cuántas copias de cada una
        Map<String, Integer> laminasRepetidas = laminasPoseidas.stream()
            .collect(Collectors.groupingBy(
                Lamina::getNombre,
                Collectors.summingInt(l -> 1)
            ))
            .entrySet().stream()
            .filter(e -> e.getValue() > 1)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        
        // Calcular totales
        int totalLaminas = laminasPoseidas.size();
        int laminasFaltantesTotal = laminasFaltantes.size();
        int laminasRepetidastotal = laminasRepetidas.values().stream()
            .mapToInt(Integer::intValue)
            .sum();
        
        List<LaminaResponseDTO> laminasDTO = laminasPoseidas.stream()
            .map(laminaMapper::toResponseDTO)
            .collect(Collectors.toList());
        
        return new LaminasEstadoDTO(laminasDTO, laminasFaltantes, laminasRepetidas, totalLaminas, laminasFaltantesTotal, laminasRepetidastotal);
    }
    
    /**
     * Obtener catálogo de láminas disponibles en un álbum
     */
    public List<LaminaCatalogoResponseDTO> obtenerCatalogo(Album album) {
        List<LaminaCatalogo> catalogo = laminaCatalogoRepository.findByAlbumAndActiveTrue(album);
        return catalogo.stream()
            .map(laminaMapper::toCatalogoResponseDTO)
            .collect(Collectors.toList());
    }
    
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
                .orElseThrow(() -> new ResourceNotFoundException("Lámina", "ID", id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Lámina", "ID", id));
        
        laminaMapper.updateEntity(requestDTO, lamina, album);
        Lamina laminaActualizada = laminaRepository.save(lamina);
        return laminaMapper.toResponseDTO(laminaActualizada);
    }
    
    /**
     * Eliminar una lámina (soft delete)
     */
    public void eliminarLamina(Long id) {
        Lamina lamina = laminaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lámina", "ID", id));
        
        lamina.setActive(false);
        laminaRepository.save(lamina);
    }
    
    /**
     * Agregar múltiples láminas en una sola solicitud (carga masiva)
     */
    @Transactional
    public List<LaminaCargueMasivoResponseDTO> agregarLaminasMasivo(LaminaCargueMasivoRequestDTO cargueMasivo, Album album) {
        // Validar que existe catálogo
        List<LaminaCatalogo> catalogo = laminaCatalogoRepository.findByAlbumAndActiveTrue(album);
        if (catalogo.isEmpty()) {
            throw new RuntimeException("Debe crear un catálogo de láminas primero");
        }
        
        List<LaminaCargueMasivoResponseDTO> resultados = new ArrayList<>();
        
        for (LaminaRequestDTO laminaDTO : cargueMasivo.laminas()) {
            try {
                // Buscar láminas existentes con el mismo nombre
                List<Lamina> laminasExistentes = laminaRepository
                    .findByAlbumAndNombreAndActiveTrue(album, laminaDTO.getNombre());
                
                // Verificar si está en catálogo
                boolean estaEnCatalogo = catalogo.stream()
                    .anyMatch(cat -> cat.getNombre().equalsIgnoreCase(laminaDTO.getNombre()));
                
                if (!estaEnCatalogo) {
                    resultados.add(new LaminaCargueMasivoResponseDTO(
                        null,
                        laminaDTO.getNombre(),
                        false,
                        0,
                        false,
                        "❌ NO AGREGADA: No está en el catálogo"
                    ));
                    continue;
                }
                
                boolean esRepetida = !laminasExistentes.isEmpty();
                
                // Crear y guardar la lámina
                Lamina lamina = laminaMapper.toEntity(laminaDTO, album);
                Lamina laminaGuardada = laminaRepository.save(lamina);
                
                int cantidadTotal = laminasExistentes.size() + 1;
                
                String estado = esRepetida 
                    ? "✓ AGREGADA (Repetida: " + cantidadTotal + " copias)"
                    : "✓ AGREGADA (Nueva)";
                
                resultados.add(new LaminaCargueMasivoResponseDTO(
                    laminaGuardada.getId(),
                    laminaGuardada.getNombre(),
                    esRepetida,
                    cantidadTotal,
                    true,
                    estado
                ));
                
            } catch (Exception e) {
                resultados.add(new LaminaCargueMasivoResponseDTO(
                    null,
                    laminaDTO.getNombre(),
                    false,
                    0,
                    false,
                    "❌ ERROR: " + e.getMessage()
                ));
            }
        }
        
        return resultados;
    }
}
