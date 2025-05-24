package Menus;

import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorViajes.Beneficios;
import GestorViajes.ServicioViajes;
import GestorViajes.Viaje;
import java.util.HashMap;
import java.util.Map;

public class ServicioUsuario {

    private final Map<Usuario, Viaje> viajesActivos = new HashMap<>();
    private final GestorVehiculos gestorVehiculos = GestorVehiculos.getInstancia();
    private final Mapa mapa = Mapa.getInstancia();

    public void iniciarViajeBase(Usuario usuario, VehiculoEnum tipoVehiculo, String nombreBase) {
        ServicioViajes servicioViajes = ServicioViajes.getInstancia();
        Viaje nuevoViaje = servicioViajes.iniciarViaje(usuario, tipoVehiculo, nombreBase);
        if (nuevoViaje != null) {
            viajesActivos.put(usuario, nuevoViaje);
        }
    }

    public void iniciarViajeCoordenadas(Usuario usuario, VehiculoEnum tipoVehiculo, int coordX, int coordY) {
        ServicioViajes servicioViajes = ServicioViajes.getInstancia();
        Viaje nuevoViaje = servicioViajes.iniciarViajeMoto(usuario, tipoVehiculo, coordX, coordY);
        if (nuevoViaje != null) {
            viajesActivos.put(usuario, nuevoViaje);
        }
    }

    public boolean finalizarViajeBase(Usuario usuario, String nombreBase) {
        Viaje viajeActivo = viajesActivos.get(usuario);
        if (viajeActivo == null) return false;
        boolean terminado = ServicioViajes.getInstancia().finalizarViajeBase(usuario, viajeActivo, nombreBase);
        if (terminado) viajesActivos.remove(usuario);
        return terminado;
    }

    public boolean finalizarViajeCoordenadas(Usuario usuario, int coordX, int coordY) {
        Viaje viajeActivo = viajesActivos.get(usuario);
        if (viajeActivo == null) return false;
        boolean terminado = ServicioViajes.getInstancia().finalizarViajeCoordenadas(usuario, viajeActivo, coordX, coordY);
        if (terminado) viajesActivos.remove(usuario);
        return terminado;
    }

    public Viaje getViajeActivo(Usuario usuario) {
        return viajesActivos.get(usuario);
    }

    public void mostrarConsultaVehiculosDisponibles(Usuario usuario) {
        System.out.println("\n--- Consulta de vehículos disponibles ---");
        System.out.printf("%-12s %-10s %-15s %-15s %-10s\n", "Tipo", "ID", "Base/Coord", "Ocupación", "Batería");

        // Mostrar bicicletas y patinetes en bases
        for (Base base : mapa.getListaBases()) {
            int ocupacion = 0;
            for (Vehiculo v : base.getListaVehiculos()) {
                if (v.getReservado()) continue;
                double bateria = v.getBateria();
                boolean mostrar = false;
                if (bateria > 20) {
                    mostrar = true;
                } else if (bateria > 10 && bateria <= 20 && usuario instanceof GestorPersonas.Personas.Premium) {
                    mostrar = true;
                }
                if (bateria > 10 && mostrar) {
                    ocupacion++;
                    System.out.printf("%-12s %-10d %-15s %-15d %-10.1f%%\n",
                        v.getVehiculoEnum(), v.getID(), base.getNombre(), ocupacion, bateria);
                }
            }
        }

        // Mostrar motos por coordenadas
        for (Vehiculo v : gestorVehiculos.getVehiculosEnMapa()) {
            if (v.getReservado()) continue;
            double bateria = v.getBateria();
            boolean mostrar = false;
            if (bateria > 20) {
                mostrar = true;
            } else if (bateria > 10 && bateria <= 20 && usuario instanceof GestorPersonas.Personas.Premium) {
                mostrar = true;
            }
            if (bateria > 10 && mostrar) {
                int x = ((GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas) v).getCoordenadaX();
                int y = ((GestorVehiculosBases.Vehiculos.VehiculoDeCoordenadas) v).getCoordenadaY();
                System.out.printf("%-12s %-10d (%d,%d) %-15s %-10.1f%%\n",
                    v.getVehiculoEnum(), v.getID(), x, y, "-", bateria);
            }
        }
    }

    public void mostrarHistorialViajes(Usuario usuario) {
        System.out.println("\n--- Historial de viajes realizados ---");
        var historial = usuario.getHistorialViajes();
        if (historial.isEmpty()) {
            System.out.println("No tienes viajes realizados.");
            return;
        }
        System.out.printf("%-12s %-10s %-25s %-25s %-10s\n", "Tipo", "ID", "Inicio", "Fin", "Importe (€)");
        for (Viaje viaje : historial) {
            String inicio = new java.util.Date(viaje.getTiempoInicio()).toString();
            String fin = viaje.getTiempoFin() > 0 ? new java.util.Date(viaje.getTiempoFin()).toString() : "En curso";
            double importe = viaje.getCosto();
            System.out.printf("%-12s %-10d %-25s %-25s %-10.2f\n",
                viaje.getVehiculo().getVehiculoEnum(),
                viaje.getVehiculo().getID(),
                inicio,
                fin,
                importe
            );
        }
    }

    public void mostrarSaldo(Usuario usuario) {
        System.out.printf("\nSaldo disponible: %.2f €\n", usuario.getSaldo());
    }

    public void recargarSaldo(Usuario usuario, double cantidad) {
        usuario.agregarSaldo(cantidad);
        System.out.printf("Recarga exitosa. Nuevo saldo: %.2f €\n", usuario.getSaldo());
    }

    public boolean reservarVehiculo(Usuario usuario, int idVehiculo) {
        Beneficios beneficios = new Beneficios();
        if (!beneficios.getReservasPermitidas()) {
            System.out.println("Las reservas están deshabilitadas actualmente.");
            return false;
        }

        Vehiculo vehiculo = gestorVehiculos.getVehiculoPorID(idVehiculo);
        if (vehiculo == null) {
            System.out.println("No existe un vehículo con ese ID.");
            return false;
        }
        if (vehiculo.getReservado()) {
            System.out.println("Este vehículo ya está reservado.");
            return false;
        }
        if (vehiculo.getEnUso()) {
            System.out.println("Este vehículo está actualmente en uso.");
            return false;
        }
        if (vehiculo.getAverias()) {
            System.out.println("Este vehículo está averiado y no se puede reservar.");
            return false;
        }
        if (vehiculo.getBateria() <= vehiculo.getBateriaMinima(usuario)) {
            System.out.println("Este vehículo no cumple el nivel mínimo de batería para tu tipo de usuario.");
            return false;
        }

        boolean exito = beneficios.reservarVehiculo(idVehiculo);
        if (exito) {
            vehiculo.setReservado(true);
            System.out.println("Vehículo reservado correctamente. Tienes 20 minutos para iniciar el viaje.");
            return true;
        } else {
            System.out.println("No se pudo reservar el vehículo. Puede que ya esté reservado por otro usuario.");
            return false;
        }
    }

    public Vehiculo getVehiculoMasCercano(Usuario usuario, VehiculoEnum tipoVehiculo, int coordX, int coordY) {
        return gestorVehiculos.getVehiculoMasCercano(coordX, coordY, tipoVehiculo, usuario);
    }

    public Mapa getMapa() {
        return mapa;
    }

    public GestorVehiculos getGestorVehiculos() {
        return gestorVehiculos;
    }
}