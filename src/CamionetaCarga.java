public class CamionetaCarga extends Vehiculo {
    private static final double RECARGO_POR_TONELADA_DIA = 100;
    private static final int UMBRAL_MANTENIMIENTO = 15;

    private double capacidadToneladas;

    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria, double capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria);
        if (capacidadToneladas <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que 0.");
        }
        this.capacidadToneladas = capacidadToneladas;
    }

    @Override
    public double calcularRecargo(int dias) {
        return RECARGO_POR_TONELADA_DIA * capacidadToneladas * dias;
    }

    @Override
    public TipoLicencia licenciaRequerida() {
        return TipoLicencia.B;
    }

    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    @Override
    public String getCategoria() {
        return "Camioneta de carga";
    }

    @Override
    public String describirCaracteristicas() {
        return "Capacidad máxima: " + capacidadToneladas + " t";
    }

    public double getCapacidadToneladas() {
        return capacidadToneladas;
    }
}
