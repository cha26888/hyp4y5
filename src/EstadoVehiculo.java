public enum EstadoVehiculo {
    DISPONIBLE("Disponible"),
    ALQUILADO("Alquilado"),
    EN_MANTENIMIENTO("En mantenimiento");

    private final String descripcion;

    private EstadoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
