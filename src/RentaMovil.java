import java.util.ArrayList;

public class RentaMovil {
    private ArrayList<Vehiculo> flota;
    private ArrayList<Cliente> clientes;
    private ArrayList<Alquiler> alquileres;
    private int siguienteNumeroAlquiler;

    public RentaMovil() {
        this.flota = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.alquileres = new ArrayList<>();
        this.siguienteNumeroAlquiler = 1;
    }

    public void cargarDatosIniciales() {
        registrarVehiculo(new Automovil("P123ABC", "Toyota", "Corolla", 300, 5, true));
        registrarVehiculo(new Automovil("P456DEF", "Nissan", "Versa", 250, 5, false));
        registrarVehiculo(new Motocicleta("M111AAA", "Honda", "CB190", 120, 190));
        Motocicleta motoCercaDeUmbral = new Motocicleta("M222BBB", "Yamaha", "MT-03", 180, 321);
        motoCercaDeUmbral.setDiasAcumulados(18);
        registrarVehiculo(motoCercaDeUmbral);
        registrarVehiculo(new CamionetaCarga("C333CCC", "Toyota", "Hilux", 200, 1.5));
        registrarVehiculo(new CamionetaCarga("C444DDD", "Isuzu", "NPR", 350, 3));
        registrarVehiculo(new Microbus("B555EEE", "Toyota", "Hiace", 450, 15, true));
        registrarVehiculo(new Microbus("B666FFF", "Nissan", "Urvan", 400, 12, false));

        ArrayList<TipoLicencia> licenciasAna = new ArrayList<>();
        licenciasAna.add(TipoLicencia.C);
        ClienteIndividual frecuente = new ClienteIndividual("1234567890101", "Ana López", licenciasAna);
        frecuente.setAlquileresConfirmados(3);
        registrarCliente(frecuente);

        ArrayList<TipoLicencia> licenciasCarlos = new ArrayList<>();
        licenciasCarlos.add(TipoLicencia.A);
        licenciasCarlos.add(TipoLicencia.M);
        registrarCliente(new ClienteIndividual("2345678901202", "Carlos Pérez", licenciasCarlos));

        ArrayList<TipoLicencia> licenciasLogistica = new ArrayList<>();
        licenciasLogistica.add(TipoLicencia.B);
        registrarCliente(new ClienteCorporativo("1234567-8", "Logística del Valle S.A.", "María García", licenciasLogistica));

        ArrayList<TipoLicencia> licenciasTurismo = new ArrayList<>();
        licenciasTurismo.add(TipoLicencia.C);
        registrarCliente(new ClienteCorporativo("9876543-2", "Turismo Quetzal S.A.", "Luis Méndez", licenciasTurismo));
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }
        if (buscarVehiculo(vehiculo.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa " + vehiculo.getPlaca() + ".");
        }
        flota.add(vehiculo);
    }

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (buscarCliente(cliente.getIdentificador()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el identificador " + cliente.getIdentificador() + ".");
        }
        clientes.add(cliente);
    }

    public Cotizacion cotizar(String placa, String identificador, int dias) {
        Vehiculo vehiculo = exigirVehiculo(placa);
        Cliente cliente = exigirCliente(identificador);
        return new Cotizacion(vehiculo, cliente, dias);
    }

    public Alquiler confirmarAlquiler(Cotizacion cotizacion) {
        if (cotizacion == null) {
            throw new IllegalArgumentException("No hay cotización para confirmar.");
        }
        if (!cotizacion.esAlquilable()) {
            String mensaje = "No se puede alquilar:";
            for (String razon : cotizacion.getRazones()) {
                mensaje = mensaje + " " + razon;
            }
            throw new IllegalStateException(mensaje);
        }
        Alquiler alquiler = new Alquiler(siguienteNumeroAlquiler, cotizacion);
        cotizacion.getVehiculo().marcarAlquilado();
        cotizacion.getCliente().registrarAlquiler(alquiler);
        alquileres.add(alquiler);
        siguienteNumeroAlquiler++;
        return alquiler;
    }

    public Alquiler registrarDevolucion(String placa) {
        Vehiculo vehiculo = exigirVehiculo(placa);
        if (vehiculo.getEstado() != EstadoVehiculo.ALQUILADO) {
            throw new IllegalStateException("Solo se puede devolver un vehículo alquilado. Estado actual: " + vehiculo.getEstado().getDescripcion() + ".");
        }
        Alquiler alquiler = buscarAlquilerActivo(vehiculo);
        vehiculo.registrarDevolucion(alquiler.getDias());
        alquiler.finalizar();
        return alquiler;
    }

    public void finalizarMantenimiento(String placa) {
        Vehiculo vehiculo = exigirVehiculo(placa);
        vehiculo.finalizarMantenimiento();
    }

    public EstadoVehiculo obtenerEstadoVehiculo(String placa) {
        return exigirVehiculo(placa).getEstado();
    }

    public ArrayList<String> revisarCondiciones(String placa, String identificador) {
        return Cotizacion.revisarCondiciones(exigirVehiculo(placa), exigirCliente(identificador));
    }

    public boolean existeVehiculo(String placa) {
        return buscarVehiculo(placa) != null;
    }

    public boolean existeCliente(String identificador) {
        return buscarCliente(identificador) != null;
    }

    public ArrayList<Vehiculo> obtenerFlota() {
        return new ArrayList<>(flota);
    }

    public ArrayList<Cliente> obtenerClientes() {
        return new ArrayList<>(clientes);
    }

    public ArrayList<String> obtenerCategorias() {
        ArrayList<String> categorias = new ArrayList<>();
        for (Vehiculo vehiculo : flota) {
            if (!categorias.contains(vehiculo.getCategoria())) {
                categorias.add(vehiculo.getCategoria());
            }
        }
        return categorias;
    }

    public int contarVehiculos(String categoria, EstadoVehiculo estado) {
        int cantidad = 0;
        for (Vehiculo vehiculo : flota) {
            boolean coincideCategoria = categoria == null || vehiculo.getCategoria().equals(categoria);
            boolean coincideEstado = estado == null || vehiculo.getEstado() == estado;
            if (coincideCategoria && coincideEstado) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public double calcularIngresosTotales() {
        double total = 0;
        for (Alquiler alquiler : alquileres) {
            total = total + alquiler.getTotal();
        }
        return total;
    }

    public double calcularIngresosCategoria(String categoria) {
        double total = 0;
        for (Alquiler alquiler : alquileres) {
            if (alquiler.getVehiculo().getCategoria().equals(categoria)) {
                total = total + alquiler.getTotal();
            }
        }
        return total;
    }

    public double calcularDescuentosTotales() {
        double total = 0;
        for (Alquiler alquiler : alquileres) {
            total = total + alquiler.getDescuento();
        }
        return total;
    }

    public ArrayList<Alquiler> obtenerAlquileresActivos() {
        ArrayList<Alquiler> activos = new ArrayList<>();
        for (Alquiler alquiler : alquileres) {
            if (alquiler.estaActivo()) {
                activos.add(alquiler);
            }
        }
        return activos;
    }

    public ArrayList<Alquiler> obtenerHistorialCliente(String identificador) {
        return exigirCliente(identificador).getHistorial();
    }

    public double calcularTotalPagadoCliente(String identificador) {
        return exigirCliente(identificador).calcularTotalPagado();
    }

    private Vehiculo buscarVehiculo(String placa) {
        if (placa == null) {
            return null;
        }
        for (Vehiculo vehiculo : flota) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return vehiculo;
            }
        }
        return null;
    }

    private Cliente buscarCliente(String identificador) {
        if (identificador == null) {
            return null;
        }
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificador().equalsIgnoreCase(identificador.trim())) {
                return cliente;
            }
        }
        return null;
    }

    private Vehiculo exigirVehiculo(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo con la placa " + placa + ".");
        }
        return vehiculo;
    }

    private Cliente exigirCliente(String identificador) {
        Cliente cliente = buscarCliente(identificador);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con el identificador " + identificador + ".");
        }
        return cliente;
    }

    private Alquiler buscarAlquilerActivo(Vehiculo vehiculo) {
        for (Alquiler alquiler : alquileres) {
            if (alquiler.estaActivo() && alquiler.getVehiculo() == vehiculo) {
                return alquiler;
            }
        }
        throw new IllegalStateException("El vehículo " + vehiculo.getPlaca() + " no tiene un alquiler activo.");
    }
}
