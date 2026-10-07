public class Alquiler {
    private int numero;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private int dias;
    private double subtotal;
    private double descuento;
    private double total;
    private boolean activo;

    public Alquiler(int numero, Cotizacion cotizacion) {
        this.numero = numero;
        this.cliente = cotizacion.getCliente();
        this.vehiculo = cotizacion.getVehiculo();
        this.dias = cotizacion.getDias();
        this.subtotal = cotizacion.getSubtotal();
        this.descuento = cotizacion.getDescuento();
        this.total = cotizacion.getTotal();
        this.activo = true;
    }

    public void finalizar() {
        if (!activo) {
            throw new IllegalStateException("El alquiler #" + numero + " ya está finalizado.");
        }
        activo = false;
    }

    public boolean estaActivo() {
        return activo;
    }

    public String describir() {
        return "Alquiler #" + numero
                + " | Cliente: " + cliente.getNombre() + " (" + cliente.getIdentificador() + ")"
                + " | Vehículo: " + vehiculo.getCategoria() + " " + vehiculo.getPlaca()
                + " | Días: " + dias
                + " | Subtotal: Q" + String.format("%,.2f", subtotal)
                + " | Descuento: Q" + String.format("%,.2f", descuento)
                + " | Total: Q" + String.format("%,.2f", total)
                + " | " + (activo ? "Activo" : "Finalizado");
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
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
}
