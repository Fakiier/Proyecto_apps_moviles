# AppCloro – Medición de cloro en línea

Aplicación móvil Android para consultar, registrar y dar seguimiento a los niveles de cloro libre del agua en las granjas de una empresa avícola y ganadera. Desarrollada para la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles** (Evaluación Parcial 2, Duoc UC).

## Integrantes
- Javier Romero
- Catalina Campos

Planificación en Trello: <<enlace al tablero>>

## Contexto
Los operarios, supervisores y jefaturas no tienen una herramienta móvil para ver desde terreno las mediciones, históricos y alertas de cloro. La app permite identificar rápido valores fuera de rango y registrar las acciones correctivas. Usa únicamente datos ficticios.

## Funcionalidades
- **Inicio de sesión por rol** (operario, supervisor, jefatura) con validación por campo.
- **Puntos de medición por granja**, con nivel de cloro y estado (normal, advertencia, crítico).
- **Registro de mediciones** con validación de nivel y fecha, y vista previa del estado.
- **Historial** por punto con filtro de rango y resumen (promedio, mínimo, máximo).
- **Alertas** generadas automáticamente al detectar valores fuera de rango, con cierre solo si hay acción correctiva y una medición normal posterior.
- **Acciones correctivas** con descripción validada y foto (cámara o galería).
- **Panel de jefatura** con resumen por granja y barra animada.
- **Notificación** cuando se registra una medición crítica.
- **Persistencia local** con Room: la app funciona sin conexión.
- **Sincronización con una API** propia: baja mediciones nuevas y sube las pendientes.
- **Animaciones**: transiciones entre pantallas, `AnimatedVisibility`, `Crossfade` y barra de progreso.

### Rangos de cloro libre (ppm)
| Estado | Rango |
|---|---|
| Normal | 3,0 a 5,0 |
| Advertencia | 2,0 a 3,0 y 5,0 a 6,0 |
| Crítico | menor a 2,0 o mayor a 6,0 |

### Recursos nativos
- Cámara y galería (acciones correctivas).
- Notificaciones (alertas críticas).

## Arquitectura y tecnologías
- Kotlin, Jetpack Compose, Material 3, Navigation Compose.
- **MVVM**: pantallas (`ui/screen`), ViewModels (`ui/viewmodel`), validaciones desacopladas (`validacion`), datos (`data`: modelos, repositorios, Room y cliente de la API).
- Room (SQLite) para persistencia, Retrofit para la API, Coil para imágenes.
- API: Spring Boot (Java 21, Maven) en la carpeta `api-cloro`, con datos simulados.

## Cómo ejecutar

### Requisitos
- Android Studio, y un emulador o un celular Android 7.0+ (API 24).
- JDK 21 para la API.

### 1. Levantar la API
```
cd api-cloro
.\mvnw.cmd spring-boot:run      # Windows
./mvnw spring-boot:run          # Linux / macOS
```
El servidor queda en el **puerto 8098** y genera una medición nueva por punto cada minuto. Comprobar: `http://localhost:8098/api/mediciones/ultimas`.

### 2. Configurar la dirección de la API en la app
Editar `URL_BASE` en `app/src/main/java/com/example/appcloro_grupojrcc/data/remote/ClienteApi.kt`:
- **Emulador:** `http://10.0.2.2:8098/`
- **Celular real:** `http://<IP de tu PC>:8098/` (ver con `ipconfig`). El celular y el PC deben estar en la misma red Wi-Fi y el firewall debe permitir el puerto 8098.

La app permite tráfico `http` (`usesCleartextTraffic`) solo porque es un prototipo académico.

### 3. Ejecutar la app
Abrir la carpeta del proyecto en Android Studio, sincronizar Gradle y ejecutar `app`. Aceptar el permiso de notificaciones cuando lo pida.

## Usuarios de prueba
Contraseña de todos: `1234`

| Correo | Rol | Granjas |
|---|---|---|
| operario1@cloro.cl | Operario | Norte |
| operario2@cloro.cl | Operario | Sur y Centro |
| supervisor1@cloro.cl | Supervisor | Norte y Sur |
| supervisor2@cloro.cl | Supervisor | Centro |
| jefatura@cloro.cl | Jefatura | Todas |

## Flujo de trabajo
Se trabajó con ramas `feature/...` y Pull Requests hacia `main`. Planificación y reparto de tareas en Trello.