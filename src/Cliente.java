import java.util.ArrayList;

public abstract class Cliente {
    private String identificador;
    private String nombre;
    private ArrayList<TipoLicencia> licencias;
    private ArrayList<Alquiler> historial;
    private int alquileresConfirmados;

    protected Cliente(String identificador, String nombre, ArrayList<TipoLicencia> licencias) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no puede estar vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (licencias == null || licencias.isEmpty()) {
            throw new IllegalArgumentException("El cliente debe tener al menos una licencia válida.");
        }
        this.identificador = identificador.trim();
        this.nombre = nombre.trim();
        this.licencias = new ArrayList<>();
        for (TipoLicencia licencia : licencias) {
            if (licencia != null && !this.licencias.contains(licencia)) {
                this.licencias.add(licencia);
            }
        }
        if (this.licencias.isEmpty()) {
            throw new IllegalArgumentException("El cliente debe tener al menos una licencia válida.");
        }
        this.historial = new ArrayList<>();
        this.alquileresConfirmados = 0;
    }

    public abstract double calcularDescuento(double subtotal);

    public abstract int getLimiteAlquileresActivos();

    public abstract String describirDatosPropios();

    public String describir() {
        String textoLicencias = "";
        for (TipoLicencia licencia : licencias) {
            if (!textoLicencias.isEmpty()) {
                textoLicencias = textoLicencias + ", ";
            }
            textoLicencias = textoLicencias + licencia.name();
        }
        return describirDatosPropios() + " | " + nombre
                + " | Licencias: " + textoLicencias
                + " | Alquileres activos: " + contarAlquileresActivos() + "/" + getLimiteAlquileresActivos()
                + " | Alquileres confirmados: " + alquileresConfirmados;
    }

    public int contarAlquileresActivos() {
        int activos = 0;
        for (Alquiler alquiler : historial) {
            if (alquiler.estaActivo()) {
                activos++;
            }
        }
        return activos;
    }

    public boolean puedeAlquilarMas() {
        return contarAlquileresActivos() < getLimiteAlquileresActivos();
    }

    public boolean puedeConducir(Vehiculo vehiculo) {
        TipoLicencia requerida = vehiculo.licenciaRequerida();
        if (requerida == null) {
            return true;
        }
        for (TipoLicencia licencia : licencias) {
            if (licencia.autoriza(requerida)) {
                return true;
            }
        }
        return false;
    }

    public void registrarAlquiler(Alquiler alquiler) {
        historial.add(alquiler);
        alquileresConfirmados++;
    }

    public double calcularTotalPagado() {
        double total = 0;
        for (Alquiler alquiler : historial) {
            total = total + alquiler.getTotal();
        }
        return total;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<TipoLicencia> getLicencias() {
        return new ArrayList<>(licencias);
    }

    public ArrayList<Alquiler> getHistorial() {
        return new ArrayList<>(historial);
    }

    public int getAlquileresConfirmados() {
        return alquileresConfirmados;
    }

    public void setAlquileresConfirmados(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad de alquileres no puede ser negativa.");
        }
        this.alquileresConfirmados = cantidad;
    }
}
