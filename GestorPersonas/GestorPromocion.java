package GestorPersonas;
import Enum.VehiculoEnum;
import GestorPersonas.Personas.Usuario;
import GestorVehiculosBases.Vehiculos.Vehiculo;
import GestorViajes.GestorViajes;
import GestorViajes.Viaje;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GestorPromocion {

    //=================================================
    // ATRIBUTOS Y PATRÓN SINGLETON
    //=================================================
    private static GestorPromocion instancia;

    // Constructor privado para patrón Singleton
    private GestorPromocion() {}

    // Devuelve la instancia única de GestorPromocion
    public static GestorPromocion getInstancia() {
        if (instancia == null) {
            instancia = new GestorPromocion();
        }
        return instancia;
    }

    //=================================================
    // MÉTODOS PRINCIPALES DE PROMOCIÓN
    //=================================================

    // Devuelve la lista de usuarios elegibles para promoción a PREMIUM
    public List<Usuario> getListaCandidatosPremium(Map<Usuario, List<Viaje>> viajesPorUsuarioMap) {
        List<Usuario> usuariosParaPromocion = new ArrayList<>();

        // Fechas de referencia para los últimos 1, 3 y 6 meses
        LocalDate ahora = LocalDate.now();
        LocalDate haceUnMes = ahora.minusMonths(1);
        LocalDate haceTresMeses = ahora.minusMonths(3);
        LocalDate haceSeisMeses = ahora.minusMonths(6);

        // Iterar sobre cada usuario y sus viajes
        for (Map.Entry<Usuario, List<Viaje>> entry : viajesPorUsuarioMap.entrySet()) {
            Usuario usuario = entry.getKey();
            List<Viaje> viajes = entry.getValue();

            // Filtrar viajes según las fechas de referencia
            List<Viaje> viajesUltimoMes = filtrarViajesPorFecha(
                viajes, 
                haceUnMes.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            );
            List<Viaje> viajesUltimosTresMeses = filtrarViajesPorFecha(
                viajes, 
                haceTresMeses.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            );
            List<Viaje> viajesUltimosSeisMeses = filtrarViajesPorFecha(
                viajes, 
                haceSeisMeses.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            );

            // Verificar condiciones de promoción
            boolean cumpleCondicion1 = verificarViajesUltimoMes(viajesUltimoMes); // 15 viajes último mes
            boolean cumpleCondicion2 = verificarViajesConsecutivos(viajesUltimosTresMeses, 10, 3); // 10 viajes/mes 3 meses consecutivos
            boolean cumpleCondicion3 = verificarUsoDeTodosLosTipos(viajesUltimosSeisMeses); // Todos los tipos 6 meses consecutivos

            // Si cumple al menos una condición, agregar a la lista de promoción
            if (cumpleCondicion1 || cumpleCondicion2 || cumpleCondicion3) {
                usuariosParaPromocion.add(usuario);
            }
        }

        return usuariosParaPromocion;
    }

    //=================================================
    // MÉTODOS AUXILIARES DE PROMOCIÓN
    //=================================================

    // Verifica si un usuario realizó al menos 15 viajes en el último mes
    private boolean verificarViajesUltimoMes(List<Viaje> viajesUltimoMes) {
        // Retorna true si el tamaño de la lista es mayor o igual a 15
        return viajesUltimoMes.size() >= 15;
    }

    // Filtra los viajes que ocurrieron después de una fecha específica (en milisegundos)
    private List<Viaje> filtrarViajesPorFecha(List<Viaje> viajes, long fechaInicioMillis) {
        List<Viaje> viajesFiltrados = new ArrayList<>();
        for (Viaje viaje : viajes) {
            // Si el viaje ocurrió después de la fecha indicada, se añade a la lista
            if (viaje.getTiempoInicio() >= fechaInicioMillis) {
                viajesFiltrados.add(viaje);
            }
        }
        return viajesFiltrados;
    }

    // Verifica si un usuario realizó un número mínimo de viajes durante meses consecutivos
    private boolean verificarViajesConsecutivos(List<Viaje> viajes, int minimoViajes, int mesesConsecutivos) {
        // Mapa para contar viajes por mes (clave: "YYYY-MM", valor: cantidad de viajes)
        Map<String, Integer> viajesPorMes = new HashMap<>();

        // Agrupar viajes por mes
        for (Viaje viaje : viajes) {
            String mesId = calcularMesId(viaje.getTiempoInicio());
            viajesPorMes.put(mesId, viajesPorMes.getOrDefault(mesId, 0) + 1);
        }

        // Ordenar los meses cronológicamente
        List<String> mesesOrdenados = new ArrayList<>(viajesPorMes.keySet());
        mesesOrdenados.sort(String::compareTo);

        int mesesCumplidos = 0; // Contador de meses consecutivos que cumplen la condición

        // Recorrer los meses ordenados y verificar si cumplen el mínimo de viajes
        for (String mes : mesesOrdenados) {
            if (viajesPorMes.get(mes) >= minimoViajes) {
                mesesCumplidos++;
                // Si ya se cumplieron los meses consecutivos requeridos, retornar true
                if (mesesCumplidos >= mesesConsecutivos) {
                    return true;
                }
            } else {
                // Si no cumple en un mes, reiniciar el contador
                mesesCumplidos = 0;
            }
        }
        // Si no se encontraron los meses consecutivos requeridos, retornar false
        return false;
    }

    // Verifica si un usuario utilizó todos los tipos de vehículos durante 6 meses consecutivos
    private boolean verificarUsoDeTodosLosTipos(List<Viaje> viajes) {
        // Mapa para agrupar, por mes, los tipos de vehículo usados
        Map<String, Set<VehiculoEnum>> tiposPorMes = new HashMap<>();

        // Agrupar los tipos de vehículo usados en cada mes
        for (Viaje viaje : viajes) {
            String mesId = calcularMesId(viaje.getTiempoInicio()); // Obtener identificador de mes (YYYY-MM)
            tiposPorMes.putIfAbsent(mesId, new HashSet<>()); // Si no existe el mes, crear el set
            // Añadir el tipo de vehículo usado en este viaje al set del mes correspondiente
            tiposPorMes.get(mesId).add((VehiculoEnum) ((Vehiculo) viaje.getVehiculo()).getVehiculoEnum());
        }

        // Ordenar los meses cronológicamente
        List<String> mesesOrdenados = new ArrayList<>(tiposPorMes.keySet());
        mesesOrdenados.sort(String::compareTo);

        int mesesCumplidos = 0; // Contador de meses consecutivos que cumplen la condición

        // Recorrer los meses ordenados y verificar si en cada uno se usaron todos los tipos de vehículo
        for (String mes : mesesOrdenados) {
            Set<VehiculoEnum> tiposEnMes = tiposPorMes.get(mes);

            // Comprobar si en este mes se usaron los tres tipos de vehículo requeridos
            if (tiposEnMes.contains(VehiculoEnum.BICICLETA) &&
                tiposEnMes.contains(VehiculoEnum.PATINETE) &&
                tiposEnMes.contains(VehiculoEnum.MOTOPEQUENA)) {
                mesesCumplidos++; // Sumar mes consecutivo que cumple
                // Si ya se cumplieron 6 meses consecutivos, retornar true
                if (mesesCumplidos >= 6) {
                    return true;
                }
            } else {
                // Si falta algún tipo en el mes, reiniciar el contador de meses consecutivos
                mesesCumplidos = 0;
            }
        }

        // Si no se encontraron 6 meses consecutivos, retornar false
        return false;
    }

    // Calcula el identificador de un mes en formato "YYYY-MM" a partir de un tiempo en milisegundos
    private String calcularMesId(long tiempoInicio) {
        Instant instant = Instant.ofEpochMilli(tiempoInicio);
        ZoneId zone = ZoneId.systemDefault();
        int year = instant.atZone(zone).getYear();
        int month = instant.atZone(zone).getMonthValue();
        // Formato con cero a la izquierda para el mes
        return year + "-" + (month < 10 ? "0" + month : month);
    }

    //=================================================
    // CONSULTAS Y UTILIDADES DE PROMOCIÓN
    //=================================================

    // Muestra en consola los usuarios que cumplen con las condiciones para ser promovidos
    public void mostrarUsuariosPromocion() {
        GestorViajes gestorViajes = GestorViajes.getInstancia();
        Map<Usuario, List<Viaje>> viajesPorUsuario = gestorViajes.getMapaViajesPorUsuario();
        List<Usuario> usuariosPromocion = getListaCandidatosPremium(viajesPorUsuario);

        System.out.println("\n===== USUARIOS QUE CUMPLEN CONDICIONES PARA PROMOCIÓN =====");

        if (usuariosPromocion.isEmpty()) {
            System.out.println("No hay usuarios que cumplan las condiciones para promoción.");
            return;
        }

        System.out.println("Se encontraron " + usuariosPromocion.size() + " usuarios elegibles para promoción:");
        System.out.println("------------------------------------------------------------");

        for (Usuario usuario : usuariosPromocion) {
            System.out.println("DNI: " + usuario.getDNI() + " | Nombre: " + usuario.getNombre() + " " + usuario.getApellidos());
            List<Viaje> viajes = viajesPorUsuario.get(usuario);

            // Calcular fechas de referencia
            LocalDate ahora = LocalDate.now();
            LocalDate haceUnMes = ahora.minusMonths(1);
            LocalDate haceTresMeses = ahora.minusMonths(3);
            LocalDate haceSeisMeses = ahora.minusMonths(6);

            // Filtrar viajes por periodo
            List<Viaje> viajesUltimoMes = filtrarViajesPorFecha(viajes, haceUnMes.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            List<Viaje> viajesUltimosTresMeses = filtrarViajesPorFecha(viajes, haceTresMeses.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
            List<Viaje> viajesUltimosSeisMeses = filtrarViajesPorFecha(viajes, haceSeisMeses.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

            // Verificar condiciones para mostrar cuáles cumple
            boolean condicion1 = verificarViajesUltimoMes(viajesUltimoMes);
            boolean condicion2 = verificarViajesConsecutivos(viajesUltimosTresMeses, 10, 3);
            boolean condicion3 = verificarUsoDeTodosLosTipos(viajesUltimosSeisMeses);

            System.out.println("  Condiciones cumplidas:");
            if (condicion1) {
                System.out.println("  - Al menos 15 viajes en el último mes (" + viajesUltimoMes.size() + " viajes)");
            }
            if (condicion2) {
                System.out.println("  - Al menos 10 viajes mensuales durante 3 meses consecutivos");
            }
            if (condicion3) {
                System.out.println("  - Uso de todos los tipos de vehículos durante 6 meses consecutivos");
            }
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("============================================================");
        System.out.println("Total de usuarios elegibles para promoción: " + usuariosPromocion.size());
    }

    // Obtiene información detallada de por qué un usuario específico es elegible para promoción
    public Map<String, Object> obtenerDetallesPromocionUsuario(Usuario usuario) {
        GestorViajes gestorViajes = GestorViajes.getInstancia();
        Map<Usuario, List<Viaje>> viajesPorUsuario = gestorViajes.getMapaViajesPorUsuario();

        // Si el usuario no tiene viajes registrados, retornar null
        if (!viajesPorUsuario.containsKey(usuario)) {
            return null;
        }

        List<Viaje> viajes = viajesPorUsuario.get(usuario);
        Map<String, Object> detalles = new HashMap<>();

        // Calcular fechas de referencia
        LocalDate ahora = LocalDate.now();
        LocalDate haceUnMes = ahora.minusMonths(1);
        LocalDate haceTresMeses = ahora.minusMonths(3);
        LocalDate haceSeisMeses = ahora.minusMonths(6);

        // Filtrar viajes por periodo
        List<Viaje> viajesUltimoMes = filtrarViajesPorFecha(viajes, haceUnMes.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<Viaje> viajesUltimosTresMeses = filtrarViajesPorFecha(viajes, haceTresMeses.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        List<Viaje> viajesUltimosSeisMeses = filtrarViajesPorFecha(viajes, haceSeisMeses.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

        // Verificar condiciones
        boolean condicion1 = verificarViajesUltimoMes(viajesUltimoMes);
        boolean condicion2 = verificarViajesConsecutivos(viajesUltimosTresMeses, 10, 3);
        boolean condicion3 = verificarUsoDeTodosLosTipos(viajesUltimosSeisMeses);

        // Si no cumple ninguna condición, retornar null
        if (!condicion1 && !condicion2 && !condicion3) {
            return null;
        }

        // Guardar detalles de las condiciones cumplidas
        detalles.put("condicion1", condicion1);
        detalles.put("condicion2", condicion2);
        detalles.put("condicion3", condicion3);
        detalles.put("viajesUltimoMes", viajesUltimoMes.size());

        return detalles;
    }

    // Verifica si un usuario específico es elegible para promoción
    public boolean esUsuarioElegibleParaPromocion(Usuario usuario) {
        return obtenerDetallesPromocionUsuario(usuario) != null;
    }

}
