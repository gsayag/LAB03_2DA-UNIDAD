# AutoCare

AutoCare es una aplicación móvil Android para la gestión y control del mantenimiento vehicular.

El proyecto permite registrar la información principal de un vehículo, almacenar mantenimientos realizados, controlar próximos servicios según fecha o kilometraje, consultar el historial y determinar automáticamente si un mantenimiento se encuentra al día, próximo o vencido.

La aplicación fue desarrollada como proyecto académico utilizando Kotlin, Jetpack Compose, Material 3 y Room.

---

## Descripción del problema

Los propietarios de vehículos suelen olvidar cuándo realizaron el último mantenimiento, cuánto pagaron por un servicio o cuándo deben realizar el siguiente cambio de aceite, revisión de frenos, llantas u otro mantenimiento.

AutoCare busca resolver este problema permitiendo centralizar esta información dentro de una aplicación móvil sencilla.

La aplicación permite conocer rápidamente:

- El vehículo registrado.
- El kilometraje actual.
- Los mantenimientos realizados.
- El costo de cada mantenimiento.
- La próxima fecha de mantenimiento.
- El próximo kilometraje recomendado.
- El estado actual de cada mantenimiento.

---

# Funcionalidades

## Gestión del vehículo

Permite registrar y actualizar:

- Marca.
- Modelo.
- Año.
- Placa.
- Kilometraje actual.

Los datos se almacenan localmente mediante Room.

---

## Registro de mantenimientos

Permite registrar:

- Tipo de mantenimiento.
- Fecha del servicio.
- Kilometraje del servicio.
- Costo.
- Próximo kilometraje.
- Próxima fecha.
- Observaciones.

---

## Historial

La aplicación permite consultar todos los mantenimientos registrados.

Cada registro muestra:

- Tipo.
- Estado.
- Fecha.
- Kilometraje.
- Costo.
- Próximo kilometraje.
- Próxima fecha.
- Observaciones.

---

## Edición de mantenimientos

Los mantenimientos existentes pueden modificarse sin crear un nuevo registro.

Esto permite corregir:

- Fechas.
- Costos.
- Kilometrajes.
- Observaciones.
- Próximos mantenimientos.

---

## Eliminación de mantenimientos

Antes de eliminar un mantenimiento se muestra un cuadro de confirmación.

Ejemplo:

```text
¿Eliminar mantenimiento?

Se eliminará "Cambio de aceite" del historial.
Esta acción no se puede deshacer.

Cancelar     Eliminar
