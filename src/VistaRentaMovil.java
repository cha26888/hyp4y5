import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class VistaRentaMovil {
    private final Scanner scanner;
    private final DecimalFormat formatoMonto;

    public VistaRentaMovil() {
        this.scanner = new Scanner(System.in);
        this.formatoMonto = new DecimalFormat("#,##0.00");
    }

    public void mostrarMenu() {
        System.out.println();
        System.out.println("========== RentaMovil ==========");
        System.out.println("1. Registrar vehículo");
        System.out.println("2. Registrar cliente");
        System.out.println("3. Consultar flota");
        System.out.println("4. Consultar clientes");
        System.out.println("5. Cotizar alquiler");
        System.out.println("6. Alquilar vehículo");
        System.out.println("7. Registrar devolución");
        System.out.println("8. Registrar fin de mantenimiento");
        System.out.println("9. Reporte de flota");
        System.out.println("10. Reporte de ingresos y descuentos");
        System.out.println("11. Alquileres activos");
        System.out.println("12. Historial de un cliente");
        System.out.println("0. Salir");
    }

    public int leerOpcion() {
        return leerEntero("Seleccione una opción: ");
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public String leerTextoNoVacio(String mensaje) {
        String texto = leerTexto(mensaje);
        while (texto.isEmpty()) {
            mostrarError("Este dato no puede quedar vacío.");
            texto = leerTexto(mensaje);
        }
        return texto;
    }

    public int leerEntero(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero.");
            }
        }
    }

    public double leerDecimal(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número; use punto para los decimales, por ejemplo 1.5.");
            }
        }
    }

    public int leerEnteroPositivo(String mensaje) {
        int valor = leerEntero(mensaje);
        while (valor <= 0) {
            mostrarError("Debe ingresar un número entero mayor que 0.");
            valor = leerEntero(mensaje);
        }
        return valor;
    }

    public double leerDecimalPositivo(String mensaje) {
        double valor = leerDecimal(mensaje);
        while (valor <= 0) {
            mostrarError("Debe ingresar un número mayor que 0.");
            valor = leerDecimal(mensaje);
        }
        return valor;
    }

    public boolean leerBooleano(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje + " (s/n): ").toLowerCase();
            if (entrada.equals("s") || entrada.equals("si") || entrada.equals("sí")) {
                return true;
            }
            if (entrada.equals("n") || entrada.equals("no")) {
                return false;
            }
            mostrarError("Responda s o n.");
        }
    }

    public ArrayList<TipoLicencia> leerLicencias() {
        while (true) {
            String entrada = leerTexto("Licencias (A, B, C o M). Si son varias, sepárelas con coma, por ejemplo A, M: ").toUpperCase();
            ArrayList<TipoLicencia> licencias = new ArrayList<>();
            boolean valido = !entrada.isEmpty();
            for (String parte : entrada.split("[,\\s]+")) {
                if (parte.isEmpty()) {
                    continue;
                }
                try {
                    TipoLicencia licencia = TipoLicencia.valueOf(parte);
                    if (!licencias.contains(licencia)) {
                        licencias.add(licencia);
                    }
                } catch (IllegalArgumentException e) {
                    valido = false;
                }
            }
            if (valido && !licencias.isEmpty()) {
                return licencias;
            }
            mostrarError("Ingrese al menos una licencia válida (A, B, C o M).");
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarFlota(ArrayList<Vehiculo> flota) {
        System.out.println();
        System.out.println("--- Flota (" + flota.size() + " vehículos) ---");
        if (flota.isEmpty()) {
            System.out.println("No hay vehículos registrados.");
        }
        for (Vehiculo vehiculo : flota) {
            System.out.println(vehiculo.describir());
        }
    }

    public void mostrarClientes(ArrayList<Cliente> clientes) {
        System.out.println();
        System.out.println("--- Clientes (" + clientes.size() + ") ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente.describir());
        }
    }

    public void mostrarCotizacion(Cotizacion cotizacion) {
        System.out.println();
        System.out.println("--- Cotización ---");
        System.out.println("Vehículo: " + cotizacion.getVehiculo().describir());
        System.out.println("Cliente: " + cotizacion.getCliente().getNombre() + " (" + cotizacion.getCliente().getIdentificador() + ")");
        System.out.println("Días: " + cotizacion.getDias());
        System.out.println("Subtotal: Q" + formatearMonto(cotizacion.getSubtotal()));
        System.out.println("Descuento: Q" + formatearMonto(cotizacion.getDescuento()));
        System.out.println("Total: Q" + formatearMonto(cotizacion.getTotal()));
        if (cotizacion.esAlquilable()) {
            System.out.println("El cliente puede alquilar este vehículo ahora.");
        } else {
            System.out.println("El cliente NO puede alquilar este vehículo ahora:");
            for (String razon : cotizacion.getRazones()) {
                System.out.println("  - " + razon);
            }
        }
    }

    public void mostrarAlquiler(Alquiler alquiler) {
        System.out.println(alquiler.describir());
    }

    public void mostrarAlquileres(String titulo, ArrayList<Alquiler> lista) {
        System.out.println();
        System.out.println("--- " + titulo + " ---");
        if (lista.isEmpty()) {
            System.out.println("No hay alquileres para mostrar.");
        }
        for (Alquiler alquiler : lista) {
            mostrarAlquiler(alquiler);
        }
    }

    public void mostrarFilaFlota(String nombre, int total, int disponibles, int alquilados, int mantenimiento) {
        System.out.println(nombre + ": " + total + " | Disponibles: " + disponibles + " | Alquilados: " + alquilados + " | En mantenimiento: " + mantenimiento);
    }

    public void mostrarIngresoCategoria(String categoria, double ingreso) {
        System.out.println(categoria + ": Q" + formatearMonto(ingreso));
    }

    public void mostrarTotalesIngresos(double total, double descuentos) {
        System.out.println("Ingresos totales: Q" + formatearMonto(total));
        System.out.println("Total de descuentos otorgados: Q" + formatearMonto(descuentos));
    }

    public void mostrarHistorialCliente(String identificador, ArrayList<Alquiler> historial, double totalPagado) {
        mostrarAlquileres("Historial del cliente " + identificador, historial);
        System.out.println("Total pagado: Q" + formatearMonto(totalPagado));
    }

    private String formatearMonto(double monto) {
        return formatoMonto.format(monto);
    }
}
