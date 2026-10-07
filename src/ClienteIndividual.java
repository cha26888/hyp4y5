import java.util.ArrayList;

public class ClienteIndividual extends Cliente {
    private static final int DIGITOS_DPI = 13;
    private static final int MINIMO_PARA_DESCUENTO = 3;
    private static final double DESCUENTO_FRECUENTE = 0.05;
    private static final int LIMITE_ACTIVOS = 1;

    public ClienteIndividual(String dpi, String nombre, ArrayList<TipoLicencia> licencias) {
        super(dpi, nombre, licencias);
        if (!esDpiValido(getIdentificador())) {
            throw new IllegalArgumentException("El DPI debe tener exactamente " + DIGITOS_DPI + " dígitos.");
        }
    }

    public static boolean esDpiValido(String dpi) {
        return dpi != null && dpi.trim().matches("\\d{" + DIGITOS_DPI + "}");
    }

    @Override
    public double calcularDescuento(double subtotal) {
        if (getAlquileresConfirmados() >= MINIMO_PARA_DESCUENTO) {
            return subtotal * DESCUENTO_FRECUENTE;
        }
        return 0;
    }

    @Override
    public int getLimiteAlquileresActivos() {
        return LIMITE_ACTIVOS;
    }

    @Override
    public String describirDatosPropios() {
        return "[Individual] DPI: " + getIdentificador();
    }
}
