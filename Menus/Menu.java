package Menus;

import GestorPersonas.GestorPersonas;
import GestorPersonas.Personas.Administrador;
import GestorPersonas.Personas.Mantenimiento;
import GestorPersonas.Personas.Mecanico;
import GestorPersonas.Personas.Persona;
import GestorPersonas.Personas.Usuario;
import Menus.MenuAdmin.MenuAdministrador;
import Utilidades.UtilidadesEntradaDatos;

public class Menu {

    //=================================================
    // ATRIBUTOS
    //=================================================
    // Gestores del sistema
    private GestorPersonas gestorPersonas;

    // Submenús específicos por rol
    private MenuAdministrador menuAdministrador;
    private MenuMecanico menuMecanico;
    private MenuMantenimiento menuMantenimiento;
    private MenuUsuario menuUsuario;

    //=================================================
    // CONSTRUCTOR
    //=================================================
    // Constructor
    public Menu() {
        this.menuAdministrador = new MenuAdministrador();
        this.menuMecanico = new MenuMecanico();
        this.menuMantenimiento = new MenuMantenimiento();
        this.menuUsuario = new MenuUsuario();
        this.gestorPersonas = GestorPersonas.getInstancia();
    }

    //=================================================
    // MENÚ PRINCIPAL
    //=================================================
    // Método para iniciar el menú principal
    public void iniciar() {
        while (true) {

            System.out.println("\n--------------------------------------------------");
            System.out.println("NOTA IMPORTANTE:");
            System.out.println("- En todas las inicializaciones, puedes acceder al sistema con el Super User:");
            System.out.println("    DNI: 00000000A");
            System.out.println("    Rol: ADMINISTRADOR");
            System.out.println("- Si el sistema se ha inicializado por semilla o manualmente, también estarán disponibles estos registros:");
            System.out.println("    Administradores: 8 dígitos seguidos de 'A' (ejemplo: 00000001A, 00000002A, etc.)");
            System.out.println("    Mantenimiento:   8 dígitos seguidos de 'T' (ejemplo: 00000001T, 00000002T, etc.)");
            System.out.println("    Mecánicos:       8 dígitos seguidos de 'M' (ejemplo: 00000001M, 00000002M, etc.)");
            System.out.println("    Usuarios estándar: 8 dígitos seguidos de 'S' (ejemplo: 00000001S, 00000002S, etc.)");
            System.out.println("- No hay usuarios premium por defecto, pero los usuarios estándar pueden ascender a premium si alcanzan el número de viajes requerido.");
            System.out.println("--------------------------------------------------");

            System.out.println("\n========== SISTEMA DE MOVILIDAD ==========");
            System.out.println("Por favor, identifícate para acceder al sistema");
            String DNI = UtilidadesEntradaDatos.getDNI("Introduce tu DNI (sin espacios ni caracteres especiales): ");
            
            if (!gestorPersonas.existeDNI(DNI)) {
                System.out.println("Error: No existe ningún usuario con el DNI proporcionado.");
                continue;
            }
            if (!gestorPersonas.esDNIValido(DNI)) {
                System.out.println("Error: El DNI introducido no es válido.");
                continue;
            }
            
            Persona persona = gestorPersonas.getPersonaPorDNI(DNI);
            if (persona == null) {
                System.out.println("Error: No se encontró ningún usuario con los datos proporcionados.");
                continue;
            }

            System.out.println("¡Bienvenido/a, " + persona.getNombre() + " " + persona.getApellidos() + "!");
            System.out.println("Accediendo al sistema como: " + persona.getRol());

            switch (persona.getRol()) {
                case ADMINISTRADOR:
                    menuAdministrador.mostrarMenu((Administrador) persona);
                    break;
                case MECANICO:
                    menuMecanico.mostrarMenu((Mecanico) persona);
                    break;
                case MANTENIMIENTO:
                    menuMantenimiento.mostrarMenu((Mantenimiento) persona);
                    break;
                case USUARIO_STANDARD:
                    menuUsuario.mostrarMenu((Usuario) persona);
                    break;
                case USUARIO_PREMIUM:
                    menuUsuario.mostrarMenu((Usuario) persona);
                    break;
                default:
                    System.out.println("Error: Rol no reconocido: " + persona.getRol());
            }
        }
    }
}
