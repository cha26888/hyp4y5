public class Automovil extends Vehiculo {
    private static final double RECARGO_AUTOMATICO_POR_DIA = 50;
    private static final int UMBRAL_MANTENIMIENTO = 30;

    private int cantidadPasajeros;
    private boolean transmisionAutomatica;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean transmisionAutomatica) {
        super(placa, marca, modelo, tarifaDiaria);
        if (cantidadPasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que 0.");
        }
        this.cantidadPasajeros = cantidadPasajeros;
        this.transmisionAutomatica = transmisionAutomatica;
    }

    @Override
    public double calcularRecargo(int dias) {
        if (transmisionAutomatica) {
            return RECARGO_AUTOMATICO_POR_DIA * dias;
        }
        return 0;
    }

    @Override
    public TipoLicencia licenciaRequerida() {
        return TipoLicencia.C;
    }

    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    @Override
    public String getCategoria() {
        return "Automóvil";
    }

    @Override
    public String describirCaracteristicas() {
        return "Pasajeros: " + cantidadPasajeros + ", transmisión " + (transmisionAutomatica ? "automática" : "manual");
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public boolean isTransmisionAutomatica() {
        return transmisionAutomatica;
    }
}
