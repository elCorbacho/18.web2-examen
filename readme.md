<div align="center">

# 📚 Sistema de Gestión de Álbumes y Láminas

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**API REST completa para gestión de álbumes de colección**

[Características](#-características-principales) •
[Instalación](#-instalación-y-configuración) •
[Endpoints](#-api-endpoints) •
[Tecnologías](#️-stack-tecnológico)

</div>

---

## 📖 Descripción

API REST profesional desarrollada con **Spring Boot 3.5.9** para la gestión integral de álbumes de colección y sus láminas. Sistema diseñado con arquitectura limpia, validación automática contra catálogo maestro, detección inteligente de láminas repetidas y operaciones CRUD completas.

### ✨ Características Destacadas

- ✅ **CRUD Completo** - Operaciones completas para álbumes y láminas
- 🔍 **Validación Automática** - Validación contra catálogo maestro
- 🔄 **Detección de Duplicados** - Identificación automática de láminas repetidas
- 🗑️ **Soft Delete** - Eliminación lógica para mantener trazabilidad
- 📦 **Carga Masiva** - Importación de múltiples láminas simultáneamente
- 📊 **Estado en Tiempo Real** - Progreso de colección instantáneo
- 📝 **Auditoría Automática** - Timestamps automáticos en todas las operaciones
- 🏗️ **Arquitectura REST** - Diseño siguiendo mejores prácticas RESTful

---

## 🚀 Instalación y Configuración

### 📋 Prerrequisitos

Asegúrate de tener instalado:

- ☕ **Java 21** o superior
- 📦 **Maven** (incluido wrapper en el proyecto)
- 🔧 **Git**

### 📥 Instalación

#### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/elCorbacho/18.web2-examen
cd 18.web2-examen
```

#### 2️⃣ Instalar Dependencias

**Windows:**
```bash
.\mvnw.cmd clean install
```

**Linux/Mac:**
```bash
./mvnw clean install
```

#### 3️⃣ Ejecutar la Aplicación

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

#### 4️⃣ Acceder a la Aplicación

Una vez iniciada la aplicación, accede a:

🌐 **API Base URL:** `http://localhost:8080`  
🔍 **Actuator Health:** `http://localhost:8080/actuator/health`
📜 **Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## 💾 Base de Datos

El proyecto utiliza **MySQL** montado en **AWS RDS** para producción.



Consola H2: `http://localhost:8080/h2-console`


## 📡 API Endpoints

### 🎯 1. Gestión de Álbumes
**Base:** `/api/albums`

<table>
<thead>
<tr>
<th width="80">Método</th>
<th width="250">Endpoint</th>
<th>Descripción</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>POST</code></td>
<td><code>/api/albums</code></td>
<td>Crear un nuevo álbum de colección</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/albums</code></td>
<td>Listar todos los álbumes registrados</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/albums/{id}</code></td>
<td>Obtener detalles de un álbum específico</td>
</tr>
<tr>
<td><code>PUT</code></td>
<td><code>/api/albums/{id}</code></td>
<td>Actualizar información de un álbum</td>
</tr>
<tr>
<td><code>DELETE</code></td>
<td><code>/api/albums/{id}</code></td>
<td>Eliminar álbum (soft delete)</td>
</tr>
</tbody>
</table>

---

### 🏷️ 2. Gestión de Láminas (Usuario)
**Base:** `/api/laminas`

> 💡 **Validación automática contra catálogo + Detección de repetidas**

<table>
<thead>
<tr>
<th width="80">Método</th>
<th width="300">Endpoint</th>
<th>Descripción</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>POST</code></td>
<td><code>/api/laminas</code></td>
<td>Agregar lámina con validación y detección de repetidas</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/laminas</code></td>
<td>Listar todas las láminas del sistema</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/laminas/{id}</code></td>
<td>Obtener detalles de una lámina específica</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/laminas/album/{albumId}</code></td>
<td>Listar todas las láminas de un álbum</td>
</tr>
<tr>
<td><code>PUT</code></td>
<td><code>/api/laminas/{id}</code></td>
<td>Actualizar información de una lámina</td>
</tr>
<tr>
<td><code>DELETE</code></td>
<td><code>/api/laminas/{id}</code></td>
<td>Eliminar lámina (soft delete)</td>
</tr>
</tbody>
</table>

---

### 📖 3. Catálogo y Estadísticas
**Base:** `/api/albums/{albumId}/catalogo`

> 📊 **Administración del catálogo maestro y seguimiento de progreso**

<table>
<thead>
<tr>
<th width="80">Método</th>
<th width="350">Endpoint</th>
<th>Descripción</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>POST</code></td>
<td><code>/api/albums/{albumId}/catalogo</code></td>
<td>Crear catálogo maestro de láminas</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/albums/{albumId}/catalogo</code></td>
<td>Ver catálogo completo disponible</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/api/albums/{albumId}/catalogo/estado</code></td>
<td>Ver estadísticas: poseídas, faltantes, repetidas y totales</td>
</tr>
</tbody>
</table>

---

### 📦 4. Operaciones Masivas
**Base:** `/api/laminas/masivo`

> ⚡ **Carga rápida de múltiples láminas**

<table>
<thead>
<tr>
<th width="80">Método</th>
<th width="250">Endpoint</th>
<th>Descripción</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>POST</code></td>
<td><code>/api/laminas/masivo</code></td>
<td>Agregar múltiples láminas (valida cada una individualmente)</td>
</tr>
</tbody>
</table>

---

### 🏥 5. Monitoreo y Salud
**Base:** `/actuator`

> 🔍 **Spring Boot Actuator para monitoring**

<table>
<thead>
<tr>
<th width="80">Método</th>
<th width="250">Endpoint</th>
<th>Descripción</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>GET</code></td>
<td><code>/actuator/health</code></td>
<td>Estado de salud de la aplicación</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/actuator/info</code></td>
<td>Información de la aplicación</td>
</tr>
<tr>
<td><code>GET</code></td>
<td><code>/actuator</code></td>
<td>Lista completa de endpoints disponibles</td>
</tr>
</tbody>
</table>

---

## 🛠️ Stack Tecnológico

### Backend Framework
- ☕ **Java 21** - Lenguaje de programación
- 🍃 **Spring Boot 3.5.9** - Framework principal

### Dependencias Spring
- 🌐 **Spring Web** - Construcción de APIs REST
- 🗄️ **Spring Data JPA** - Persistencia y gestión de datos
- 📊 **Spring Boot Actuator** - Monitoreo y métricas
- 🔥 **Spring Boot DevTools** - Hot-reload en desarrollo

### Base de Datos
- 🐬 **MySQL** - Base de datos en AWS RDS (Producción)
- 💾 **H2 Database** - Base de datos en memoria (Desarrollo)

### Herramientas y Librerías
- 🧰 **Lombok** - Reducción de código boilerplate
- 📦 **Maven** - Gestión de dependencias y construcción
- ✅ **Jakarta Validation** - Validación de datos

### Arquitectura
- 🏗️ **MVC Pattern** - Separación de capas
- 🔄 **DTOs & Mappers** - Transferencia segura de datos
- 🗑️ **Soft Delete Pattern** - Eliminación lógica
- ⏰ **JPA Auditing** - Auditoría automática

---

## 🧪 Testing

Ejecutar la suite de tests:

**Windows:**
```bash
.\mvnw.cmd test
```

**Linux/Mac:**
```bash
./mvnw test
```

---

## 📚 Documentación Adicional

Para más detalles sobre los endpoints, ejemplos de peticiones y respuestas, consulta:

📄 [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

---

## 🤝 Contribución

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo `LICENSE` para más detalles.

---

## 👨‍💻 Autor

**Proyecto Web 2 - Examen**

Desarrollado con ❤️ usando Spring Boot

---

<div align="center">

⭐ Si te gusta este proyecto, dale una estrella en GitHub ⭐

</div>
