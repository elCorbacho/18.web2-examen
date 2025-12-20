# 📚 Sistema de Gestión de Álbumes y Láminas

API REST desarrollada con Spring Boot 3.5.9 para la gestión completa de álbumes de colección y sus láminas. Incluye validación automática contra catálogo, detección de repetidas y operaciones CRUD completas.

## 🚀 Inicio Rápido

### Prerrequisitos
- Java 21 o superior
- Maven (incluido wrapper en el proyecto)
- Git

### 1. Clonar el Repositorio
```bash
git clone <https://github.com/elCorbacho/18.web2-examen>
cd 18.web2-examen
```

### 2. Instalar Dependencias
```bash
# Windows
.\mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

### 3. Ejecutar la Aplicación
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```
### 4. Acceder a la Aplicación
La aplicación estará disponible en: **http://localhost:8080**

### Base de Datos
Montada con MySQL en AWS RDS.

## 📡 Endpoints Disponibles

### 🎯 1. Gestión de Álbumes - `/api/albums`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/albums` | Crear un nuevo álbum |
| GET | `/api/albums` | Listar todos los álbumes |
| GET | `/api/albums/{id}` | Obtener álbum por ID |
| PUT | `/api/albums/{id}` | Actualizar álbum existente |
| DELETE | `/api/albums/{id}` | Eliminar álbum (soft delete) |

### 🏷️ 2. Gestión de Láminas - `/api/laminas`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/laminas` | Agregar lámina con validación automática contra catálogo y detección de repetidas |
| GET | `/api/laminas` | Listar todas las láminas del sistema |
| GET | `/api/laminas/{id}` | Obtener lámina por ID |
| GET | `/api/laminas/album/{albumId}` | Obtener láminas de un álbum específico |
| PUT | `/api/laminas/{id}` | Actualizar información de lámina |
| DELETE | `/api/laminas/{id}` | Eliminar lámina (soft delete) |

### 📖 3. Catálogo y Estado - `/api/albums/{albumId}/catalogo`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/albums/{albumId}/catalogo` | Crear catálogo de láminas para un álbum |
| GET | `/api/albums/{albumId}/catalogo` | Ver catálogo disponible del álbum |
| GET | `/api/albums/{albumId}/catalogo/estado` | Ver estado completo: láminas poseídas, faltantes, repetidas y totales |

### 📦 4. Carga Masiva - `/api/laminas/masivo`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/laminas/masivo` | Agregar múltiples láminas en una solicitud (valida cada una individualmente) |

### 🏥 5. Monitoreo - `/actuator`
Spring Boot Actuator disponible para health checks y métricas del sistema.

---

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.9**
  - Spring Web (REST APIs)
  - Spring Data JPA (Persistencia)
  - Spring Boot Actuator (Monitoreo)
  - Spring Boot DevTools (Hot-reload)
- **Lombok** (Reducción de boilerplate)
- **Maven** (Gestión de dependencias)

---

## 📝 Características Principales

✅ **CRUD Completo** para álbumes y láminas  
✅ **Validación automática** contra catálogo  
✅ **Detección de láminas repetidas**  
✅ **Soft Delete** para mantener trazabilidad  
✅ **Carga masiva** de láminas  
✅ **Estado en tiempo real** del progreso de colección  
✅ **Auditoría automática** con timestamps  
✅ **Arquitectura REST** siguiendo mejores prácticas  

---
