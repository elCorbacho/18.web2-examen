# Copilot Instructions - Web2 Examen App

## Project Overview
Aplicación Spring Boot 3.5.9 para examen de Web 2, con arquitectura REST, JPA y soporte para H2/PostgreSQL.

**ROL del Asistente:**
Arquitecto de software experto en Spring Boot para aplicaciones web empresariales y APIs RESTful, con dominio avanzado en el ecosistema Spring Boot.

**Stack principal:**
- Java 21
- Spring Boot 3.5.9 (Web, Data JPA, Actuator)
- Lombok para reducir boilerplate
- H2 (desarrollo) / PostgreSQL (producción)
- Maven con wrapper incluido

## Architecture & Structure

**Package base:** `ipss.web2.examen`

La estructura sigue las convenciones estándar de Spring Boot:
- `src/main/java/ipss/web2/examen/` - Código fuente de la aplicación
- `src/main/resources/` - Configuración y recursos estáticos
- `src/test/java/ipss/web2/examen/` - Tests unitarios e integración

## Development Workflows

**Build & Run:**
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Tests:**
```bash
.\mvnw.cmd test
```

**DevTools activo:** Hot-reload automático al guardar cambios en desarrollo.

## Code Standards

### Principios de Diseño
- **SOLID principles** - Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- **CLEAN CODE practices** - Código legible, mantenible y autoexplicativo
- **DRY (Don't Repeat Yourself)** - Evitar duplicación de lógica
- **RESTful API design standard** - Seguir convenciones REST para endpoints
- **MVC architecture** - Separación clara entre Modelo, Vista y Controlador

### Naming Conventions
- **PascalCase:** Clases, Interfaces, Enums (`UserEntity`, `ProductRepository`, `OrderStatus`)
- **camelCase:** Variables, métodos, funciones (`findUserById`, `userName`, `calculateTotal`)
- **UPPER_SNAKE_CASE:** Constantes (`MAX_RETRY_ATTEMPTS`, `DEFAULT_PAGE_SIZE`)

### Comentarios
Usar comentarios básicos en español para documentar lógica compleja o decisiones de diseño.

## Code Conventions

### Entities & DTOs
- Usar **Lombok** para reducir código: `@Data`, `@Entity`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Las entidades JPA van en un paquete `model` o `entity`
- DTOs en paquete `dto` si es necesario

### Controllers
- Usar `@RestController` para endpoints REST
- Prefijo base: `/api` para endpoints de negocio
- Actuator disponible en `/actuator` para monitoreo

### Services & Repositories
- Repositorios: interfaces extendiendo `JpaRepository<Entity, ID>`
- Servicios: lógica de negocio con `@Service`
- Inyección de dependencias vía constructor (recomendado con Lombok `@RequiredArgsConstructor`)

### DTOs & Mappers Pattern

**Siempre usar DTOs** para entrada/salida de API:
- `*RequestDTO` - Para datos de entrada (con validaciones `@Valid`)
- `*ResponseDTO` - Para datos de salida (solo campos públicos)
- Nunca exponer entidades JPA directamente en controllers

**Mappers obligatorios:**
- Componente `@Component` dedicado por entidad: `*Mapper`
- Métodos: `toResponseDTO()`, `toEntity()`, `updateEntity()`
- Centralizar toda lógica de conversión (evitar mapeo en controllers/services)

**Ejemplo de estructura:**
```
dto/
  UsuarioRequestDTO.java
  UsuarioResponseDTO.java
mapper/
  UsuarioMapper.java
model/
  Usuario.java
```

## Database Configuration

### Principios ACID
Todas las transacciones deben cumplir con principios ACID (Atomicity, Consistency, Isolation, Durability).
Usar `@Transactional` en servicios para operaciones críticas.

### Audit Fields & Soft Delete Pattern
Implementar campos de auditoría en entidades:
```java
@CreatedDate
private LocalDateTime createdAt;

@LastModifiedDate
private LocalDateTime updatedAt;

@Column(name = "is_active")
private Boolean active = true;  // Soft delete pattern
```

Preferir soft delete (flag `active`) sobre borrado físico para mantener trazabilidad.

### Configuración de Base de Datos

**Por defecto:** H2 en memoria para desarrollo rápido

**Para PostgreSQL:** Añadir en `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/examen
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

La consola H2 está disponible en desarrollo para inspección de datos.

## Actuator Endpoints
Monitoreo y métricas disponibles en `/actuator` - útil para health checks y debugging.
