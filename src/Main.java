public class Main {
    public static void main(String[] args) {
        RentaMovil empresa = new RentaMovil();
        VistaRentaMovil vista = new VistaRentaMovil();
        ControladorRentaMovil controlador = new ControladorRentaMovil(empresa, vista);
        empresa.cargarDatosIniciales();
        controlador.iniciar();
    }
}
