public class Microbus extends Vehiculo {
    private static final double RECARGO_PILOTO_POR_DIA = 250;
    private static final int UMBRAL_MANTENIMIENTO = 25;

    private int cantidadPasajeros;
    private boolean incluyePiloto;

    public Microbus(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean incluyePiloto) {
        super(placa, marca, modelo, tarifaDiaria);
        if (cantidadPasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que 0.");
        }
        this.cantidadPasajeros = cantidadPasajeros;
        this.incluyePiloto = incluyePiloto;
    }

    @Override
    public double calcularRecargo(int dias) {
        if (incluyePiloto) {
            return RECARGO_PILOTO_POR_DIA * dias;
        }
        return 0;
    }

    @Override
    public TipoLicencia licenciaRequerida() {
        if (incluyePiloto) {
            return null;
        }
        return TipoLicencia.B;
    }

    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    @Override
    public String getCategoria() {
        return "Microbús";
    }

    @Override
    public String describirCaracteristicas() {
        return "Pasajeros: " + cantidadPasajeros + ", " + (incluyePiloto ? "incluye piloto" : "sin piloto");
    }

    public int getCantidadPasajeros() {
        return cantidadPasajeros;
    }

    public boolean isIncluyePiloto() {
        return incluyePiloto;
    }
}
