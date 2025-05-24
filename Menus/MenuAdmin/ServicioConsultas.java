package Menus.MenuAdmin;

import Enum.VehiculoEnum;
import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Mantenimiento;
import GestorPersonas.Personas.Mecanico;
import GestorPersonas.Personas.Persona;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Facturas.FacturaReparacionVehiculo;
import GestorVehiculosBases.GestorBases;
import GestorVehiculosBases.GestorVehiculos;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorViajes.GestorViajes;
import GestorViajes.Viaje;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicioConsultas {

    // ===========================
    // ATRIBUTOS Y CONSTRUCTOR
    // ===========================

    // Instancias de gestores principales
    private final GestorPersonas gestorPersonas;
    private final GestorVehiculos gestorVehiculos;
    private final GestorViajes gestorViajes;
    private final GestorBases gestorBases;
    private final Mapa mapa;

    // Constructor: inicializa los gestores
    public ServicioConsultas() {
        this.gestorPersonas = GestorPersonas.getInstancia();
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.gestorViajes = GestorViajes.getInstancia();
        this.gestorBases = GestorBases.getInstancia();
        this.mapa = Mapa.getInstancia();
    }

    // ===========================
    // CONSULTAS BÁSICAS SOBRE VEHÍCULOS
    // ===========================

    // Devuelve la lista de todos los vehículos
    public List<Vehiculo> obtenerVehiculos() {
        // Llama al gestor de vehículos para obtener la lista
        return gestorVehiculos.getListaVehiculos();
    }

    // Devuelve los vehículos que tienen averías
    public List<Vehiculo> obtenerVehiculosConAverias() {
        List<Vehiculo> resultado = new ArrayList<>();
        // Recorre todos los vehículos y añade los que tienen avería
        for (Vehiculo v : gestorVehiculos.getListaVehiculos()) {
            if (v.getAverias()) resultado.add(v); // Si tiene avería, añadir a la lista
        }
        return resultado;
    }

    // ===========================
    // CONSULTAS SOBRE BASES
    // ===========================

    // Devuelve la lista de todas las bases
    public List<Base> obtenerBases() {
        // Llama al mapa para obtener la lista de bases
        return mapa.getListaBases();
    }

    // Devuelve las bases ordenadas por viajes iniciados
    public List<Base> obtenerBasesOrdenadasPorViajesIniciados() {
        List<Base> bases = new ArrayList<>(mapa.getListaBases());
        // Ordena las bases por viajes iniciados (descendente)
        bases.sort((b1, b2) -> Integer.compare(b2.getViajesIniciadosEnBase(), b1.getViajesIniciadosEnBase()));
        return bases;
    }

    // Devuelve las bases ordenadas por viajes finalizados
    public List<Base> obtenerBasesOrdenadasPorViajesFinalizados() {
        List<Base> bases = new ArrayList<>(mapa.getListaBases());
        // Ordena las bases por viajes finalizados (descendente)
        bases.sort((b1, b2) -> Integer.compare(b2.getViajesFinalizadosEnBase(), b1.getViajesFinalizadosEnBase()));
        return bases;
    }

    // ===========================
    // CONSULTAS SOBRE PERSONAS Y USUARIOS
    // ===========================

    // Devuelve la lista de personas ordenadas por DNI
    public List<Persona> obtenerPersonasOrdenadasPorDNI() {
        List<Persona> lista = new ArrayList<>(gestorPersonas.getPersonas());
        // Ordena la lista por DNI
        lista.sort(Comparator.comparing(Persona::getDNI));
        return lista;
    }

    // Devuelve la lista de usuarios
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        // Añade solo las personas que son usuarios
        for (var p : gestorPersonas.getPersonas()) {
            if (p instanceof Usuario u) usuarios.add(u); // Solo añade si es Usuario
        }
        return usuarios;
    }

    // Devuelve el historial de viajes de un usuario
    public List<Viaje> obtenerViajesUsuario(Usuario usuario) {
        // Devuelve la lista de viajes del usuario
        return usuario.getHistorialViajes();
    }

    // ===========================
    // CONSULTAS SOBRE VIAJES
    // ===========================

    // Devuelve los vehículos que están en uso actualmente
    public List<Vehiculo> obtenerVehiculosEnUsoActual() {
        // Llama al gestor de viajes para obtener los vehículos en uso
        return gestorViajes.obtenerVehiculosEnUsoActual();
    }

    // Devuelve los viajes realizados en un periodo de tiempo
    public List<Viaje> obtenerViajesEnPeriodo(long inicio, long fin) {
        List<Viaje> resultado = new ArrayList<>();
        // Recorre todos los viajes y añade los que están en el periodo
        for (Viaje v : gestorViajes.getListaTodosViajes()) {
            long tInicio = v.getTiempoInicio();
            long tFin = (v.getTiempoFin() == 0) ? System.currentTimeMillis() : v.getTiempoFin();
            if (tFin >= inicio && tInicio <= fin) resultado.add(v); // Si el viaje está en el periodo, añadir
        }
        return resultado;
    }

    // Devuelve la lista de todos los viajes
    public List<Viaje> obtenerTodosViajes() {
        // Llama al gestor de viajes para obtener la lista completa
        return gestorViajes.getListaTodosViajes();
    }

    // ===========================
    // CONSULTAS AVANZADAS Y RANKINGS
    // ===========================

    // Devuelve un mapa con las reparaciones por vehículo en un periodo
    public Map<Integer, List<FacturaReparacionVehiculo>> obtenerReparacionesPorVehiculoEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        var personas = gestorPersonas.getPersonas();
        List<Mecanico> mecanicos = new ArrayList<>();
        // Filtra solo los mecánicos
        for (var p : personas) {
            if (p instanceof Mecanico m) {
                mecanicos.add(m); // Añade solo mecánicos
            }
        }
        Map<Integer, List<FacturaReparacionVehiculo>> reparacionesPorVehiculo = new HashMap<>();
        // Recorre las reparaciones de cada mecánico
        for (Mecanico mecanico : mecanicos) {
            for (FacturaReparacionVehiculo factura : mecanico.getReparacionesVehiculosRealizadas()) {
                LocalDate fecha = factura.getFecha();
                // Comprueba si la fecha está en el rango
                if ((fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) &&
                    (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin))) {
                    int idVehiculo = factura.getVehiculo().getID();
                    reparacionesPorVehiculo.computeIfAbsent(idVehiculo, k -> new ArrayList<>()).add(factura);
                }
            }
        }
        return reparacionesPorVehiculo;
    }

    // Devuelve el ranking de intervenciones de mecánicos y mantenimiento
    public List<Object[]> obtenerRankingIntervenciones() {
        var personas = gestorPersonas.getPersonas();
        List<Object[]> ranking = new ArrayList<>();
        // Añade mecánicos y mantenimiento al ranking
        for (var p : personas) {
            if (p instanceof Mecanico m) {
                // Añade mecánico al ranking
                ranking.add(new Object[]{
                    m.getNombre() + " " + m.getApellidos(),
                    "Mecánico",
                    m.getIntervencionesRealizadas()
                });
            } else if (p instanceof Mantenimiento mt) {
                // Añade mantenimiento al ranking
                ranking.add(new Object[]{
                    mt.getNombre() + " " + mt.getApellidos(),
                    "Mantenimiento",
                    mt.getIntervencionesRealizadas()
                });
            }
        }
        // Ordena por intervenciones realizadas (descendente)
        ranking.sort((a, b) -> Integer.compare((int) b[2], (int) a[2]));
        return ranking;
    }

    // Devuelve un mapa de vehículos agrupados por tipo y ordenados por tiempo de uso
    public Map<VehiculoEnum, List<Object[]>> obtenerVehiculosPorTiempoUso() {
        List<Vehiculo> vehiculos = obtenerVehiculos();
        List<Viaje> viajes = obtenerTodosViajes();
        Map<Integer, Long> tiempoUsoPorVehiculo = new HashMap<>();
        // Inicializa el tiempo de uso en 0 para cada vehículo
        for (Vehiculo v : vehiculos) {
            tiempoUsoPorVehiculo.put(v.getID(), 0L);
        }
        // Suma el tiempo de uso de cada viaje finalizado
        for (Viaje viaje : viajes) {
            if (viaje.getTiempoFin() > 0) {
                int id = viaje.getVehiculo().getID();
                long duracion = viaje.getTiempoFin() - viaje.getTiempoInicio();
                tiempoUsoPorVehiculo.put(id, tiempoUsoPorVehiculo.getOrDefault(id, 0L) + duracion);
            }
        }
        Map<VehiculoEnum, List<Object[]>> resultado = new HashMap<>();
        // Agrupa vehículos por tipo y añade el tiempo de uso
        for (Vehiculo v : vehiculos) {
            VehiculoEnum tipo = v.getVehiculoEnum();
            resultado.computeIfAbsent(tipo, k -> new ArrayList<>())
                .add(new Object[]{v, tiempoUsoPorVehiculo.getOrDefault(v.getID(), 0L)});
        }
        // Ordena cada lista por tiempo de uso descendente
        for (List<Object[]> lista : resultado.values()) {
            lista.sort((a, b) -> Long.compare((Long) b[1], (Long) a[1]));
        }
        return resultado;
    }

    // Devuelve los usuarios ordenados por gasto en un periodo
    public List<Object[]> obtenerUsuariosPorGastoEnPeriodo(long inicio, long fin) {
        List<Usuario> usuarios = obtenerUsuarios();
        List<Object[]> resultado = new ArrayList<>();
        // Calcula el gasto de cada usuario en el periodo
        for (Usuario usuario : usuarios) {
            double gasto = 0.0;
            // Suma el costo de los viajes del usuario en el periodo
            for (Viaje viaje : usuario.getHistorialViajes()) {
                if (viaje.getTiempoFin() > 0 && viaje.getTiempoFin() >= inicio && viaje.getTiempoInicio() <= fin) {
                    gasto += viaje.getCosto();
                }
            }
            resultado.add(new Object[]{usuario, gasto});
        }
        // Ordena por gasto descendente
        resultado.sort((a, b) -> Double.compare((double) b[1], (double) a[1]));
        return resultado;
    }
}