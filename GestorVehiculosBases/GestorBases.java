package GestorVehiculosBases;

import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Mecanico;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Localizaciones.Mapa;
import GestorVehiculosBases.Localizaciones.Base;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorBases {

    //=================================================
    // ATRIBUTOS
    //=================================================    
    
    private static GestorBases instancia;
    private Mapa mapa;
    private GestorVehiculos gestorVehiculos;
    private Map<String, LocalDateTime> basesDesactivadas = new HashMap<>();

    //=================================================
    // CONSTRUCTOR Y SINGLETON
    //=================================================

    private GestorBases() {
        this.gestorVehiculos = GestorVehiculos.getInstancia();
        this.mapa = Mapa.getInstancia();
    }

    public static GestorBases getInstancia() {
        if (instancia == null) {
            instancia = new GestorBases();
        }
        return instancia;
    }

    //=================================================
    // GESTIÓN DE BASES
    //=================================================

    // Añade una base por coordenadas, validando todos los parámetros de entrada y la existencia previa.
    public boolean addBasePorCoordenadas(String nombre, int x, int y, int capacidad) {
        // Validación de instancia de mapa
        if (mapa == null) {
            System.out.println("Error: El mapa no está inicializado.");
            return false;
        }
        // Validación de nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: El nombre de la base no puede estar vacío.");
            return false;
        }
        // Validación de coordenadas (puedes ajustar los límites según tu mapa)
        if (x < 0 || y < 0) {
            System.out.println("Error: Las coordenadas deben ser valores positivos.");
            return false;
        }
        // Validación de capacidad
        if (capacidad <= 0) {
            System.out.println("Error: La capacidad debe ser mayor que cero.");
            return false;
        }
        // Validación de duplicidad por nombre
        if (mapa.getBasePorNombre(nombre) != null) {
            System.out.println("Error: Ya existe una base con ese nombre.");
            return false;
        }
        // Llama al método addBase de Mapa y devuelve el resultado
        return mapa.addBase(nombre, x, y, capacidad);
    }

    // Elimina una base por coordenadas, validando todos los parámetros de entrada y la existencia previa.
    public boolean removeBasePorCoordenadas(int x, int y) {
        // Validación de instancia de mapa
        if (mapa == null) {
            System.out.println("Error: El mapa no está inicializado.");
            return false;
        }
        // Validación de coordenadas (puedes ajustar los límites según tu mapa)
        if (x < 0 || y < 0) {
            System.out.println("Error: Las coordenadas deben ser valores positivos.");
            return false;
        }
        // Llama al método removeBase de Mapa y devuelve el resultado
        return mapa.removeBase(x, y);
    }

    // Elimina una base por nombre
    public boolean removeBasePorNombre(String nombre) {
        // Validación de instancia de mapa
        if (mapa == null) {
            System.out.println("Error: El mapa no está inicializado.");
            return false;
        }
        // Validación de nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Error: El nombre de la base no puede estar vacío.");
            return false;
        }
        // Llama al método removeBase de Mapa y devuelve el resultado
        return mapa.removeBase(nombre);
    }

    //=================================================
    // ASIGNAR AVERIA BASE A MECANICO
    //=================================================

    // Asignar mecánico a avería en base
    public boolean asignarMecanicoAveriaBase(String DNIMecanico, String nombreBase) {
        if (DNIMecanico == null || DNIMecanico.isEmpty() || nombreBase == null || nombreBase.isEmpty()) {
            System.out.println("Error: DNI o nombre de base inválido");
            return false;
        }
        GestorPersonas gestorPersonas = GestorPersonas.getInstancia();
        Object personaMecanico = gestorPersonas.getPersonaPorDNI(DNIMecanico);
        if (!(personaMecanico instanceof Mecanico)) {
            System.out.println("Error: El DNI no corresponde a un mecánico.");
            return false;
        }
        Mecanico mecanico = (Mecanico) personaMecanico;

        Base base = mapa.getBasePorNombre(nombreBase);
        if (base == null) {
            System.out.println("Error: No se encontró la base '" + nombreBase + "'");
            return false;
        }

        if (base.getAverias()) {
            if (!mecanico.getBasesAveriadas().contains(base)) {
                mecanico.addBaseAveriada(base);
                System.out.println("Base '" + nombreBase + "' averiada asignada al mecánico " + DNIMecanico);
                return true;
            } else {
                System.out.println("La base '" + nombreBase + "' ya está asignada al mecánico.");
                return false;
            }
        } else {
            System.out.println("La base '" + nombreBase + "' no está averiada.");
            return false;
        }
    }

    //=================================================
    // INFORME BASES
    //=================================================

    // Muestra un informe detallado de todas las bases del sistema para el usuario dado.
    public void mostrarDetalleBases(Usuario usuario) {
        System.out.println("\n=== INFORME DETALLADO DE BASES ===");

        // Obtiene la lista de bases directamente desde el Mapa
        List<Base> bases = mapa.getListaBases();

        // Verifica si hay bases registradas
        if (bases == null || bases.isEmpty()) {
            System.out.println("No hay bases registradas en el sistema.");
            return;
        }

        // Cabecera del informe
        System.out.println("\n--- ESTADO GENERAL DE BASES ---");
        System.out.printf("%-15s %-12s %-10s %-12s %-12s %-12s %-12s %-10s\n",
                "Nombre", "Coord.", "Capacidad", "Patinetes", "Pat. Disp.", "Bicicletas", "Bici. Disp.", "Averías");
        System.out.println("------------------------------------------------------------------------------------------------------");

        // Estadísticas globales
        int baseConAverias = 0;
        int capacidadTotal = 0;
        int vehiculosEnBases = 0;
        int patineteTotal = 0;
        int bicicletaTotal = 0;
        int patineteDisponible = 0;
        int bicicletaDisponible = 0;

        // Recorre cada base y muestra su información
        for (Base base : bases) {
            int capacidad = base.getCapacidad();
            int ocupados = base.getEspaciosOcupados();
            int patinetes = base.getTotalPatinetes();
            int patinetesDisp = base.getPatinetesDisponibles(usuario);
            int bicicletas = base.getTotalBicicletas();
            int bicicletasDisp = base.getBicicletasDisponibles(usuario);
            boolean averias = base.getAverias();

            System.out.printf("%-15s (%2d,%2d)   %-10d %-12d %-12d %-12d %-12d %-10s\n",
                    base.getNombre(),
                    base.getCoordenadaX(), base.getCoordenadaY(),
                    capacidad,
                    patinetes, patinetesDisp,
                    bicicletas, bicicletasDisp,
                    averias ? "SÍ" : "NO");

            // Acumula estadísticas globales
            if (averias) baseConAverias++;
            capacidadTotal += capacidad;
            vehiculosEnBases += ocupados;
            patineteTotal += patinetes;
            patineteDisponible += patinetesDisp;
            bicicletaTotal += bicicletas;
            bicicletaDisponible += bicicletasDisp;
        }

        int porcentajeOcupacionGlobal = capacidadTotal > 0 ? vehiculosEnBases * 100 / capacidadTotal : 0;

        // Muestra estadísticas globales
        System.out.println("\n--- ESTADÍSTICAS GLOBALES DE BASES ---");
        System.out.println("Total de bases: " + bases.size());
        System.out.println("  - Con averías: " + baseConAverias);
        System.out.println("Capacidad total del sistema: " + capacidadTotal + " vehículos");
        System.out.println("Ocupación actual: " + vehiculosEnBases + " vehículos (" + porcentajeOcupacionGlobal + "%)");
        System.out.println("  - Patinetes: " + patineteTotal + " (Disponibles: " + patineteDisponible + ")");
        System.out.println("  - Bicicletas: " + bicicletaTotal + " (Disponibles: " + bicicletaDisponible + ")");
    }

    //=================================================
    // DESACTIVAR BASES
    //=================================================

    public boolean desactivarBase(String nombreBase, int minutos) {
        Base base = Mapa.getInstancia().getBasePorNombre(nombreBase);
        if (base == null) return false;

        LocalDateTime ahora = LocalDateTime.now();
        if (basesDesactivadas.containsKey(nombreBase)) {
            LocalDateTime fin = basesDesactivadas.get(nombreBase);
            if (ahora.isBefore(fin)) return false;
        }
        basesDesactivadas.put(nombreBase, ahora.plusMinutes(minutos));
        return true;
    }

    public boolean baseEstaDesactivada(String nombreBase) {
        if (!basesDesactivadas.containsKey(nombreBase)) return false;
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fin = basesDesactivadas.get(nombreBase);
        if (ahora.isAfter(fin)) {
            basesDesactivadas.remove(nombreBase);
            return false;
        }
        return true;
    }
}