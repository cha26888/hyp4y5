public class Motocicleta extends Vehiculo {
    private static final int LIMITE_CILINDRAJE = 250;
    private static final double RECARGO_ALTA_CILINDRADA = 75;
    private static final int UMBRAL_MANTENIMIENTO = 20;

    private int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);
        if (cilindraje <= 0) {
            throw new IllegalArgumentException("El cilindraje debe ser mayor que 0.");
        }
        this.cilindraje = cilindraje;
    }

    @Override
    public double calcularRecargo(int dias) {
        if (cilindraje > LIMITE_CILINDRAJE) {
            return RECARGO_ALTA_CILINDRADA;
        }
        return 0;
    }

    @Override
    public TipoLicencia licenciaRequerida() {
        return TipoLicencia.M;
    }

    @Override
    public int getUmbralMantenimiento() {
        return UMBRAL_MANTENIMIENTO;
    }

    @Override
    public String getCategoria() {
        return "Motocicleta";
    }

    @Override
    public String describirCaracteristicas() {
        return "Cilindraje: " + cilindraje + " cc";
    }

    public int getCilindraje() {
        return cilindraje;
    }
}
