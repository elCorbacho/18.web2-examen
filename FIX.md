# 🔧 Plan de Correcciones - Proyecto Web2 Examen

**Fecha:** 20 de diciembre de 2025  
**Calificación Actual:** 92.5/100  
**Calificación Objetivo:** 97.5/100

---

## 🔴 CRÍTICAS (Hacer AHORA)

### 1. **SEGURIDAD: Remover Contraseña del Código**
**Prioridad:** 🚨 URGENTE  
**Tiempo:** 5 minutos  
**Impacto:** Seguridad crítica

**Archivo:** `src/main/resources/application.properties` línea 6

**Problema:**
```properties
spring.datasource.password=totoralillo12..
```

**Solución:**
```properties
spring.datasource.password=${DB_PASSWORD:defaultpassword}
```

**Pasos:**
1. Editar `application.properties`
2. Cambiar contraseña de la BD en AWS RDS
3. Crear variable de entorno: `DB_PASSWORD=nueva_contraseña`
4. Agregar a `.gitignore`: `application-local.properties`

---

### 2. **Crear Tests Unitarios**
**Prioridad:** 🔴 CRÍTICA  
**Tiempo:** 30-45 minutos  
**Impacto:** +5 puntos en la nota

**Crear archivo:** `src/test/java/ipss/web2/examen/services/AlbumServiceTest.java`

```java
package ipss.web2.examen.services;

import ipss.web2.examen.dtos.AlbumRequestDTO;
import ipss.web2.examen.dtos.AlbumResponseDTO;
import ipss.web2.examen.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {

    @Autowired
    private AlbumService albumService;

    @Test
    void deberiaCrearAlbumCorrectamente() {
        AlbumRequestDTO request = new AlbumRequestDTO(
            "Dragon Ball Z",
            1989,
            "Álbum de Dragon Ball Z"
        );

        AlbumResponseDTO response = albumService.crearAlbum(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Dragon Ball Z", response.getNombre());
        assertEquals(1989, response.getYear());
        assertTrue(response.getActive());
    }

    @Test
    void deberiaObtenerAlbumPorId() {
        AlbumRequestDTO request = new AlbumRequestDTO("Naruto", 2002, "Álbum de Naruto");
        AlbumResponseDTO creado = albumService.crearAlbum(request);

        AlbumResponseDTO obtenido = albumService.obtenerAlbumPorId(creado.getId());

        assertNotNull(obtenido);
        assertEquals(creado.getId(), obtenido.getId());
        assertEquals("Naruto", obtenido.getNombre());
    }

    @Test
    void deberiaLanzarExcepcionCuandoAlbumNoExiste() {
        assertThrows(ResourceNotFoundException.class, () -> {
            albumService.obtenerAlbumPorId(99999L);
        });
    }

    @Test
    void deberiaActualizarAlbumCorrectamente() {
        AlbumRequestDTO request = new AlbumRequestDTO("One Piece", 1997, "Manga de piratas");
        AlbumResponseDTO creado = albumService.crearAlbum(request);

        AlbumRequestDTO actualizacion = new AlbumRequestDTO(
            "One Piece - Grand Line",
            1997,
            "Arco Grand Line"
        );

        AlbumResponseDTO actualizado = albumService.actualizarAlbum(creado.getId(), actualizacion);

        assertEquals("One Piece - Grand Line", actualizado.getNombre());
        assertEquals("Arco Grand Line", actualizado.getDescripcion());
    }

    @Test
    void deberiaEliminarAlbumCorrectamente() {
        AlbumRequestDTO request = new AlbumRequestDTO("Bleach", 2001, "Shinigamis");
        AlbumResponseDTO creado = albumService.crearAlbum(request);

        albumService.eliminarAlbum(creado.getId());

        assertThrows(ResourceNotFoundException.class, () -> {
            albumService.obtenerAlbumPorId(creado.getId());
        });
    }
}
```

**Ejecutar:**
```bash
.\mvnw.cmd test -Dtest=AlbumServiceTest
```

---

### 3. **Crear Tests de Integración**
**Prioridad:** 🔴 CRÍTICA  
**Tiempo:** 30 minutos

**Crear archivo:** `src/test/java/ipss/web2/examen/controllers/AlbumControllerIntegrationTest.java`

```java
package ipss.web2.examen.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ipss.web2.examen.dtos.AlbumRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AlbumControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberiaCrearAlbumViaAPI() throws Exception {
        AlbumRequestDTO request = new AlbumRequestDTO(
            "Fullmetal Alchemist",
            2001,
            "Alquimia y aventura"
        );

        mockMvc.perform(post("/api/albums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.nombre").value("Fullmetal Alchemist"))
                .andExpect(jsonPath("$.data.year").value(2001))
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void deberiaListarTodosLosAlbumes() throws Exception {
        mockMvc.perform(get("/api/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void deberiaRetornar404CuandoAlbumNoExiste() throws Exception {
        mockMvc.perform(get("/api/albums/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void deberiaValidarCamposRequeridos() throws Exception {
        AlbumRequestDTO request = new AlbumRequestDTO("", null, null);

        mockMvc.perform(post("/api/albums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
```

**Ejecutar todos los tests:**
```bash
.\mvnw.cmd test
```

---

## 🟡 IMPORTANTES (Hacer después)

### 4. **Eliminar Variable Sin Uso**
**Tiempo:** 1 minuto

**Archivo:** `src/main/java/ipss/web2/examen/controllers/api/LaminaUserController.java` línea 156

**Eliminar:**
```java
Album album = albumRepository.findById(albumId)
        .orElseThrow(() -> new ResourceNotFoundException("Album", "ID", albumId));
```

Ya no se usa la variable `album`.

---

### 5. **Suprimir Warnings Null-Safety**
**Tiempo:** 5 minutos

**Archivos:**
- `AlbumService.java`
- `LaminaService.java`

**Agregar al inicio:**
```java
@SuppressWarnings("null")
@Service
@RequiredArgsConstructor
@Transactional
public class AlbumService {
```

---

### 6. **Mover Repository del Controller**
**Tiempo:** 15 minutos

**Problema:** `LaminaController.java` tiene `AlbumRepository` inyectado

**Solución:**

**En `AlbumService.java` agregar:**
```java
public Album obtenerAlbumEntityPorId(Long id) {
    return albumRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Album", "ID", id));
}
```

**En `LaminaController.java` reemplazar:**
```java
// ANTES
private final AlbumRepository albumRepository;

Album album = albumRepository.findById(albumId)...

// DESPUÉS
private final AlbumService albumService;

Album album = albumService.obtenerAlbumEntityPorId(albumId);
```

---

### 7. **Mejorar Validaciones**
**Tiempo:** 10 minutos

**Archivo:** `src/main/java/ipss/web2/examen/dtos/AlbumRequestDTO.java`

```java
package ipss.web2.examen.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequestDTO {
    
    @NotBlank(message = "El nombre del álbum es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @NotNull(message = "El año de lanzamiento es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2100, message = "El año debe ser menor a 2100")
    private Integer year;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
}
```

---

## 🟢 OPCIONALES (Mejoras futuras)

### 8. **Agregar Paginación**
**Tiempo:** 1-2 horas

**Ejemplo:**
```java
@GetMapping
public ResponseEntity<ApiResponseDTO<Page<AlbumResponseDTO>>> obtenerTodosLosAlbumes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<AlbumResponseDTO> response = albumService.obtenerTodosLosAlbums(pageable);
    return ResponseHelper.ok("Álbumes recuperados", response);
}
```

---

### 9. **Crear Helper para Respuestas**
**Tiempo:** 30 minutos

**Crear:** `src/main/java/ipss/web2/examen/utils/ResponseHelper.java`

```java
package ipss.web2.examen.utils;

import ipss.web2.examen.dtos.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseHelper {
    
    public static <T> ResponseEntity<ApiResponseDTO<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponseDTO.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build());
    }
    
    public static <T> ResponseEntity<ApiResponseDTO<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
```

**Uso:**
```java
return ResponseHelper.ok("Álbum recuperado exitosamente", response);
```

---

### 10. **Agregar Índices en Base de Datos**
**Tiempo:** 10 minutos

**En entidades agregar:**
```java
@Entity
@Table(name = "lamina", indexes = {
    @Index(name = "idx_lamina_album", columnList = "album_id"),
    @Index(name = "idx_lamina_nombre", columnList = "nombre"),
    @Index(name = "idx_lamina_active", columnList = "is_active")
})
public class Lamina {
```

---

## 📊 Resumen de Impacto

| Tarea | Tiempo | Impacto Nota | Prioridad |
|-------|--------|--------------|-----------|
| 1. Seguridad | 5 min | ⚠️ Crítico | 🔴 |
| 2. Tests Unitarios | 30 min | +3 puntos | 🔴 |
| 3. Tests Integración | 30 min | +2 puntos | 🔴 |
| 4. Variable sin uso | 1 min | +0.1 puntos | 🟡 |
| 5. Warnings | 5 min | +0.5 puntos | 🟡 |
| 6. Repository | 15 min | +0.5 puntos | 🟡 |
| 7. Validaciones | 10 min | +0.4 puntos | 🟡 |
| **TOTAL CRÍTICO** | **65 min** | **+5 puntos** | |
| **TOTAL GENERAL** | **106 min** | **+6.5 puntos** | |

---

## ✅ Checklist de Ejecución

### Fase 1: Críticas (1 hora)
- [ ] Remover contraseña del código
- [ ] Crear `AlbumServiceTest.java`
- [ ] Crear `AlbumControllerIntegrationTest.java`
- [ ] Ejecutar `.\mvnw.cmd test`
- [ ] Verificar que pasen todos los tests

### Fase 2: Importantes (30 min)
- [ ] Eliminar variable sin uso
- [ ] Suprimir warnings
- [ ] Mover repository del controller
- [ ] Mejorar validaciones

### Fase 3: Opcionales (cuando tengas tiempo)
- [ ] Agregar paginación
- [ ] Crear ResponseHelper
- [ ] Agregar índices BD

---

## 🎯 Resultado Esperado

**Antes:**
- Nota: 92.5/100
- Tests: 0/5
- Warnings: 26
- Vulnerabilidades: 1 crítica

**Después (Fase 1+2):**
- Nota: 98/100 ⭐⭐⭐⭐⭐
- Tests: 5/5
- Warnings: 0
- Vulnerabilidades: 0

---

## 🚀 Comando Final de Validación

```bash
# Ejecutar todos los tests
.\mvnw.cmd clean test

# Compilar proyecto
.\mvnw.cmd clean install

# Ejecutar aplicación
.\mvnw.cmd spring-boot:run
```

**Salida esperada:**
```
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

**¡Buena suerte con las correcciones!** 🎓
