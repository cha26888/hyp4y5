import java.util.ArrayList;

public class ClienteCorporativo extends Cliente {
    private static final double DESCUENTO_CORPORATIVO = 0.10;
    private static final int LIMITE_ACTIVOS = 3;

    private String nombreContacto;

    public ClienteCorporativo(String nit, String nombreEmpresa, String nombreContacto, ArrayList<TipoLicencia> licencias) {
        super(nit, nombreEmpresa, licencias);
        if (nombreContacto == null || nombreContacto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del contacto no puede estar vacío.");
        }
        this.nombreContacto = nombreContacto.trim();
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * DESCUENTO_CORPORATIVO;
    }

    @Override
    public int getLimiteAlquileresActivos() {
        return LIMITE_ACTIVOS;
    }

    @Override
    public String describirDatosPropios() {
        return "[Corporativo] NIT: " + getIdentificador() + " | Contacto: " + nombreContacto;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }
}
