import java.util.ArrayList;

public class ControladorRentaMovil {
    private RentaMovil empresa;
    private VistaRentaMovil vista;

    public ControladorRentaMovil(RentaMovil empresa, VistaRentaMovil vista) {
        this.empresa = empresa;
        this.vista = vista;
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            vista.mostrarMenu();
            opcion = vista.leerOpcion();
            try {
                ejecutarOpcion(opcion);
            } catch (IllegalArgumentException | IllegalStateException e) {
                vista.mostrarError(e.getMessage());
            }
        }
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarVehiculo();
                break;
            case 2:
                registrarCliente();
                break;
            case 3:
                consultarFlota();
                break;
            case 4:
                consultarClientes();
                break;
            case 5:
                cotizar();
                break;
            case 6:
                alquilar();
                break;
            case 7:
                registrarDevolucion();
                break;
            case 8:
                finalizarMantenimiento();
                break;
            case 9:
                mostrarReporteFlota();
                break;
            case 10:
                mostrarReporteIngresos();
                break;
            case 11:
                mostrarAlquileresActivos();
                break;
            case 12:
                mostrarHistorialCliente();
                break;
            case 0:
                vista.mostrarMensaje("Saliendo de RentaMovil. ¡Hasta pronto!");
                break;
            default:
                vista.mostrarError("Opción no válida.");
        }
    }

    private void registrarVehiculo() {
        vista.mostrarMensaje("Categorías: 1. Automóvil  2. Motocicleta  3. Camioneta de carga  4. Microbús");
        int categoria = vista.leerEntero("Categoría: ");
        if (categoria < 1 || categoria > 4) {
            vista.mostrarError("Categoría no válida.");
            return;
        }
        String placa = vista.leerTexto("Placa: ");
        while (placa.isEmpty() || empresa.existeVehiculo(placa)) {
            if (placa.isEmpty()) {
                vista.mostrarError("La placa no puede estar vacía.");
            } else {
                vista.mostrarError("Ya existe un vehículo con la placa " + placa.toUpperCase() + ".");
            }
            placa = vista.leerTexto("Placa: ");
        }
        String marca = vista.leerTextoNoVacio("Marca: ");
        String modelo = vista.leerTextoNoVacio("Modelo: ");
        double tarifa = vista.leerDecimalPositivo("Tarifa diaria (Q): ");
        Vehiculo vehiculo;
        if (categoria == 1) {
            int pasajeros = vista.leerEnteroPositivo("Cantidad de pasajeros: ");
            boolean automatica = vista.leerBooleano("¿Transmisión automática?");
            vehiculo = new Automovil(placa, marca, modelo, tarifa, pasajeros, automatica);
        } else if (categoria == 2) {
            int cilindraje = vista.leerEnteroPositivo("Cilindraje (cc): ");
            vehiculo = new Motocicleta(placa, marca, modelo, tarifa, cilindraje);
        } else if (categoria == 3) {
            double capacidad = vista.leerDecimalPositivo("Capacidad máxima (toneladas): ");
            vehiculo = new CamionetaCarga(placa, marca, modelo, tarifa, capacidad);
        } else {
            int pasajeros = vista.leerEnteroPositivo("Cantidad de pasajeros: ");
            boolean piloto = vista.leerBooleano("¿Incluye piloto?");
            vehiculo = new Microbus(placa, marca, modelo, tarifa, pasajeros, piloto);
        }
        empresa.registrarVehiculo(vehiculo);
        vista.mostrarMensaje("Vehículo registrado: " + vehiculo.describir());
    }

    private void registrarCliente() {
        vista.mostrarMensaje("Tipos: 1. Individual  2. Corporativo");
        int tipo = vista.leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 2) {
            vista.mostrarError("Tipo no válido.");
            return;
        }
        Cliente cliente;
        if (tipo == 1) {
            String dpi = vista.leerTexto("DPI (13 dígitos): ");
            while (!ClienteIndividual.esDpiValido(dpi) || empresa.existeCliente(dpi)) {
                if (!ClienteIndividual.esDpiValido(dpi)) {
                    vista.mostrarError("El DPI debe tener exactamente 13 dígitos.");
                } else {
                    vista.mostrarError("Ya existe un cliente con el DPI " + dpi + ".");
                }
                dpi = vista.leerTexto("DPI (13 dígitos): ");
            }
            String nombre = vista.leerTextoNoVacio("Nombre: ");
            ArrayList<TipoLicencia> licencias = vista.leerLicencias();
            cliente = new ClienteIndividual(dpi, nombre, licencias);
        } else {
            String nit = vista.leerTexto("NIT: ");
            while (nit.isEmpty() || empresa.existeCliente(nit)) {
                if (nit.isEmpty()) {
                    vista.mostrarError("El NIT no puede estar vacío.");
                } else {
                    vista.mostrarError("Ya existe un cliente con el NIT " + nit + ".");
                }
                nit = vista.leerTexto("NIT: ");
            }
            String empresaNombre = vista.leerTextoNoVacio("Nombre de la empresa: ");
            String contacto = vista.leerTextoNoVacio("Nombre del contacto: ");
            vista.mostrarMensaje("Licencias de los pilotos de la empresa:");
            ArrayList<TipoLicencia> licencias = vista.leerLicencias();
            cliente = new ClienteCorporativo(nit, empresaNombre, contacto, licencias);
        }
        empresa.registrarCliente(cliente);
        vista.mostrarMensaje("Cliente registrado: " + cliente.describir());
    }

    private void consultarFlota() {
        vista.mostrarFlota(empresa.obtenerFlota());
    }

    private void consultarClientes() {
        vista.mostrarClientes(empresa.obtenerClientes());
    }

    private void cotizar() {
        String placa = leerPlacaExistente();
        String identificador = leerClienteExistente();
        int dias = vista.leerEnteroPositivo("Días: ");
        Cotizacion cotizacion = empresa.cotizar(placa, identificador, dias);
        vista.mostrarCotizacion(cotizacion);
    }

    private void alquilar() {
        String placa = leerPlacaExistente();
        EstadoVehiculo estado = empresa.obtenerEstadoVehiculo(placa);
        if (estado != EstadoVehiculo.DISPONIBLE) {
            vista.mostrarError("El vehículo " + placa.toUpperCase() + " no está disponible (" + estado.getDescripcion() + "). No se puede alquilar.");
            return;
        }
        String identificador = leerClienteExistente();
        ArrayList<String> razones = empresa.revisarCondiciones(placa, identificador);
        if (!razones.isEmpty()) {
            vista.mostrarError("No se puede alquilar:");
            for (String razon : razones) {
                vista.mostrarError("  - " + razon);
            }
            return;
        }
        int dias = vista.leerEnteroPositivo("Días: ");
        Cotizacion cotizacion = empresa.cotizar(placa, identificador, dias);
        vista.mostrarCotizacion(cotizacion);
        if (!vista.leerBooleano("¿El cliente acepta el cobro?")) {
            vista.mostrarMensaje("Alquiler cancelado. No se realizaron cambios.");
            return;
        }
        Alquiler alquiler = empresa.confirmarAlquiler(cotizacion);
        vista.mostrarMensaje("Alquiler confirmado:");
        vista.mostrarAlquiler(alquiler);
    }

    private void registrarDevolucion() {
        String placa = vista.leerTexto("Placa del vehículo devuelto: ");
        Alquiler alquiler = empresa.registrarDevolucion(placa);
        Vehiculo vehiculo = alquiler.getVehiculo();
        vista.mostrarMensaje("Devolución registrada. Alquiler #" + alquiler.getNumero() + " finalizado.");
        vista.mostrarMensaje("Días acumulados: " + vehiculo.getDiasAcumulados() + "/" + vehiculo.getUmbralMantenimiento());
        if (vehiculo.getEstado() == EstadoVehiculo.EN_MANTENIMIENTO) {
            vista.mostrarMensaje("El vehículo " + vehiculo.getPlaca() + " alcanzó su umbral y pasó a mantenimiento.");
        } else {
            vista.mostrarMensaje("El vehículo " + vehiculo.getPlaca() + " quedó disponible.");
        }
    }

    private void finalizarMantenimiento() {
        String placa = vista.leerTexto("Placa del vehículo: ");
        empresa.finalizarMantenimiento(placa);
        vista.mostrarMensaje("Mantenimiento finalizado. El vehículo quedó disponible con 0 días acumulados.");
    }

    private void mostrarReporteFlota() {
        vista.mostrarMensaje("");
        vista.mostrarMensaje("--- Reporte de flota ---");
        for (String categoria : empresa.obtenerCategorias()) {
            vista.mostrarFilaFlota(categoria,
                    empresa.contarVehiculos(categoria, null),
                    empresa.contarVehiculos(categoria, EstadoVehiculo.DISPONIBLE),
                    empresa.contarVehiculos(categoria, EstadoVehiculo.ALQUILADO),
                    empresa.contarVehiculos(categoria, EstadoVehiculo.EN_MANTENIMIENTO));
        }
        vista.mostrarFilaFlota("TOTAL",
                empresa.contarVehiculos(null, null),
                empresa.contarVehiculos(null, EstadoVehiculo.DISPONIBLE),
                empresa.contarVehiculos(null, EstadoVehiculo.ALQUILADO),
                empresa.contarVehiculos(null, EstadoVehiculo.EN_MANTENIMIENTO));
    }

    private void mostrarReporteIngresos() {
        vista.mostrarMensaje("");
        vista.mostrarMensaje("--- Reporte de ingresos ---");
        for (String categoria : empresa.obtenerCategorias()) {
            vista.mostrarIngresoCategoria(categoria, empresa.calcularIngresosCategoria(categoria));
        }
        vista.mostrarTotalesIngresos(empresa.calcularIngresosTotales(), empresa.calcularDescuentosTotales());
    }

    private void mostrarAlquileresActivos() {
        vista.mostrarAlquileres("Alquileres activos", empresa.obtenerAlquileresActivos());
    }

    private void mostrarHistorialCliente() {
        String identificador = vista.leerTexto("DPI o NIT del cliente: ");
        ArrayList<Alquiler> historial = empresa.obtenerHistorialCliente(identificador);
        double totalPagado = empresa.calcularTotalPagadoCliente(identificador);
        vista.mostrarHistorialCliente(identificador, historial, totalPagado);
    }

    private String leerPlacaExistente() {
        String placa = vista.leerTexto("Placa del vehículo: ");
        while (!empresa.existeVehiculo(placa)) {
            vista.mostrarError("No existe un vehículo con la placa " + placa.toUpperCase() + ".");
            placa = vista.leerTexto("Placa del vehículo: ");
        }
        return placa;
    }

    private String leerClienteExistente() {
        String identificador = vista.leerTexto("DPI o NIT del cliente: ");
        while (!empresa.existeCliente(identificador)) {
            vista.mostrarError("No existe un cliente con el DPI o NIT " + identificador + ".");
            identificador = vista.leerTexto("DPI o NIT del cliente: ");
        }
        return identificador;
    }
}
