package Menus.MenuAdmin;

import Enum.RolEnum;
import GestorPersonas.Personas.Persona;
import GestorPersonas.Personas.Usuario;
import Utilidades.UtilidadesEntradaDatos;
import java.util.Map;

public class MenuGestionPersonas {

    // ==========================
    // ATRIBUTOS PRINCIPALES
    // ==========================
    private final ServicioGestionPersonas servicioGestionPersonas;

    // ==========================
    // CONSTRUCTOR
    // ==========================
    public MenuGestionPersonas() {
        this.servicioGestionPersonas = new ServicioGestionPersonas();
    }

    // ==========================
    // MENÚ PRINCIPAL DE GESTIÓN DE PERSONAS
    // ==========================
    public void mostrarMenu() {
        boolean volver = false;

        while (!volver) {
            System.out.println("\n========== GESTIÓN DE PERSONAS ==========");
            System.out.println("1. Alta Usuario");
            System.out.println("2. Baja Usuario");
            System.out.println("3. Modificar Usuario");
            System.out.println("4. Mostrar Usuarios para Promoción");
            System.out.println("5. Cambiar Estado Usuario (Standard/Premium)");
            System.out.println("6. Submenu - Buscar Personas");
            System.out.println("7. Volver al menú principal");

            System.out.print("\nSeleccione opción: ");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción", 1, 7);

            try {
                switch (opcion) {
                    case 1 -> altaUsuario();
                    case 2 -> bajaUsuario();
                    case 3 -> modificarUsuario();
                    case 4 -> mostrarUsuariosPromocion();
                    case 5 -> cambiarRolUsuario();
                    case 6 -> menuBuscarPersonas();
                    case 7 -> volver = true;
                    default -> System.out.println("Opción no válida. Por favor, selecciona una opción del menú.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ==========================
    // ALTA DE USUARIO
    // ==========================
    private void altaUsuario() {
        String nombre = UtilidadesEntradaDatos.getStringLongitud("Introduce el nombre del nuevo registro: ", 1, 20);
        String apellidos = UtilidadesEntradaDatos.getStringLongitud("Introduce los apellidos del nuevo registro: ", 1, 20);
        System.out.print("Introduce el DNI del nuevo registro (8 números seguidos de una letra mayúscula): ");
        String DNI = UtilidadesEntradaDatos.getDNI("Introduce el DNI del nuevo registro: ");
        RolEnum rolEnum = servicioGestionPersonas.seleccionarRol();
        servicioGestionPersonas.altaUsuario(nombre, apellidos, DNI, rolEnum);
    }

    // ==========================
    // BAJA DE USUARIO
    // ==========================
    private void bajaUsuario() {
        System.out.print("Introduce el DNI del usuario a eliminar (8 números seguidos de una letra mayúscula): ");
        String DNI = UtilidadesEntradaDatos.getDNI("Introduce el DNI del usuario a eliminar: ");
        servicioGestionPersonas.bajaUsuario(DNI);
    }

    // ==========================
    // MODIFICACIÓN DE USUARIO
    // ==========================
    private void modificarUsuario() {
        System.out.print("Introduce el DNI del usuario a modificar (8 números seguidos de una letra mayúscula): ");
        String DNI = UtilidadesEntradaDatos.getDNI("Introduce el DNI del usuario a modificar: ");

        if (!servicioGestionPersonas.existeDNI(DNI)) {
            System.out.println("Error: El DNI no existe. Operación cancelada.");
            return;
        }

        Persona persona = servicioGestionPersonas.getPersonaPorDNI(DNI);
        System.out.println("\nDatos actuales del usuario:");
        System.out.println("Nombre: " + persona.getNombre());
        System.out.println("Apellidos: " + persona.getApellidos());
        System.out.println("Rol actual: " + persona.getRol());

        boolean eraPremium = persona.getRol() == RolEnum.USUARIO_PREMIUM;

        String nuevoNombre = UtilidadesEntradaDatos.getStringLongitud("Introduce el nuevo nombre", 1, 20);
        String nuevosApellidos = UtilidadesEntradaDatos.getStringLongitud("Introduce los nuevos apellidos", 1, 20);
        RolEnum rolEnum = servicioGestionPersonas.seleccionarRol();

        if (eraPremium && rolEnum == RolEnum.USUARIO_STANDARD) {
            System.out.println("El usuario era Premium. Se mantendrá como USUARIO_PREMIUM.");
            rolEnum = RolEnum.USUARIO_PREMIUM;
        }

        servicioGestionPersonas.modificarPersona(DNI, nuevoNombre, nuevosApellidos, rolEnum);
    }

    // ==========================
    // MOSTRAR USUARIOS PARA PROMOCIÓN
    // ==========================
    private void mostrarUsuariosPromocion() {
        System.out.println("\n===== USUARIOS QUE CUMPLEN CONDICIONES PARA PROMOCIÓN =====");
        servicioGestionPersonas.mostrarUsuariosPromocion();
    }

    // ==========================
    // CAMBIAR ROL DE USUARIO (STANDARD/PREMIUM)
    // ==========================
    private void cambiarRolUsuario() {
        System.out.println("\n--- Modificar Estado de Usuario (Standard/Premium) ---");

        System.out.print("Introduce el DNI del usuario (8 números seguidos de una letra mayúscula): ");
        String DNI = UtilidadesEntradaDatos.getDNI("Introduce el DNI del usuario: ");

        if (!servicioGestionPersonas.existeDNI(DNI)) {
            System.out.println("Error: No existe ningún usuario con el DNI proporcionado.");
            return;
        }
        Persona persona = servicioGestionPersonas.getPersonaPorDNI(DNI);
        if (!(persona instanceof Usuario)) {
            System.out.println("Error: El DNI proporcionado no corresponde a un usuario.");
            return;
        }
        Usuario usuario = (Usuario) persona;
        boolean esPremiumActualmente = usuario.getRol() == RolEnum.USUARIO_PREMIUM;

        System.out.println("\nUsuario: " + usuario.getNombre() + " " + usuario.getApellidos());
        System.out.println("Rol actual: " + usuario.getRol());

        if (esPremiumActualmente) {
            System.out.println("¿Deseas convertir este usuario a STANDARD?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción (1-2): ", 1, 2);

            if (opcion == 1) {
                if (servicioGestionPersonas.cambiarRolUsuario(DNI, false)) {
                    System.out.println("Usuario convertido a STANDARD exitosamente.");
                } else {
                    System.out.println("No se pudo modificar el estado del usuario.");
                }
            } else {
                System.out.println("Operación cancelada. El usuario sigue siendo PREMIUM.");
            }
        } else {
            Map<String, Object> infoUsuario = servicioGestionPersonas.obtenerDetallesPromocionUsuario(usuario);

            if (infoUsuario == null) {
                System.out.println("\nEl usuario NO cumple las condiciones necesarias para ser promovido a PREMIUM.");
                System.out.println("Para ser promovido, debe cumplir al menos una de estas condiciones:");
                System.out.println("- Al menos 15 viajes en el último mes");
                System.out.println("- Al menos 10 viajes mensuales durante 3 meses consecutivos");
                System.out.println("- Uso de todos los tipos de vehículos durante 6 meses consecutivos");
                return;
            }

            System.out.println("\nEl usuario CUMPLE las siguientes condiciones para promoción:");
            if ((boolean) infoUsuario.get("condicion1")) {
                System.out.println("- Al menos 15 viajes en el último mes (" + infoUsuario.get("viajesUltimoMes") + " viajes)");
            }
            if ((boolean) infoUsuario.get("condicion2")) {
                System.out.println("- Al menos 10 viajes mensuales durante 3 meses consecutivos");
            }
            if ((boolean) infoUsuario.get("condicion3")) {
                System.out.println("- Uso de todos los tipos de vehículos durante 6 meses consecutivos");
            }

            System.out.println("\n¿Deseas promover este usuario a PREMIUM?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            int opcionPromocion = UtilidadesEntradaDatos.getEnteroPositivo("Selecciona una opción (1-2): ", 1, 2);

            if (opcionPromocion == 1) {
                if (servicioGestionPersonas.cambiarRolUsuario(DNI, true)) {
                    System.out.println("Usuario promovido a PREMIUM exitosamente.");
                } else {
                    System.out.println("No se pudo modificar el estado del usuario.");
                }
            } else {
                System.out.println("Operación cancelada. El usuario sigue siendo STANDARD.");
            }
        }
    }

    // ==========================
    // MENÚ DE BÚSQUEDA DE PERSONAS
    // ==========================
    private void menuBuscarPersonas() {
        System.out.println("\n=== BÚSQUEDA DE PERSONAS ===");
        System.out.println("1. Buscar por DNI");
        System.out.println("2. Buscar por nombre");
        System.out.println("3. Buscar por apellido");
        System.out.println("4. Filtrar por rol");
        System.out.println("5. Volver al menú anterior");

        System.out.print("\nSeleccione una opción: ");
        int opcion = UtilidadesEntradaDatos.getEnteroPositivo("Introduce una opción (1-5): ", 1, 5);

        switch (opcion) {
            case 1 -> servicioGestionPersonas.buscarPersonaPorDNI();
            case 2 -> servicioGestionPersonas.buscarPersonaPorNombre();
            case 3 -> servicioGestionPersonas.buscarPersonaPorApellido();
            case 4 -> servicioGestionPersonas.filtrarPersonasPorRol();
            case 5 -> { return; }
            default -> System.out.println("Opción no válida.");
        }
    }
}
