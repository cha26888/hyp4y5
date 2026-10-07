public abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private double tarifaDiaria;
    private EstadoVehiculo estado;
    private int diasAcumulados;

    protected Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía.");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede estar vacía.");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío.");
        }
        if (tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que 0.");
        }
        this.placa = placa.trim().toUpperCase();
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria;
        this.estado = EstadoVehiculo.DISPONIBLE;
        this.diasAcumulados = 0;
    }

    public abstract double calcularRecargo(int dias);

    public abstract TipoLicencia licenciaRequerida();

    public abstract int getUmbralMantenimiento();

    public abstract String getCategoria();

    public abstract String describirCaracteristicas();

    public double calcularSubtotal(int dias) {
        return tarifaDiaria * dias + calcularRecargo(dias);
    }

    public String describir() {
        TipoLicencia licencia = licenciaRequerida();
        String textoLicencia = licencia == null ? "ninguna" : licencia.name();
        return "[" + getCategoria() + "] " + placa + " | " + marca + " " + modelo
                + " | Tarifa: Q" + String.format("%,.2f", tarifaDiaria) + "/día"
                + " | Estado: " + estado.getDescripcion()
                + " | Días acumulados: " + diasAcumulados + "/" + getUmbralMantenimiento()
                + " | Licencia requerida: " + textoLicencia
                + " | " + describirCaracteristicas();
    }

    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    public void marcarAlquilado() {
        if (estado != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalStateException("El vehículo " + placa + " no está disponible (" + estado.getDescripcion() + ").");
        }
        estado = EstadoVehiculo.ALQUILADO;
    }

    public void registrarDevolucion(int dias) {
        if (estado != EstadoVehiculo.ALQUILADO) {
            throw new IllegalStateException("El vehículo " + placa + " no está alquilado.");
        }
        diasAcumulados = diasAcumulados + dias;
        if (diasAcumulados >= getUmbralMantenimiento()) {
            estado = EstadoVehiculo.EN_MANTENIMIENTO;
        } else {
            estado = EstadoVehiculo.DISPONIBLE;
        }
    }

    public void finalizarMantenimiento() {
        if (estado != EstadoVehiculo.EN_MANTENIMIENTO) {
            throw new IllegalStateException("El vehículo " + placa + " no está en mantenimiento.");
        }
        estado = EstadoVehiculo.DISPONIBLE;
        diasAcumulados = 0;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public int getDiasAcumulados() {
        return diasAcumulados;
    }

    public void setDiasAcumulados(int dias) {
        if (dias < 0) {
            throw new IllegalArgumentException("Los días acumulados no pueden ser negativos.");
        }
        this.diasAcumulados = dias;
    }
}
