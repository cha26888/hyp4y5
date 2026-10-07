# RentaMovil

Ejercicios 4 y 5 de CC2008 (Programación Orientada a Objetos), Ciclo 2 2026, Universidad del Valle de Guatemala.

Autores: Henry Emanuel Vicente Alvarado (26275) y Joel Alejandro Chávez Pérez (26888)

Sistema de consola para administrar la flota, los clientes y los alquileres de una empresa de renta de vehículos, usando herencia, clases abstractas y polimorfismo con el patrón MVC.

## Estructura

| Capa | Clases |
|---|---|
| Modelo | `EstadoVehiculo`, `TipoLicencia`, `Vehiculo` (abstracta), `Automovil`, `Motocicleta`, `CamionetaCarga`, `Microbus`, `Cliente` (abstracta), `ClienteIndividual`, `ClienteCorporativo`, `Cotizacion`, `Alquiler`, `RentaMovil` |
| Vista | `VistaRentaMovil` |
| Controlador | `ControladorRentaMovil` |
| Driver | `Main` |

## Compilar y ejecutar

```
javac bin*.java
java Main
```

## Datos iniciales

- 2 vehículos por categoría, todos disponibles. La motocicleta `M222BBB` inicia con 18 de 20 días acumulados (cerca del umbral).
- Clientes individuales: `1234567890101` (Ana López, 3 alquileres confirmados, ya recibe descuento) y `2345678901202` (Carlos Pérez).
- Clientes corporativos: `1234567-8` (Logística del Valle S.A.) y `9876543-2` (Turismo Quetzal S.A.).
