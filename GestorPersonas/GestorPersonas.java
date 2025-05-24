package GestorPersonas;
import Enum.RolEnum;
import GestorPersonas.Personas.Administrador;
import GestorPersonas.Personas.Mantenimiento;
import GestorPersonas.Personas.Mecanico;
import GestorPersonas.Personas.Persona;
import GestorPersonas.Personas.Premium;
import GestorPersonas.Personas.Standard;
import GestorPersonas.Personas.Usuario;
import GestorViajes.Viaje;
import Utilidades.UtilidadesEntradaDatos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GestorPersonas {

    //=================================================
    // ATRIBUTOS Y SINGLETON
    //=================================================
    private static GestorPersonas gestorPersonas;
    private final Map<String, Persona> personas = new HashMap<>();
    private final Set<String> DNISet = new HashSet<>();
    private boolean administradorInicialCreado = false;

    //=================================================
    // CONSTRUCTOR Y SINGLETON
    //=================================================
    private GestorPersonas() {
    }

    public static GestorPersonas getInstancia() {
        if (gestorPersonas == null) {
            gestorPersonas = new GestorPersonas();
        }
        return gestorPersonas;
    }

    //=================================================
    // ADMINISTRADOR INICIAL
    //=================================================
    // Agrega un administrador inicial
    public boolean addAdministradorInicial(Administrador admin) {
        // Verifica si ya existe un administrador o si el sistema no está vacío
        if (administradorInicialCreado || !personas.isEmpty() || !DNISet.isEmpty()) {
            System.out.println("Error: No se puede agregar el administrador inicial. Ya existe un administrador o el sistema no está vacío.");
            return false;
        }
        // Agrega el administrador al mapa y al set de DNIs
        personas.put(admin.getDNI(), admin);
        DNISet.add(admin.getDNI());
        administradorInicialCreado = true;
        System.out.println("Administrador inicial agregado correctamente: " + admin.getDNI() + " - " + admin.getNombre() + " " + admin.getApellidos());
        return true;
    }

    //=================================================
    // VALIDACIÓN DE DATOS
    //=================================================
    // Verifica si un DNI ya existe
    public boolean existeDNI(String DNI) {
        return DNISet.contains(DNI);
    }

    // Valida el formato del DNI (8 dígitos y una letra mayúscula)
    public boolean esDNIValido(String DNI) {
        return DNI != null && DNI.matches("\\d{8}[A-Z]");
    }

    // Valida que nombre y apellidos no sean nulos ni vacíos
    private boolean sonNombreApellidosValidos(String nombre, String apellidos) {
        if (nombre == null || nombre.trim().isEmpty()) return false;
        if (apellidos == null || apellidos.trim().isEmpty()) return false;
        return true;
    }

    //=================================================
    // CRUD DE PERSONAS
    //=================================================
    // Agrega una nueva persona según el rol
    public void addPersona(String nombre, String apellidos, String DNI, RolEnum rol) {
        // Verifica si el DNI ya existe
        if (existeDNI(DNI)) {
            System.out.println("Error: No se pudo agregar la persona. El DNI ya existe en el sistema.");
            return;
        }
        // Valida los datos de entrada
        if (!sonNombreApellidosValidos(nombre, apellidos) || !esDNIValido(DNI)) {
            System.out.println("Error: No se pudo agregar la persona. Verifica el formato de los datos ingresados.");
            return;
        }
        // Crea la persona según el rol
        Persona persona;
        switch (rol) {
            case USUARIO_STANDARD:
                persona = new Standard(nombre, apellidos, DNI);
                break;
            case MECANICO:
                persona = new Mecanico(nombre, apellidos, DNI);
                break;
            case MANTENIMIENTO:
                persona = new Mantenimiento(nombre, apellidos, DNI);
                break;
            case ADMINISTRADOR:
                persona = new Administrador(nombre, apellidos, DNI);
                break;
            default:
                throw new IllegalArgumentException("Error: Rol no reconocido.");
        }
        // Agrega la persona al sistema
        personas.put(DNI, persona);
        DNISet.add(DNI);
        System.out.println("Persona agregada correctamente.");
    }

    // Obtiene una persona por DNI
    public Persona getPersonaPorDNI(String DNI) {
        // Valida el formato del DNI
        if (!esDNIValido(DNI)) {
            System.out.println("Error: Verifica el formato de los datos ingresados. Deben ser 8 dígitos seguidos de una letra mayúscula.");
            return null;
        }
        // Verifica si el DNI existe
        if (!existeDNI(DNI)) {
            System.out.println("Error: No se pudo eliminar la persona. El DNI no existe en el sistema.");
            return null;
        }
        // Retorna la persona correspondiente
        return personas.get(DNI);
    }

    // Elimina una persona por DNI
    public boolean removePersona(String DNI) {
        // Valida el formato del DNI
        if (!esDNIValido(DNI)) {
            System.out.println("Error: Verifica el formato de los datos ingresados. Deben ser 8 dígitos seguidos de una letra mayúscula.");
            return false;
        }
        // Verifica si el DNI existe
        if (!existeDNI(DNI)) {
            System.out.println("Error: No se pudo eliminar la persona. El DNI no existe en el sistema.");
            return false;
        }
        // Elimina la persona del sistema
        personas.remove(DNI);
        DNISet.remove(DNI);
        System.out.println("Persona eliminada correctamente.");
        return true;
    }

    // Modifica una persona por DNI
    public boolean modificarPersona(String DNI, String nuevoNombre, String nuevosApellidos, RolEnum nuevoRol) {
        // Valida los datos de entrada
        if (!sonNombreApellidosValidos(nuevoNombre, nuevosApellidos)) {
            System.out.println("Error: No se pudo modificar la persona. Verifica el formato de los datos ingresados.");
            return false;
        }
        // Valida el formato del DNI
        if (!esDNIValido(DNI)) {
            System.out.println("Error: Verifica el formato de los datos ingresados. Deben ser 8 dígitos seguidos de una letra mayúscula.");
            return false;
        }
        // Verifica si el DNI existe
        if (!existeDNI(DNI)) {
            System.out.println("Error: No se pudo modificar la persona. El DNI no existe en el sistema.");
            return false;
        }
        // Modifica los datos de la persona
        Persona persona = personas.get(DNI);
        if (persona != null) {
            persona.setNombre(nuevoNombre);
            persona.setApellidos(nuevosApellidos);
            persona.setRol(nuevoRol);
            System.out.println("Persona modificada correctamente.");
            return true;
        }
        return false;
    }

    // Obtiene el rol de una persona por DNI
    public RolEnum getRolPersona(String DNI) {
        Persona persona = getPersonaPorDNI(DNI);
        if (persona != null) {
            return persona.getRol();
        } else {
            System.out.println("Error: Persona no encontrada.");
            return null;
        }
    }

    //=================================================
    // USUARIOS Y ROLES
    //=================================================
    // Devuelve una lista con todos los usuarios registrados
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> resultado = new ArrayList<>();
        // Filtra solo las instancias de Usuario
        for (Persona persona : personas.values()) {
            if (persona instanceof Usuario) {
                resultado.add((Usuario) persona);
            }
        }
        return resultado;
    }

    // Cambia el rol de un usuario a PREMIUM o STANDARD
    public boolean cambiarRolUsuario(String DNI, boolean hacerPremium) {
        // Valida el formato del DNI
        if (!esDNIValido(DNI)) {
            System.out.println("Error: Formato de DNI inválido.");
            return false;
        }
        // Verifica si el DNI existe
        if (!existeDNI(DNI)) {
            System.out.println("Error: El DNI no existe en el sistema.");
            return false;
        }
        // Verifica que la persona sea un usuario
        Persona persona = getPersonaPorDNI(DNI);
        if (!(persona instanceof Usuario)) {
            System.out.println("Error: El DNI proporcionado no corresponde a un usuario.");
            return false;
        }
        Usuario usuario = (Usuario) persona;
        // Verifica si ya tiene el rol deseado
        if ((hacerPremium && usuario.getRol() == RolEnum.USUARIO_PREMIUM) ||
            (!hacerPremium && usuario.getRol() == RolEnum.USUARIO_STANDARD)) {
            System.out.println("El usuario ya tiene el estado " + 
                               (hacerPremium ? "PREMIUM" : "STANDARD"));
            return false;
        }
        // Si se va a hacer premium, verifica condiciones de promoción
        if (hacerPremium) {
            GestorPromocion ayudantePromocion = GestorPromocion.getInstancia();
            Map<String, Object> infoUsuario = ayudantePromocion.obtenerDetallesPromocionUsuario(usuario);
            if (infoUsuario == null) {
                System.out.println("Error: El usuario no cumple con las condiciones necesarias para ser promovido a PREMIUM.");
                System.out.println("Consulte las estadísticas de uso para más información.");
                return false;
            }
            System.out.println("El usuario cumple las siguientes condiciones para promoción:");
            if ((boolean) infoUsuario.get("condicion1")) {
                System.out.println("- Al menos 15 viajes en el último mes (" + infoUsuario.get("viajesUltimoMes") + " viajes)");
            }
            if ((boolean) infoUsuario.get("condicion2")) {
                System.out.println("- Al menos 10 viajes mensuales durante 3 meses consecutivos");
            }
            if ((boolean) infoUsuario.get("condicion3")) {
                System.out.println("- Uso de todos los tipos de vehículos durante 6 meses consecutivos");
            }
        }
        // Conserva los datos del usuario anterior
        String nombre = usuario.getNombre();
        String apellidos = usuario.getApellidos();
        double saldo = usuario.getSaldo();
        List<Viaje> viajes = usuario.getHistorialViajes();
        // Crea el nuevo usuario con el rol actualizado
        Usuario nuevoUsuario;
        if (hacerPremium) {
            nuevoUsuario = new Premium(nombre, apellidos, DNI);
            System.out.println("Usuario promovido a PREMIUM exitosamente.");
        } else {
            nuevoUsuario = new Standard(nombre, apellidos, DNI);
            System.out.println("Usuario convertido a STANDARD exitosamente.");
        }
        // Restaura saldo e historial de viajes
        nuevoUsuario.setSaldo(saldo);
        for (Viaje viaje : viajes) {
            nuevoUsuario.registrarViaje(viaje);
        }
        // Actualiza el usuario en el sistema
        personas.put(DNI, nuevoUsuario);
        return true;
    }

    //=================================================
    // UTILIDADES
    //=================================================
    // Devuelve todas las personas
    public Collection<Persona> getPersonas() {
        return personas.values();
    }

    // ==========================
    // BÚSQUEDA POR DNI
    // ==========================
    public void buscarPersonaPorDNI() {
        // Solicitar DNI
        System.out.print("Introduce el DNI a buscar: ");
        String DNI = UtilidadesEntradaDatos.getDNI("Introduce el DNI a buscar: ");

        // Buscar y mostrar resultado
        Persona persona = gestorPersonas.getPersonaPorDNI(DNI);
        if (persona == null) {
            System.out.println("No se encontró ninguna persona con ese DNI.");
        } else {
            System.out.println("\nPersona encontrada:");
            mostrarDetallesPersona(persona);
        }
    }

    // ==========================
    // BÚSQUEDA POR NOMBRE
    // ==========================
    public void buscarPersonaPorNombre() {
        // Solicitar nombre o parte
        System.out.print("Introduce el nombre o parte del nombre: ");
        String nombre = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre o parte del nombre: ", 1, 50).toLowerCase();

        List<Persona> resultados = new ArrayList<>();
        // Buscar coincidencias en nombre o apellidos
        for (Persona persona : gestorPersonas.getPersonas()) {
            if (persona.getNombre().toLowerCase().contains(nombre) ||
                persona.getApellidos().toLowerCase().contains(nombre)) {
                resultados.add(persona);
            }
        }

        // Mostrar resultados
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron personas con ese nombre o apellido.");
        } else {
            System.out.println("\nSe encontraron " + resultados.size() + " resultados:");
            for (Persona persona : resultados) {
                mostrarDetallesPersona(persona);
                System.out.println("-------------------------");
            }
        }
    }

    // ==========================
    // BÚSQUEDA POR APELLIDO
    // ==========================
    public void buscarPersonaPorApellido() {
        // Solicitar apellido o parte
        System.out.print("Introduce el apellido o parte del apellido: ");
        String apellido = UtilidadesEntradaDatos.getStringLongitud("Introduce el apellido o parte del apellido: ", 1, 50).toLowerCase();

        List<Persona> resultados = new ArrayList<>();
        // Buscar coincidencias solo en apellidos
        for (Persona persona : gestorPersonas.getPersonas()) {
            if (persona.getApellidos().toLowerCase().contains(apellido)) {
                resultados.add(persona);
            }
        }

        // Mostrar resultados
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron personas con ese apellido.");
        } else {
            System.out.println("\nSe encontraron " + resultados.size() + " resultados:");
            for (Persona persona : resultados) {
                mostrarDetallesPersona(persona);
                System.out.println("-------------------------");
            }
        }
    }

    // ==========================
    // FILTRAR PERSONAS POR ROL
    // ==========================
    public void filtrarPersonasPorRol() {
        // Mostrar opciones de rol
        System.out.println("Selecciona el rol a filtrar:");
        System.out.println("1. " + RolEnum.USUARIO_STANDARD);
        System.out.println("2. " + RolEnum.USUARIO_PREMIUM);
        System.out.println("3. " + RolEnum.MECANICO);
        System.out.println("4. " + RolEnum.MANTENIMIENTO);
        System.out.println("5. " + RolEnum.ADMINISTRADOR);

        System.out.print("Introduce opción: ");
        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción (1-5): ", 1, 5);

        RolEnum rolFiltro;
        switch (opcion) {
            case 1 -> rolFiltro = RolEnum.USUARIO_STANDARD;
            case 2 -> rolFiltro = RolEnum.USUARIO_PREMIUM;
            case 3 -> rolFiltro = RolEnum.MECANICO;
            case 4 -> rolFiltro = RolEnum.MANTENIMIENTO;
            case 5 -> rolFiltro = RolEnum.ADMINISTRADOR;
            default -> {
                System.out.println("Opción no válida.");
                return;
            }
        }

        // Filtrar personas por rol
        List<Persona> personasFiltradas = new ArrayList<>();
        for (Persona persona : gestorPersonas.getPersonas()) {
            if (persona.getRol() == rolFiltro) {
                personasFiltradas.add(persona);
            }
        }

        // Mostrar resultados
        if (personasFiltradas.isEmpty()) {
            System.out.println("No se encontraron personas con el rol " + rolFiltro);
        } else {
            System.out.println("\nSe encontraron " + personasFiltradas.size() + " personas con rol " + rolFiltro + ":");
            for (Persona persona : personasFiltradas) {
                mostrarDetallesPersona(persona);
                System.out.println("-------------------------");
            }
        }
    }

     // ==========================
    // MOSTRAR DETALLES DE UNA PERSONA
    // ==========================
    private void mostrarDetallesPersona(Persona persona) {
        // Mostrar datos básicos
        System.out.println("DNI: " + persona.getDNI());
        System.out.println("Nombre: " + persona.getNombre() + " " + persona.getApellidos());
        System.out.println("Rol: " + persona.getRol());

        // Si es usuario, mostrar info adicional
        if (persona instanceof Usuario usuario) {
            System.out.println("Tipo: " + usuario.getRol());
            System.out.println("Viajes realizados: " + usuario.getHistorialViajes().size());
        }
    }
}
