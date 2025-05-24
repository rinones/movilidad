package GestorViajes;

import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorViajes {

    //==========================
    // SINGLETON Y DEPENDENCIAS
    //==========================
    private static GestorViajes instancia;
    private GestorVehiculos gestorVehiculos;
    private Mapa mapa;

    //==========================
    // ESTRUCTURAS DE DATOS
    //==========================
    private final List<Viaje> viajes;
    private final Map<Usuario, List<Viaje>> viajesPorUsuario;

    //==========================
    // CONSTRUCTOR
    //==========================
    private GestorViajes() {
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.mapa = Mapa.getInstancia();
        this.viajesPorUsuario = new HashMap<>();
        this.viajes = new ArrayList<>();
    }

    // Devuelve la instancia singleton
    public static GestorViajes getInstancia() {
        if (instancia == null) {
            instancia = new GestorViajes();
        }
        return instancia;
    }

    //==========================
    // ACCESO A DATOS
    //==========================

    // Devuelve la lista de todos los viajes
    public List<Viaje> getListaTodosViajes() {
        return viajes;
    }

    // Devuelve el mapa de viajes por usuario
    public Map<Usuario, List<Viaje>> getMapaViajesPorUsuario() {
        return viajesPorUsuario;
    }

    // Devuelve los vehículos actualmente en uso
    public List<Vehiculo> obtenerVehiculosEnUsoActual() {
        List<Vehiculo> vehiculosEnUso = new ArrayList<>();
        for (Viaje viaje : viajes) {
            if (viaje.estaActivo()) {
                vehiculosEnUso.add(viaje.getVehiculo());
            }
        }
        return vehiculosEnUso;
    }

    // Devuelve detalles del viaje activo de un vehículo por ID
    public String mostrarDetallesViajeActual(int idVehiculo) {
        for (Viaje viaje : viajes) {
            if (viaje.getVehiculo().getID() == idVehiculo && viaje.estaActivo()) {
                return String.format("%-5d %-15s %-20s %s", 
                    viaje.getVehiculo().getID(),
                    viaje.getVehiculo().getVehiculoEnum(),
                    viaje.getUsuario().getNombre() + " " + viaje.getUsuario().getApellidos(),
                    new java.util.Date(viaje.getTiempoInicio())
                );
            }
        }
        return "No hay datos del viaje para el vehículo ID: " + idVehiculo;
    }

    //==========================
    // GESTIÓN DE VIAJES
    //==========================

    // Registra un nuevo viaje
    public boolean registrarViaje(Viaje viaje) {
        if (viaje == null) {
            System.out.println("Error: No se puede registrar un viaje nulo.");
            return false;
        }
        viajes.add(viaje);
        Usuario usuario = viaje.getUsuario();
        viajesPorUsuario.computeIfAbsent(usuario, k -> new ArrayList<>()).add(viaje);
        return true;
    }

    //==========================
    // VISUALIZACIÓN DE VIAJES
    //==========================

    // Muestra todos los viajes registrados
    public void mostrarViajes() {
        if (viajes.isEmpty()) {
            System.out.println("No hay viajes registrados.");
            return;
        }
        System.out.println("\n=== LISTADO DE VIAJES ===");
        System.out.println("Vehículo - Usuario - Inicio - Fin - Costo - Activo");
        for (Viaje viaje : viajes) {
            System.out.println(viaje.getVehiculo().getNombreVehiculo() + " - " + 
                              viaje.getUsuario() + " - " + 
                              new java.util.Date(viaje.getTiempoInicio()) + " - " + 
                              (viaje.getTiempoFin() > 0 ? new java.util.Date(viaje.getTiempoFin()) : "En curso") + " - " + 
                              String.format("%.2f", viaje.getCosto()) + "€ - " +
                              (viaje.estaActivo() ? "Activo" : "Finalizado"));
        }
    }

    //==========================
    // VISUALIZACIÓN DE TARIFAS Y DESCUENTOS
    //==========================

    // Muestra las tarifas actuales por tipo de vehículo
    public void mostrarTarifasActuales() {
        System.out.println("\n=== TARIFAS ACTUALES ===");
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            double tarifa = tipo.getTarifa();
            System.out.printf("%s: %.2f€ por hora\n", tipo, tarifa);
        }
    }

    // Muestra los descuentos actuales por tipo de vehículo
    public void mostrarDescuentosActuales() {
        System.out.println("\n=== DESCUENTOS ACTUALES ===");
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            double descuento = tipo.getDescuento();
            System.out.printf("%s: %.1f%%\n", tipo, descuento);
        }
    }

    //==========================
    // VISUALIZACIÓN DE RESERVAS
    //==========================

    // Muestra el estado actual de las reservas
    public void mostrarEstadoReservas() {
        System.out.println("\n=== ESTADO DE RESERVAS ===");
        Beneficios beneficios = new Beneficios();
        String estado;
        if (beneficios.getReservasPermitidas()) {
            estado = "HABILITADAS";
        } else {
            estado = "DESHABILITADAS";
        }
        System.out.println("Las reservas están actualmente: " + estado);
    }
}
