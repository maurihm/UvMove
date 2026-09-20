# UV Move - Prueba de Concepto (PoC)

Este repositorio contiene la Prueba de Concepto (PoC) del proyecto **UV Move**, demostrando la colaboración exitosa entre dos módulos: **Control de Viajes** e **Inventario de Flota**.

## 1. Estructura del Repositorio

- **`backend/`**: Código fuente en Java (Spring Boot) conteniendo los dos módulos (Control de Viajes e Inventario de Flota), incluyendo el contrato de comunicación, las entidades y la conexión a la base de datos.
- **`frontend/`**: Interfaz web mínima desarrollada en React + Vite que consume el API del backend y maneja la autenticación.
- **`init.sql`**: Script de base de datos para inicializar el esquema y los datos en IBM Db2.
- **`.env.example`**: Archivo de ejemplo en la carpeta `frontend/` para configurar las llaves de Supabase (las llaves reales fueron omitidas por seguridad).

---

## 2. Evidencias de Funcionamiento

*(Profesor/Revisor: A continuación se anexan las evidencias gráficas del funcionamiento de los requerimientos)*

### Evidencia de Persistencia (IBM Db2)
Los datos están siendo guardados y recuperados correctamente desde una instancia real de IBM Db2.
![Evidencia de Persistencia Db2](./evidencia_db2.png?v=2)

### Escenario 1 - Flujo Exitoso y Protección de Estado
Se solicita el inicio de un viaje con un vehículo en condiciones óptimas (V-001 con 85% de batería). 

**Paso A (Éxito):** Al primer clic, el módulo de Inventario lo aprueba, cambia su estado a "En_Uso", y el de Viajes guarda el registro exitosamente.
![Escenario 1 Exitoso](./escenario1_exito.png?v=2)

**Paso B (Bloqueo por RN7):** Si el usuario intenta iniciar viaje nuevamente con ese mismo vehículo, el backend detecta que ya no está "Disponible" y bloquea la operación aplicando la RN7. ¡Esto demuestra que el estado persistió y las reglas protegen el sistema!
![Escenario 1 Fallo RN7](./escenario1_fallo.png?v=2)

### Escenario 2 - Rechazo por Regla de Negocio
Se intenta iniciar un viaje con un vehículo con baja carga (V-002 con 5% de batería). La regla de negocio detecta la condición e impide que la operación continúe.
![Escenario 2 Rechazo](./escenario2.png?v=2)

---

## 3. Documentación y Trazabilidad

### Trazabilidad Final
- El **Requisito / Regla de Negocio RN6** (Batería mínima del 10% para iniciar viaje) se implementó de forma estricta en el backend, específicamente en el módulo **Inventario de Flota** (`InventarioService.java`). 
- Su ejecución se demuestra en la pantalla de error de la interfaz web cuando se ejecuta el Escenario 2.

### Actualización del Modelo
- **No hubo ajustes mayores**. El modelo de datos propuesto originalmente para los vehículos (V-001 y V-002) y los viajes mapeó perfectamente con las entidades y relaciones implementadas en Db2 y en JPA (Spring Boot).
