package Menus.MenuAdmin;

import Enum.VehiculoEnum;
import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Mantenimiento;
import GestorPersonas.Personas.Mecanico;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Facturas.FacturaReparacionVehiculo;
import GestorVehiculosBases.Localizaciones.Base;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorViajes.Viaje;
import Utilidades.UtilidadesEntradaDatos;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuConsultas {

    // ===========================
    // ATRIBUTOS Y CONSTRUCTOR
    // ===========================

    // Servicio para realizar las consultas
    private final ServicioConsultas servicioConsultas;

    // Constructor de la clase MenuConsultas
    public MenuConsultas() {
        this.servicioConsultas = new ServicioConsultas();
    }

    // ===========================
    // MENÚ PRINCIPAL
    // ===========================

    // Muestra el menú principal de consultas y gestiona la selección del usuario
    public void mostrarMenu() {
        while (true) {
            // Imprime las opciones del menú
            System.out.println("\n========== MENÚ CONSULTAS ==========");
            System.out.println("1. Estado de batería de vehículos");
            System.out.println("2. Vehículos con averías mecánicas");
            System.out.println("3. Estado de las bases");
            System.out.println("4. Personas registradas en el sistema");
            System.out.println("5. Utilización de vehículos por usuarios");
            System.out.println("6. Vehículos en uso actualmente");
            System.out.println("7. Vehículos en uso en un periodo de tiempo");
            System.out.println("8. Bases por demanda");
            System.out.println("9. Listado de reparaciones de vehículos por periodo");
            System.out.println("10. Ranking de intervenciones de trabajadores"); 
            System.out.println("11. Listado de vehículos por tipo y tiempo de uso"); 
            System.out.println("12. Listado de usuarios por gasto en alquileres en un periodo");
            System.out.println("13. Volver al menú anterior");
            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Opción: ", 1, 13);

            // Ejecuta la opción seleccionada
            switch (opcion) {
                case 1 -> mostrarEstadoBateriaVehiculos();
                case 2 -> mostrarVehiculosConAverias();
                case 3 -> mostrarEstadoBases();
                case 4 -> mostrarPersonasRegistradas();
                case 5 -> mostrarUtilizacionVehiculosPorUsuarios();
                case 6 -> mostrarVehiculosEnUsoActual();
                case 7 -> mostrarVehiculosEnUsoPorPeriodo();
                case 8 -> mostrarBasesPorDemanda();
                case 9 -> mostrarReparacionesVehiculosPorPeriodo();
                case 10 -> mostrarRankingIntervencionesTrabajadores();
                case 11 -> mostrarVehiculosPorTiempoUso();
                case 12 -> mostrarUsuariosPorGastoEnPeriodo();
                case 13 -> {
                    System.out.println("Volviendo al menú anterior...");
                    return;
                }
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    // ===========================
    // MÉTODOS DE CONSULTA
    // ===========================

    // Muestra el estado de batería de todos los vehículos
    public void mostrarEstadoBateriaVehiculos() {
        System.out.println("=== Estado de batería de todos los vehículos ===");
        List<Vehiculo> listaVehiculos = servicioConsultas.obtenerVehiculos();
        // Recorre todos los vehículos y muestra su batería
        for (Vehiculo vehiculo : listaVehiculos) {
            int id = vehiculo.getID();
            VehiculoEnum tipo = vehiculo.getVehiculoEnum();
            double nivelBateria = vehiculo.getBateria();
            System.out.println("ID: " + id + " | Tipo: " + tipo + " | Batería: " + String.format("%.2f", nivelBateria) + "%");
        }
    }

    // Muestra los vehículos que tienen averías mecánicas
    public void mostrarVehiculosConAverias() {
        System.out.println("=== Vehículos con averías mecánicas ===");
        List<Vehiculo> listaVehiculos = servicioConsultas.obtenerVehiculosConAverias();
        if (listaVehiculos.isEmpty()) {
            System.out.println("No hay vehículos con averías.");
            return;
        }
        // Imprime cabecera de la tabla
        System.out.printf("%-6s | %-10s%n", "ID", "Tipo");
        System.out.println("------------------------");
        // Muestra cada vehículo con avería
        for (Vehiculo vehiculo : listaVehiculos) {
            int id = vehiculo.getID();
            VehiculoEnum tipo = vehiculo.getVehiculoEnum();
            System.out.printf("%-6d | %-10s%n", id, tipo);
        }
    }

    // Muestra el estado de todas las bases
    public void mostrarEstadoBases() {
        System.out.println("=== Estado de las bases de bicicletas y patinetes ===");
        System.out.printf("%-15s | %-10s | %-10s | %-10s | %-12s | %-10s%n",
            "Base", "Capacidad", "Libres", "Bicis", "Patinetes", "Avería");
        System.out.println("--------------------------------------------------------------------------------");
        List<Base> bases = servicioConsultas.obtenerBases();
        // Recorre todas las bases y muestra su información
        for (Base base : bases) {
            int totalBicis = base.getTotalBicicletas();
            int totalPat = base.getTotalPatinetes();
            int libres = base.getEspaciosLibres();
            String averia = base.getAverias() ? "SÍ" : "NO";
            System.out.printf("%-15s | %-10d | %-10d | %-10d | %-12d | %-10s%n",
                base.getNombre(), base.getCapacidad(), libres, totalBicis, totalPat, averia);
        }
    }

    // Muestra las personas registradas en el sistema
    public void mostrarPersonasRegistradas() {
        System.out.println("=== Personas registradas en el sistema ===");
        var listaPersonas = servicioConsultas.obtenerPersonasOrdenadasPorDNI();
        if (listaPersonas.isEmpty()) {
            System.out.println("No hay personas registradas.");
            return;
        }
        // Imprime cabecera de la tabla
        System.out.printf("%-10s | %-20s | %-20s | %-15s%n",
            "DNI", "Nombre", "Apellidos", "Rol");
        System.out.println("---------------------------------------------------------------------");
        // Muestra cada persona
        for (var persona : listaPersonas) {
            System.out.printf("%-10s | %-20s | %-20s | %-15s%n",
                persona.getDNI(),
                persona.getNombre(),
                persona.getApellidos(),
                persona.getRol()
            );
        }
    }

    // Muestra la utilización de vehículos por cada usuario
    public void mostrarUtilizacionVehiculosPorUsuarios() {
        System.out.println("=== Utilización de vehículos por usuarios ===");
        List<Usuario> usuarios = servicioConsultas.obtenerUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en el sistema.");
            return;
        }
        // Recorre cada usuario y muestra sus viajes
        for (Usuario usuario : usuarios) {
            System.out.println("\nUsuario: " + usuario.getNombre() + " " + usuario.getApellidos() + " | DNI: " + usuario.getDNI());
            List<Viaje> viajes = servicioConsultas.obtenerViajesUsuario(usuario);
            if (viajes.isEmpty()) {
                System.out.println("  No ha realizado viajes.");
                continue;
            }
            System.out.printf("  %-20s | %-15s | %-10s%n", "Fecha", "Tipo Vehículo", "Importe (€)");
            System.out.println("  ----------------------------------------------------------");
            double totalImporte = 0.0;
            for (Viaje viaje : viajes) {
                if (viaje.getTiempoFin() == 0) continue; // Solo mostrar viajes finalizados
                String fecha = Instant.ofEpochMilli(viaje.getTiempoInicio())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString();
                String tipoVehiculo = viaje.getVehiculo().getVehiculoEnum().toString();
                double importe = viaje.getCosto();
                totalImporte += importe;
                System.out.printf("  %-20s | %-15s | %-10.2f%n", fecha, tipoVehiculo, importe);
            }
            System.out.printf("  Total importe asociado: %.2f €%n", totalImporte);
        }
    }

    // Muestra los vehículos que están en uso actualmente
    public void mostrarVehiculosEnUsoActual() {
        System.out.println("=== Vehículos en uso actualmente ===");
        List<Vehiculo> vehiculosEnUso = servicioConsultas.obtenerVehiculosEnUsoActual();
        if (vehiculosEnUso.isEmpty()) {
            System.out.println("No hay vehículos en uso en este momento.");
            return;
        }
        // Imprime cabecera de la tabla
        System.out.printf("%-5s | %-15s | %-20s%n", "ID", "Tipo", "Usuario");
        System.out.println("-----------------------------------------------");
        List<Viaje> todosViajes = servicioConsultas.obtenerTodosViajes();
        // Busca el viaje activo para cada vehículo en uso
        for (Vehiculo vehiculo : vehiculosEnUso) {
            Viaje viajeActivo = null;
            for (Viaje v : todosViajes) {
                if (v.getVehiculo().getID() == vehiculo.getID() && v.estaActivo()) {
                    viajeActivo = v;
                    break;
                }
            }
            String usuario;
            if (viajeActivo != null) {
                usuario = viajeActivo.getUsuario().getNombre() + " " + viajeActivo.getUsuario().getApellidos();
            } else {
                usuario = "Desconocido";
            }
            System.out.printf("%-5d | %-15s | %-20s%n", vehiculo.getID(), vehiculo.getVehiculoEnum(), usuario);
        }
    }

    // Muestra los vehículos en uso en un periodo de tiempo dado
    public void mostrarVehiculosEnUsoPorPeriodo() {
        System.out.println("=== Vehículos en uso en un periodo de tiempo ===");
        // Solicita fechas al usuario
        System.out.println("Introduce la fecha de inicio:");
        long inicio = pedirFechaComoMillis();
        System.out.println("Introduce la fecha de fin:");
        long fin = pedirFechaComoMillis();
        if (fin < inicio) {
            System.out.println("El tiempo de fin debe ser posterior al de inicio.");
            return;
        }
        List<Viaje> viajesEnPeriodo = servicioConsultas.obtenerViajesEnPeriodo(inicio, fin);
        if (viajesEnPeriodo.isEmpty()) {
            System.out.println("No hubo vehículos en uso en ese periodo.");
            return;
        }
        // Imprime cabecera de la tabla
        System.out.printf("%-5s | %-15s | %-20s | %-20s | %-20s%n", "ID", "Tipo", "Usuario", "Inicio", "Fin");
        System.out.println("-----------------------------------------------------------------------------------------------");
        // Muestra cada viaje en el periodo
        for (Viaje v : viajesEnPeriodo) {
            String usuario = v.getUsuario().getNombre() + " " + v.getUsuario().getApellidos();
            String fechaInicio = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(v.getTiempoInicio()));
            String fechaFin = (v.getTiempoFin() == 0)
                    ? "En curso"
                    : new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(v.getTiempoFin()));
            System.out.printf("%-5d | %-15s | %-20s | %-20s | %-20s%n",
                    v.getVehiculo().getID(), v.getVehiculo().getVehiculoEnum(), usuario, fechaInicio, fechaFin);
        }
    }

    // Muestra las bases ordenadas por demanda (viajes iniciados y finalizados)
    public void mostrarBasesPorDemanda() {
        // Tabla por viajes iniciados
        List<Base> basesIniciados = servicioConsultas.obtenerBasesOrdenadasPorViajesIniciados();
        System.out.println("\n=== Bases ordenadas por VIAJES INICIADOS ===");
        System.out.printf("%-15s | %-10s | %-10s | %-10s | %-10s%n", "Base", "Capacidad", "Iniciados", "Finalizados", "Avería");
        System.out.println("--------------------------------------------------------------------------");
        // Muestra cada base por viajes iniciados
        for (Base base : basesIniciados) {
            System.out.printf("%-15s | %-10d | %-10d | %-10d | %-10s%n",
                base.getNombre(),
                base.getCapacidad(),
                base.getViajesIniciadosEnBase(),
                base.getViajesFinalizadosEnBase(),
                base.getAverias() ? "SÍ" : "NO"
            );
        }

        // Tabla por viajes finalizados
        List<Base> basesFinalizados = servicioConsultas.obtenerBasesOrdenadasPorViajesFinalizados();
        System.out.println("\n=== Bases ordenadas por VIAJES FINALIZADOS ===");
        System.out.printf("%-15s | %-10s | %-10s | %-10s | %-10s%n", "Base", "Capacidad", "Iniciados", "Finalizados", "Avería");
        System.out.println("--------------------------------------------------------------------------");
        // Muestra cada base por viajes finalizados
        for (Base base : basesFinalizados) {
            System.out.printf("%-15s | %-10d | %-10d | %-10d | %-10s%n",
                base.getNombre(),
                base.getCapacidad(),
                base.getViajesIniciadosEnBase(),
                base.getViajesFinalizadosEnBase(),
                base.getAverias() ? "SÍ" : "NO"
            );
        }
    }

    // Muestra el listado de reparaciones de vehículos en un periodo dado
    public void mostrarReparacionesVehiculosPorPeriodo() {
        // Pedir al usuario la fecha de inicio y fin
        System.out.println("=== Listado de reparaciones de vehículos por periodo ===");
        System.out.println("Introduce la fecha de inicio:");
        int diaInicio = UtilidadesEntradaDatos.getEnteroPositivo("Día (1-31): ", 1, 31);
        int mesInicio = UtilidadesEntradaDatos.getEnteroPositivo("Mes (1-12): ", 1, 12);
        int anioInicio = UtilidadesEntradaDatos.getEnteroPositivo("Año (ejemplo: 2025): ", 2000, 2100);
        LocalDate fechaInicio = LocalDate.of(anioInicio, mesInicio, diaInicio);

        System.out.println("Introduce la fecha de fin:");
        int diaFin = UtilidadesEntradaDatos.getEnteroPositivo("Día (1-31): ", 1, 31);
        int mesFin = UtilidadesEntradaDatos.getEnteroPositivo("Mes (1-12): ", 1, 12);
        int anioFin = UtilidadesEntradaDatos.getEnteroPositivo("Año (ejemplo: 2025): ", 2000, 2100);
        LocalDate fechaFin = LocalDate.of(anioFin, mesFin, diaFin);

        // Comprobar que la fecha de fin no es antes que la de inicio
        if (fechaFin.isBefore(fechaInicio)) {
            System.out.println("La fecha de fin debe ser posterior o igual a la de inicio.");
            return;
        }

        // Conseguir todos los mecánicos del sistema
        var personas = GestorPersonas.getInstancia().getPersonas();
        List<Mecanico> mecanicos = new ArrayList<>();
        for (var p : personas) {
            if (p instanceof Mecanico m) {
                mecanicos.add(m);
            }
        }

        // Crear un mapa para agrupar las reparaciones por vehículo
        Map<Integer, List<FacturaReparacionVehiculo>> reparacionesPorVehiculo = new HashMap<>();

        // Recorrer todos los mecánicos y sus reparaciones
        for (Mecanico mecanico : mecanicos) {
            for (FacturaReparacionVehiculo factura : mecanico.getReparacionesVehiculosRealizadas()) {
                LocalDate fecha = factura.getFecha();
                // Si la fecha está en el rango pedido, la añadimos al mapa
                if ((fecha.isEqual(fechaInicio) || fecha.isAfter(fechaInicio)) &&
                    (fecha.isEqual(fechaFin) || fecha.isBefore(fechaFin))) {
                    int idVehiculo = factura.getVehiculo().getID();
                    if (!reparacionesPorVehiculo.containsKey(idVehiculo)) {
                        reparacionesPorVehiculo.put(idVehiculo, new ArrayList<>());
                    }
                    reparacionesPorVehiculo.get(idVehiculo).add(factura);
                }
            }
        }

        // Si no hay reparaciones, avisar al usuario
        if (reparacionesPorVehiculo.isEmpty()) {
            System.out.println("No hay reparaciones registradas en ese periodo.");
            return;
        }

        // Mostrar los resultados
        System.out.println("Listado de vehículos con reparaciones entre " + fechaInicio + " y " + fechaFin + ":");
        for (var entry : reparacionesPorVehiculo.entrySet()) {
            int idVehiculo = entry.getKey();
            List<FacturaReparacionVehiculo> facturas = entry.getValue();
            Vehiculo vehiculo = facturas.get(0).getVehiculo();
            double total = 0;
            System.out.println("Vehículo ID: " + idVehiculo + " (" + vehiculo.getNombreVehiculo() + ")");
            System.out.println("  Número de reparaciones: " + facturas.size());
            for (FacturaReparacionVehiculo factura : facturas) {
                System.out.println("    Fecha: " + factura.getFecha() + " | Importe: " + factura.getCoste() + "€");
                total += factura.getCoste();
            }
            System.out.println("  Importe total: " + String.format("%.2f", total) + "€");
            System.out.println("-----------------------------------");
        }
    }

    // Muestra el ranking de intervenciones de mecánicos y personal de mantenimiento
    public void mostrarRankingIntervencionesTrabajadores() {
        System.out.println("=== Ranking de intervenciones de Mecánicos y Mantenimiento ===");
        var personas = GestorPersonas.getInstancia().getPersonas();
        List<Object[]> ranking = new ArrayList<>();

        // Recorre todas las personas y añade mecánicos y mantenimiento al ranking
        for (var p : personas) {
            if (p instanceof Mecanico m) {
                ranking.add(new Object[]{
                    m.getNombre() + " " + m.getApellidos(),
                    "Mecánico",
                    m.getIntervencionesRealizadas()
                });
            } else if (p instanceof Mantenimiento mt) {
                ranking.add(new Object[]{
                    mt.getNombre() + " " + mt.getApellidos(),
                    "Mantenimiento",
                    mt.getIntervencionesRealizadas()
                });
            }
        }

        // Ordenar de mayor a menor por intervenciones
        ranking.sort((a, b) -> Integer.compare((int) b[2], (int) a[2]));

        if (ranking.isEmpty()) {
            System.out.println("No hay mecánicos ni personal de mantenimiento registrados.");
            return;
        }

        // Imprime cabecera de la tabla
        System.out.printf("%-25s | %-13s | %-15s%n", "Nombre", "Rol", "Intervenciones");
        System.out.println("---------------------------------------------------------------");
        // Muestra cada fila del ranking
        for (Object[] fila : ranking) {
            System.out.printf("%-25s | %-13s | %-15d%n", fila[0], fila[1], fila[2]);
        }
    }

    // Muestra el listado de vehículos por tipo y tiempo de uso
    public void mostrarVehiculosPorTiempoUso() {
        System.out.println("=== Listado de vehículos por tipo y tiempo de uso (mayor a menor) ===");
        List<Vehiculo> vehiculos = servicioConsultas.obtenerVehiculos();
        List<Viaje> viajes = servicioConsultas.obtenerTodosViajes();

        // Map: VehiculoID -> Tiempo total de uso en milisegundos
        Map<Integer, Long> tiempoUsoPorVehiculo = new HashMap<>();
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

        // Agrupar por tipo de vehículo
        Map<VehiculoEnum, List<Vehiculo>> vehiculosPorTipo = new HashMap<>();
        for (Vehiculo v : vehiculos) {
            vehiculosPorTipo.computeIfAbsent(v.getVehiculoEnum(), k -> new ArrayList<>()).add(v);
        }

        // Para cada tipo de vehículo, muestra la lista ordenada por tiempo de uso
        for (VehiculoEnum tipo : VehiculoEnum.values()) {
            List<Vehiculo> lista = vehiculosPorTipo.get(tipo);
            if (lista == null || lista.isEmpty()) continue;
            // Ordenar por tiempo de uso descendente
            lista.sort((v1, v2) -> Long.compare(
                tiempoUsoPorVehiculo.getOrDefault(v2.getID(), 0L),
                tiempoUsoPorVehiculo.getOrDefault(v1.getID(), 0L)
            ));
            System.out.println("\nTipo: " + tipo);
            System.out.printf("%-5s | %-20s | %-15s%n", "ID", "Nombre", "Tiempo de uso (h)");
            System.out.println("-----------------------------------------------");
            // Muestra cada vehículo del tipo
            for (Vehiculo v : lista) {
                long tiempoMs = tiempoUsoPorVehiculo.getOrDefault(v.getID(), 0L);
                double horas = tiempoMs / 3600000.0;
                System.out.printf("%-5d | %-20s | %-15.2f%n", v.getID(), v.getNombreVehiculo(), horas);
            }
        }
    }

    // Muestra el listado de usuarios por gasto en alquileres en un periodo
    public void mostrarUsuariosPorGastoEnPeriodo() {
        System.out.println("=== Listado de usuarios por gasto en alquileres en un periodo ===");
        // Solicita fechas al usuario
        System.out.println("Introduce la fecha de inicio:");
        long inicio = pedirFechaComoMillis();
        System.out.println("Introduce la fecha de fin:");
        long fin = pedirFechaComoMillis();
        if (fin < inicio) {
            System.out.println("El tiempo de fin debe ser posterior al de inicio.");
            return;
        }

        List<Usuario> usuarios = servicioConsultas.obtenerUsuarios();
        Map<Usuario, Double> gastoPorUsuario = new HashMap<>();

        // Calcula el gasto de cada usuario en el periodo
        for (Usuario usuario : usuarios) {
            double gasto = 0.0;
            for (Viaje viaje : usuario.getHistorialViajes()) {
                if (viaje.getTiempoFin() > 0 && viaje.getTiempoFin() >= inicio && viaje.getTiempoInicio() <= fin) {
                    gasto += viaje.getCosto();
                }
            }
            gastoPorUsuario.put(usuario, gasto);
        }

        // Ordenar usuarios por gasto descendente
        List<Map.Entry<Usuario, Double>> listaOrdenada = new ArrayList<>(gastoPorUsuario.entrySet());
        listaOrdenada.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // Imprime cabecera de la tabla
        System.out.printf("%-10s | %-20s | %-20s | %-10s%n", "DNI", "Nombre", "Apellidos", "Gasto (€)");
        System.out.println("---------------------------------------------------------------------");
        // Muestra cada usuario y su gasto
        for (Map.Entry<Usuario, Double> entry : listaOrdenada) {
            Usuario usuario = entry.getKey();
            double gasto = entry.getValue();
            System.out.printf("%-10s | %-20s | %-20s | %-10.2f%n",
                usuario.getDNI(),
                usuario.getNombre(),
                usuario.getApellidos(),
                gasto
            );
        }
    }

    // ===========================
    // MÉTODOS AUXILIARES
    // ===========================

    // Solicita al usuario una fecha y hora y la devuelve en milisegundos desde epoch
    private long pedirFechaComoMillis() {
        int dia = UtilidadesEntradaDatos.getEnteroPositivo("Día (1-31): ", 1, 31);
        int mes = UtilidadesEntradaDatos.getEnteroPositivo("Mes (1-12): ", 1, 12);
        int anio = UtilidadesEntradaDatos.getEnteroPositivo("Año (ejemplo: 2025): ", 2000, 2100);
        int hora = UtilidadesEntradaDatos.getEnteroPositivo("Hora (0-23): ", 0, 23);
        int minuto = UtilidadesEntradaDatos.getEnteroPositivo("Minuto (0-59): ", 0, 59);
        LocalDateTime ldt = LocalDateTime.of(anio, mes, dia, hora, minuto);
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
