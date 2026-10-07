import java.util.ArrayList;

public class Cotizacion {
    private final Vehiculo vehiculo;
    private final Cliente cliente;
    private final int dias;
    private final double subtotal;
    private final double descuento;
    private final double total;
    private final ArrayList<String> razones;

    public Cotizacion(Vehiculo vehiculo, Cliente cliente, int dias) {
        if (vehiculo == null || cliente == null) {
            throw new IllegalArgumentException("El vehículo y el cliente son obligatorios.");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días deben ser un número entero mayor que 0.");
        }
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.dias = dias;
        this.subtotal = redondear(vehiculo.calcularSubtotal(dias));
        this.descuento = redondear(cliente.calcularDescuento(subtotal));
        this.total = redondear(subtotal - descuento);
        this.razones = revisarCondiciones(vehiculo, cliente);
    }

    public boolean esAlquilable() {
        return razones.isEmpty();
    }

    public static ArrayList<String> revisarCondiciones(Vehiculo vehiculo, Cliente cliente) {
        ArrayList<String> razones = new ArrayList<>();
        if (!vehiculo.estaDisponible()) {
            razones.add("El vehículo no está disponible (" + vehiculo.getEstado().getDescripcion() + ").");
        }
        if (!cliente.puedeConducir(vehiculo)) {
            razones.add("El cliente no tiene una licencia que autorice la " + vehiculo.licenciaRequerida().name() + ".");
        }
        if (!cliente.puedeAlquilarMas()) {
            razones.add("El cliente llegó a su límite de " + cliente.getLimiteAlquileresActivos() + " alquiler(es) activo(s).");
        }
        return razones;
    }

    private double redondear(double monto) {
        return Math.round(monto * 100.0) / 100.0;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public int getDias() {
        return dias;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<String> getRazones() {
        return new ArrayList<>(razones);
    }
}
